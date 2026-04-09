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

public class RestaurantManager {
    private static Logger logger = LoggerUtility.getLogger(RestaurantManager.class, "html");

    private Simulation simulation;

    private int constructionMode = 0;
    private Zone zoneBlockSelec = null;
    private String meubleACreer = "";

    private static ArgentRepository argentRepository = ArgentRepository.getInstance();

    public RestaurantManager(Simulation simulation) {
        this.simulation = simulation;
    }

    public void enregistrerMeuble(Meuble meuble) {
        MobileElementManager manager = simulation.getManager();
        Map map = simulation.getMap();
        ArrayList<Block> blocksOccupees = simulation.getBlocksOccupees();
        
        if (meuble.getType().equals("TABLE")) {
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
            logger.trace("Table libre ajoutée");
        } else if (meuble.getType().equals("FOUR")) {
            manager.ajouterFourVide(meuble);
            blocksOccupees.add(meuble.getPosition());
            logger.trace("Four libre ajouté");
        }
    }

    public void ajouterMeuble(int ligne, int colonne) {
        Zone zoneMeuble = ZoneManager.getZone(simulation.getMap().getBlock(ligne, colonne), simulation.getZones());
        
        if (zoneMeuble != null) {
            Meuble meuble = new Meuble(simulation.getMap().getBlock(ligne, colonne), meubleACreer);
            simulation.getMeubles().add(meuble);
            enregistrerMeuble(meuble);
            logger.trace("meuble ajouté");
        }
    }

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

                List<Block> construBlocksLigne = ZoneManager.getBlockLigne(ligne, zoneDuBlockCible);
                List<Block> construBlocksColonne = ZoneManager.getBlockColonne(colonne, zoneDuBlockCible);

                List<Block> construListTemp;
                if (construBlocksLigne.size() > construBlocksColonne.size()) {
                    construListTemp = new ArrayList<Block>(construBlocksLigne);
                } else {
                    construListTemp = new ArrayList<Block>(construBlocksColonne);
                }

                int prix = calculerPrixConstruction();
                argentRepository.retirerMonnaie(prix);
                simulation.getDayStatistics().addCoutConstruction(prix);

                for (Block block : construListTemp) {
                    ZoneManager.ajouterBlockDansZone(block, zoneDuBlockCible, zoneBlockSelec);
                    logger.trace("block ajouté à la zone " + zoneBlockSelec.getNom());
                }

                zoneBlockSelec = null;
                simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
            } else {
                zoneBlockSelec = null;
                simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
            }
        }
    }

    public int calculerPrixConstruction() {
        Zone zoneConstructible = simulation.getZones().get("CONSTRUCTIBLE");
        int prix = 0;
        for (Block block : zoneConstructible.getBlocks()) {
            prix += 20;
        }
        return prix;
    }

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