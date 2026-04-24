package engine.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import config.GameConfiguration;
import engine.item.Ingredient;
import engine.item.Stockage;
import engine.map.Block;
import engine.map.Zone;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import org.apache.log4j.Logger;
import log.LoggerUtility;

/**
 * Gère la sauvegarde et le chargement de la partie dans un fichier CSV.
 *
 * Chaque ligne commence par un mot-clé qui identifie le type de donnée :
 * <ul>
 *   <li>{@code ARGENT,valeur}</li>
 *   <li>{@code REPUTATION,valeur}</li>
 *   <li>{@code PROPRETE,valeur}</li>
 *   <li>{@code JOUR,valeur}</li>
 *   <li>{@code STOCK,nomIngredient,prixIngredient,quantite}</li>
 *   <li>{@code MEUBLE,type,ligne,colonne}</li>
 *   <li>{@code SERVEUR,nom,niveau,salaire,ligne,colonne}</li>
 *   <li>{@code CUISINIER,nom,niveau,salaire,ligne,colonne}</li>
 *   <li>{@code SUCCES,nom,debloque,reclame}</li>
 *   <li>{@code ZONE,nomZone,ligne,colonne}</li>
 * </ul>
 *
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class SaveManager {

    private static final Logger logger = LoggerUtility.getLogger(SaveManager.class, "html");
    private static final String SAVE_FILE = "saves/save.csv";

    /**
     * Sauvegarde l'état du jeu dans le fichier CSV.
     *
     * @param simulation la simulation en cours
     */
    public static void sauvegarder(Simulation simulation) {
        new File("saves").mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {

            // Zones agrandies (sauf CONSTRUCTIBLE qui est temporaire)
            for (Entry<String, Zone> entry : simulation.getZones().entrySet()) {
                if (entry.getKey().equals("CONSTRUCTIBLE")) continue;
                for (Block b : entry.getValue().getBlocks()) {
                    writer.write("ZONE," + entry.getKey() + "," + b.getLine() + "," + b.getColumn() + "\n");
                }
            }
            // Stock : une ligne par ingrédient
            Stockage stockage = StockRepository.getInstance().getStockage();
            if (stockage != null) {
                for (Entry<Ingredient, Integer> entry : stockage.getIngredients().entrySet()) {
                    Ingredient ing = entry.getKey();
                    writer.write("STOCK," + ing.getNom() + "," + ing.getPrix() + "," + entry.getValue() + "\n");
                }
            }

            // Meubles
            for (Meuble m : simulation.getMeubles()) {
                writer.write("MEUBLE," + m.getType() + "," + m.getPosition().getLine() + "," + m.getPosition().getColumn() + "\n");
            }

            // Serveurs
            for (Serveur s : simulation.getManager().getServeurs()) {
                writer.write("SERVEUR," + s.getName() + "," + s.getNiveau() + "," + s.getSalaireBase()
                        + "," + s.getPosition().getLine() + "," + s.getPosition().getColumn() + "\n");
            }

            // Cuisiniers
            for (Cuisinier c : simulation.getManager().getCuisiniers()) {
                writer.write("CUISINIER," + c.getName() + "," + c.getNiveau() + "," + c.getSalaireBase()
                        + "," + c.getPosition().getLine() + "," + c.getPosition().getColumn() + "\n");
            }

            // Succès
            for (Succes s : SuccesRepository.getInstance().getSucces()) {
                writer.write("SUCCES," + s.getNom() + "," + s.isEstDebloque() + "," + s.isEstReclame() + "\n");
            }

            // Stats globales de la simulation
            for (Entry<String, Integer> entry : simulation.getGameStats().entrySet()) {
                writer.write("GAMESTAT," + entry.getKey() + "," + entry.getValue() + "\n");
            }

            // Argent, réputation, propreté, jour
            writer.write("ARGENT," + ArgentRepository.getInstance().getMonnaie() + "\n");
            writer.write("REPUTATION," + ReputationRepository.getInstance().getReputation() + "\n");
            writer.write("PROPRETE," + PropreteRepository.getInstance().getProprete() + "\n");
            writer.write("JOUR," + simulation.getDayStatistics().getNbJour() + "\n");

            // Historique de l'argent
            for (Integer entry : ArgentRepository.getInstance().getArgentHistory()) {
                writer.write("ARGENT_HIST," + entry + "\n");
            }

            logger.info("Partie sauvegardée dans " + SAVE_FILE);

        } catch (IOException e) {
            logger.error("Erreur de sauvegarde : " + e.getMessage());
        }
    }


    /**
     * Charge la sauvegarde et restaure l'état du jeu.
     *
     * @param simulation la simulation à remplir
     * @return vrai si le chargement a réussi
     */
    public static boolean charger(Simulation simulation) {
        File fichier = new File(SAVE_FILE);
        if (!fichier.exists()) {
            logger.warn("Pas de sauvegarde trouvée.");
            return false;
        }

        HashMap<String, Integer> stockQuantites = new HashMap<String, Integer>();
        HashMap<String, Integer> stockPrix      = new HashMap<String, Integer>();
        HashMap<String, Zone>   zones           = simulation.getZones();
        java.util.List<Integer> argentHistory   = new java.util.ArrayList<Integer>();

        simulation.reinitialiserPourChargement();

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] p = ligne.split(",", -1);
                switch (p[0]) {

                    case "JOUR":
                        simulation.getDayStatistics().setNbJour(Integer.parseInt(p[1]));
                        break;

                    case "STOCK":
                        // STOCK,nom,prix,quantite
                        stockPrix.put(p[1], Integer.parseInt(p[2]));
                        stockQuantites.put(p[1], Integer.parseInt(p[3]));
                        break;

                    case "MEUBLE":
                        // MEUBLE,type,ligne,colonne
                        Block blockMeuble = simulation.getMap().getBlock(Integer.parseInt(p[2]), Integer.parseInt(p[3]));
                        Meuble meuble = new Meuble(blockMeuble, p[1]);
                        Zone zoneMeuble = ZoneManager.getZone(blockMeuble, zones);
                        if (zoneMeuble != null) {
                            int argentAvant = ArgentRepository.getInstance().getMonnaie();
                            simulation.getRestaurantManager().enregistrerMeuble(meuble, zoneMeuble);
                            ArgentRepository.getInstance().setMonnaie(argentAvant); // annule le paiement
                        }
                        break;

                    case "SERVEUR":
                        // SERVEUR,nom,niveau,salaire,ligne,colonne
                        Block posS = simulation.getMap().getBlock(Integer.parseInt(p[4]), Integer.parseInt(p[5]));
                        Serveur serveur = new Serveur(posS, Integer.parseInt(p[2]), Integer.parseInt(p[3]), p[1]);
                        simulation.getManager().ajouterServeur(serveur);
                        break;

                    case "CUISINIER":
                        // CUISINIER,nom,niveau,salaire,ligne,colonne
                        Block posC = simulation.getMap().getBlock(Integer.parseInt(p[4]), Integer.parseInt(p[5]));
                        Cuisinier cuisinier = new Cuisinier(posC, Integer.parseInt(p[2]), Integer.parseInt(p[3]), p[1]);
                        simulation.getManager().ajouterCuisinier(cuisinier);
                        break;

                    case "SUCCES":
                        // SUCCES,nom,debloque,reclame
                        for (Succes s : SuccesRepository.getInstance().getSucces()) {
                            if (s.getNom().equals(p[1])) {
                                s.setEstDebloque(Boolean.parseBoolean(p[2]));
                                s.setEstReclame(Boolean.parseBoolean(p[3]));
                                break;
                            }
                        }
                        break;

                    case "ZONE":
                        // ZONE,nomZone,ligne,colonne
                        Zone zone = zones.get(p[1]);
                        if (zone != null) {
                            zone.ajouterBlock(simulation.getMap().getBlock(Integer.parseInt(p[2]), Integer.parseInt(p[3])));
                        }
                        break;

                    case "GAMESTAT":
                        // GAMESTAT,cle,valeur
                        simulation.getGameStats().put(p[1], Integer.parseInt(p[2]));
                        break;

                    case "ARGENT":
                        ArgentRepository.getInstance().setMonnaie(Integer.parseInt(p[1]));
                        break;

                    case "REPUTATION":
                        int repCible = Integer.parseInt(p[1]);
                        int repActuelle = ReputationRepository.getInstance().getReputation();
                        ReputationRepository.getInstance().ajouterReputation(repCible - repActuelle);
                        break;

                    case "PROPRETE":
                        int propCible = Integer.parseInt(p[1]);
                        int propActuelle = PropreteRepository.getInstance().getProprete();
                        PropreteRepository.getInstance().ajouterProprete(propCible - propActuelle);
                        break;

                    case "ARGENT_HIST":
                        // ARGENT_HIST,valeur
                        argentHistory.add(Integer.parseInt(p[1]));
                        break;

                    default:
                        break;
                }
            }

            // Reconstruit le stock à partir des données lues
            if (!stockQuantites.isEmpty()) {
                HashMap<Ingredient, Integer> ingredientsMap = new HashMap<Ingredient, Integer>();
                for (Entry<String, Integer> entry : stockQuantites.entrySet()) {
                    String nomIng = entry.getKey();
                    int prixIng = stockPrix.get(nomIng);
                    
                    // Cherche l'objet Ingredient correspondant dans la simulation
                    Ingredient ingCible = null;
                    for (Ingredient i : simulation.getIngredients()) {
                        if (i.getNom().equals(nomIng)) {
                            ingCible = i;
                            break;
                        }
                    }
                    
                    // Si pas trouvé (cas rare), on en crée un nouveau
                    if (ingCible == null) {
                        ingCible = new Ingredient(nomIng, prixIng);
                    }
                    
                    ingredientsMap.put(ingCible, entry.getValue());
                }
                StockRepository.getInstance().setStockage(new Stockage(ingredientsMap));
            }

            // Met à jour la capacité du stock selon la taille de la RESERVE
            if (zones.containsKey("RESERVE")) {
                StockRepository.getInstance().setNbCases(zones.get("RESERVE").getBlocks().size());
            }

            // Restaure l'historique de l'argent
            if (!argentHistory.isEmpty()) {
                ArgentRepository.getInstance().setArgentHistory(argentHistory);
            }

            logger.info("Partie chargée depuis " + SAVE_FILE);
            return true;

        } catch (IOException | NumberFormatException e) {
            logger.error("Erreur de chargement : " + e.getMessage());
            return false;
        }
    }

    /**
     * Vérifie si un fichier de sauvegarde existe.
     *
     * @return vrai si la sauvegarde existe
     */
    public static boolean sauvegardeExiste() {
        return new File(SAVE_FILE).exists();
    }
}
