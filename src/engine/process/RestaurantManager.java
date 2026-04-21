package engine.process;

import java.util.ArrayList;
import java.util.List;

import engine.map.Map;
import org.apache.log4j.Logger;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Zone;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import log.LoggerUtility;

/**
 * Gère tout ce que le joueur ("manageur") fait pendant la partie.
 * 
 * S'occupe d'acheter des meubles, d'agrandir les zones et d'embaucher du personnel.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class RestaurantManager {
    private static Logger logger = LoggerUtility.getLogger(RestaurantManager.class, "html");

    private Simulation simulation;

    private int constructionMode = 0;
    private Zone zoneBlockSelec = null;
    private String meubleACreer = "";

    private static ArgentRepository argentRepository = ArgentRepository.getInstance();

    /**
     * Crée le gestionnaire du restaurant.
     * 
     * @param simulation la simulation principale du jeu
     */
    public RestaurantManager(Simulation simulation) {
        this.simulation = simulation;
    }

    /**
     * Place un meuble sur la carte et paye son prix.
     * 
     * @param meuble le meuble à placer
     * @param zoneMeuble la zone où on le place (ex: Salle pour une table)
     */
    public void enregistrerMeuble(Meuble meuble, Zone zoneMeuble) {
        MobileElementManager manager = simulation.getManager();
        Map map = simulation.getMap();
        ArrayList<Block> blocksOccupees = simulation.getBlocksOccupees();
        
        if (meuble.getType().equals("TABLE")) {
            if (zoneMeuble.getNom().equals("SALLE")) {
                simulation.getMeubles().add(meuble);
                manager.ajouterTableVide(meuble);
                blocksOccupees.add(meuble.getPosition());
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() + 1, meuble.getPosition().getColumn())); // Block du bas
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() - 1, meuble.getPosition().getColumn())); // Block du haut
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine(), meuble.getPosition().getColumn() + 1)); // Block de droite
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine(), meuble.getPosition().getColumn() - 1)); // Block de gauche
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() + 1, meuble.getPosition().getColumn() + 1)); // Block du bas droite
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() + 1, meuble.getPosition().getColumn() - 1)); // Block du bas gauche
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() - 1, meuble.getPosition().getColumn() + 1)); // Block du haut droite
                blocksOccupees.add(map.getBlock(meuble.getPosition().getLine() - 1, meuble.getPosition().getColumn() - 1)); // Block du haut gauche
                argentRepository.retirerMonnaie(GameConfiguration.PRIX_TABLE);
                simulation.getDayStatistics().addAchat(GameConfiguration.PRIX_TABLE);
                logger.trace("Table libre ajoutée");
            } else {
                logger.trace("Veuillez placer la table dans la salle");
            }
            
        } else if (meuble.getType().equals("FOUR")) {
            if (zoneMeuble.getNom().equals("CUISINE")) {
                simulation.getMeubles().add(meuble);
                manager.ajouterFourVide(meuble);
                blocksOccupees.add(meuble.getPosition());
                argentRepository.retirerMonnaie(GameConfiguration.PRIX_FOUR);
                simulation.getDayStatistics().addAchat(GameConfiguration.PRIX_FOUR);
                logger.trace("Four libre ajouté");
            } else {
                logger.trace("Veuillez placer le four dans la cuisine");
            }

        } else if (meuble.getType().equals("PLANTE")) { // Meuble de décoration
            simulation.getMeubles().add(meuble);
            blocksOccupees.add(meuble.getPosition());
            argentRepository.retirerMonnaie(GameConfiguration.PRIX_PLANTE);
            simulation.getDayStatistics().addAchat(GameConfiguration.PRIX_PLANTE);
            logger.trace("Meuble de décoration " + meubleACreer + " ajouté");
        
        } else if (meuble.getType().equals("PORTE_MANTEAU")) { // Meuble de décoration
            simulation.getMeubles().add(meuble);
            blocksOccupees.add(meuble.getPosition());
            argentRepository.retirerMonnaie(GameConfiguration.PRIX_PORTE_MANTEAU);
            simulation.getDayStatistics().addAchat(GameConfiguration.PRIX_PORTE_MANTEAU);
            logger.trace("Meuble de décoration " + meubleACreer + " ajouté");
        }
    }

    /**
     * Essaie d'acheter et de placer un meuble sur une case.
     * 
     * @param ligne coordonnée Y
     * @param colonne coordonnée X
     */
    public void ajouterMeuble(int ligne, int colonne) {
        Block blockMeuble = simulation.getMap().getBlock(ligne, colonne);
        Zone zoneMeuble = ZoneManager.getZone(blockMeuble, simulation.getZones());
        
        if (zoneMeuble != null && !simulation.getBlocksOccupees().contains(blockMeuble)) {
            Meuble meuble = new Meuble(blockMeuble, meubleACreer);
            enregistrerMeuble(meuble, zoneMeuble);
        } else {
            logger.trace("Veuillez placer le meuble dans une zone valide et non occupée");
        }
    }

    /**
     * Agrandit une zone du restaurant en achetant de l'espace vide.
     * 
     * @param ligne coordonnée Y de la case cliquée
     * @param colonne coordonnée X de la case cliquée
     */
    public void agrandirZone(int ligne, int colonne) {
        if (zoneBlockSelec == null) {
            Block blockSelec = simulation.getMap().getBlock(ligne, colonne);
            zoneBlockSelec = ZoneManager.getZone(blockSelec, simulation.getZones());

            if (zoneBlockSelec != null && !zoneBlockSelec.getNom().equals("CONSTRUCTIBLE")) {
                List<Block> blocksColonne = ZoneManager.getBlockColonne(colonne, zoneBlockSelec);
                List<Block> blocksLigne = ZoneManager.getBlockLigne(ligne, zoneBlockSelec);

                List<Block> voisinsConstructibles;
                voisinsConstructibles = ZoneManager.getVoisinsConstructiblesLigne(blocksLigne, simulation.getZones(), simulation.getMap());
                voisinsConstructibles.addAll(ZoneManager.getVoisinsConstructiblesColonne(blocksColonne, simulation.getZones(), simulation.getMap()));
                ZoneManager.ajouterZoneConstructible(voisinsConstructibles, simulation.getZones());
            }
        } else {
            Block blockCible = simulation.getMap().getBlock(ligne, colonne);
            Zone zoneDuBlockCible = ZoneManager.getZone(blockCible, simulation.getZones());
            if (zoneDuBlockCible != null && zoneDuBlockCible.getNom().equals("CONSTRUCTIBLE")) {
                int prix = calculerPrixConstruction();
                if (argentRepository.getMonnaie() >= prix) {

                    List<Block> construBlocksLigne = ZoneManager.getBlockLigne(ligne, zoneDuBlockCible);
                    List<Block> construBlocksColonne = ZoneManager.getBlockColonne(colonne, zoneDuBlockCible);

                    List<Block> construListTemp;
                    if (construBlocksLigne.size() > construBlocksColonne.size()) {
                        construListTemp = new ArrayList<Block>(construBlocksLigne);
                    } else {
                        construListTemp = new ArrayList<Block>(construBlocksColonne);
                    }
                    
                    for (Block block : construListTemp) {
                        ZoneManager.ajouterBlockDansZone(block, zoneDuBlockCible, zoneBlockSelec);
                        logger.trace("block ajouté à la zone " + zoneBlockSelec.getNom());
                    }

                    argentRepository.retirerMonnaie(prix);
                    simulation.getDayStatistics().addCoutConstruction(prix);

                    if (zoneBlockSelec.getNom().equals("RESERVE")) {
                        StockRepository.getInstance().setNbCases(simulation.getZones().get("RESERVE").getBlocks().size());
                    }

                    zoneBlockSelec = null;
                    simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
                } else {
                    logger.trace("Pas assez d'argent pour agrandir la zone");
                }
            } else {
                zoneBlockSelec = null;
                simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
            }
        }
    }

    /**
     * Calcule le prix total de l'espace qu'on veut acheter.
     * 
     * @return prix total à payer
     */
    public int calculerPrixConstruction() {
        Zone zoneConstructible = simulation.getZones().get("CONSTRUCTIBLE");
        int prix = 0;
        for (Block block : zoneConstructible.getBlocks()) {
            prix += 20;
        }
        return prix;
    }

    /**
     * Embauche un nouveau serveur si on a assez d'argent.
     * 
     * @return vrai si l'achat est réussi
     */
    public boolean acheterServeur() {
        int prix = GameConfiguration.PRIX_SERVEUR;
        int nbServeurs = simulation.getManager().getServeurs().size();

        if (!SimulationUtility.peutAcheterServeur(simulation.getMeubles(), nbServeurs)) {
            logger.trace("Pas assez de tables pour un nouveau serveur");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.trace("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        simulation.getDayStatistics().addCoutConstruction(prix);

        String nom = "Serveur " + (nbServeurs + 1);
        Serveur serveur = new Serveur(simulation.getComptoirS(), 1, GameConfiguration.SALAIRE_SERVEUR_BASE, nom);
        simulation.getManager().ajouterServeur(serveur);

        logger.trace("Nouveau serveur : " + nom);
        return true;
    }

    /**
     * Embauche un nouveau cuisinier.
     * 
     * @return vrai si l'achat est réussi
     */
    public boolean acheterCuisinier() {
        int prix = GameConfiguration.PRIX_CUISINIER;
        int nbCuisiniers = simulation.getManager().getCuisiniers().size();

        if (!SimulationUtility.peutAcheterCuisinier(simulation.getMeubles(), nbCuisiniers)) {
            logger.trace("Pas assez de fours pour un nouveau cuisinier");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.trace("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        simulation.getDayStatistics().addCoutConstruction(prix);

        String nom = "Chef " + (nbCuisiniers + 1);
        Cuisinier cuisinier = new Cuisinier(simulation.getComptoirC(), 1, GameConfiguration.SALAIRE_CUISINIER_BASE, nom);
        simulation.getManager().ajouterCuisinier(cuisinier);

        logger.trace("Nouveau cuisinier : " + nom);
        return true;
    }


    /**
     * Améliore un serveur (niveau +1, salaire +20).
     * 
     * @param serveur employé à améliorer
     * @return vrai s'il a été amélioré, faux si niveau max ou pas d'argent
     */
    public boolean ameliorerServeur(Serveur serveur) {
        int prix = GameConfiguration.PRIX_AMELIORATION;

        if (serveur.getNiveau() >= 5) {
            logger.trace("Serveur déjà au niveau maximum");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.trace("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        serveur.setNiveau(serveur.getNiveau() + 1);
        serveur.setSalaireBase(serveur.getSalaireBase() + 20);
        return true;
    }

    /**
     * Améliore un cuisinier (niveau +1, salaire +30).
     * 
     * @param cuisinier cuisinier à améliorer
     * @return vrai s'il a été amélioré, faux si niveau max ou pas d'argent
     */
    public boolean ameliorerCuisinier(Cuisinier cuisinier) {
        int prix = GameConfiguration.PRIX_AMELIORATION;

        if (cuisinier.getNiveau() >= 5) {
            logger.trace("Cuisinier déjà au niveau maximum");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.trace("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        cuisinier.setNiveau(cuisinier.getNiveau() + 1);
        cuisinier.setSalaireBase(cuisinier.getSalaireBase() + 30);
        return true;
    }

    public int getConstructionMode() {
        return constructionMode;
    }

    public void setConstructionMode(int constructionMode) {
        this.constructionMode = constructionMode;
        zoneBlockSelec = null;
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
        if (constructionMode == 1) {
            logger.debug("mode construction = aggrandissement de zone activé");
        } else if (constructionMode == 2) {
            logger.debug("mode construction = ajout de meuble activé");
        } else {
            logger.debug("mode construction désactivé");
        }
    }

    public String getMeubleACreer() {
        return meubleACreer;
    }

    public void setMeubleACreer(String meubleACreer) {
        this.meubleACreer = meubleACreer;
    }
}