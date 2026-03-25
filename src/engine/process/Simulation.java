package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import config.GameConfiguration;
import engine.item.Commande;
import engine.item.Ingredient;
import engine.item.Recette;
import engine.item.Stockage;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import engine.mobile.*;
import engine.process.chrono.Chronometer;
import log.LoggerUtility;

public class Simulation {
    private static Logger logger = LoggerUtility.getLogger(Simulation.class, "html");

    private Map map;
    private HashMap<String, Zone> zones;
    private MobileElementManager manager;

    private ArrayList<Meuble> meubles;

    private ArrayList<Recette> recettes;
    private ArrayList<Ingredient> ingredients;

    private ArrayList<Commande> commandesEnAttente = new ArrayList<Commande>();
    private ArrayList<Commande> commandesACuisiner = new ArrayList<Commande>();
    private ArrayList<Commande> commandesCuisson = new ArrayList<Commande>();
    private ArrayList<Commande> commandesPretes = new ArrayList<Commande>();

    private HashMap<Cuisinier, Integer> tempsCuisson = new HashMap<>();
    private HashMap<Client, Integer> tempsManger = new HashMap<>();
    private HashMap<Client, Commande> clientEnTrainManger = new HashMap<>();

    private HashMap<Client, Serveur> serveurQuiAServi = new HashMap<>();

    private Chronometer chronometre;

    private static ArgentRepository argentRepository = ArgentRepository.getInstance();
    private static ReputationRepository reputationRepository = ReputationRepository.getInstance();
    private static StockRepository stockageRepository = StockRepository.getInstance();
    private static PropreteRepository propreteRepository = PropreteRepository.getInstance();

    private Block comptoirS;
    private Block comptoirC;
    private Block entree;
    private Block four;

    private boolean stop = false;
    private int constructionMode = 0;
    private boolean alerteStock = false;

    private Zone zoneBlockSelec = null;
    private String meubleACreer = "";

    private int speedMultiplier = 1;

    private DayStatistics dayStatistics = new DayStatistics();

    public Simulation() {
        map = GameBuilder.buildMap();
        zones = GameBuilder.buildZones(map);
        meubles = GameBuilder.buildMeubles(map);
        ArrayList<Serveur> serveurs = GameBuilder.buildServeurs(map);
        ArrayList<Cuisinier> cuisiniers = GameBuilder.buildCuisiniers(map);

        manager = new MobileElementManager(map, serveurs, cuisiniers);

        ingredients = GameBuilder.buildIngredients();
        Stockage stockage = GameBuilder.buildStockage(ingredients);
        stockageRepository.setStockage(stockage);
        recettes = GameBuilder.buildRecette(ingredients);

        chronometre = GameBuilder.buildChronometer();

        SuccesRepository.getInstance().setSucces(GameBuilder.buildSucces());

        comptoirS = map.getBlock(22, 10);
        comptoirC = map.getBlock(22, 9);
        entree = map.getBlock(19, 20);
        four = map.getBlock(17, 6);

        for (Meuble meuble : meubles) {
            ajouterTable(meuble);
        }
    }

    public void ajouterTable(Meuble meuble) {
        if (meuble.getType().equals("TABLE")) {
            manager.ajouterTableVide(meuble);
            logger.info("table libre ajoutée");
        }
    }

    public void ajouterMeuble(int ligne, int colonne) {
        Zone zoneMeuble = ZoneManager.getZone(map.getBlock(ligne, colonne), zones);
        if (zoneMeuble != null) {
            Meuble meuble = new Meuble(map.getBlock(ligne, colonne), meubleACreer);
            meubles.add(meuble);
            if (meuble.getType().equals("TABLE") && zoneMeuble.getNom().equals("SALLE")) {
                manager.ajouterTableVide(meuble);
            }
            logger.info("meuble ajouté");
        }
    }

    public void nextRound() {
        if (constructionMode == 0 && !stop) {
            alerteStock = !stockageRepository.auMoinsUneRecetteDisponible(recettes);

            generateClient();
            satisfactionUpdate();
            moveClients();

            moveServeurs();
            moveCuisiniers();
            updateCuisson();

            assignerServeurPrendre();
            assignerCuisinier();
            verifierCommandesPretes();

            chronometre.increment();

            if (chronometre.getMinute().getValue() == 0) {
                propreteRepository.ajouterProprete(-3);
            }

            SuccesRepository.getInstance().verifierSucces(dayStatistics);
        }
    }

    private void moveClients() {
        Iterator<Client> it = manager.getClients().iterator();
        while (it.hasNext()) {
            Client c = it.next();

            if (tempsManger.containsKey(c)) {
                int temps = tempsManger.get(c) - 1;

                if (temps <= 0) {
                    tempsManger.remove(c);

                    Commande commande = clientEnTrainManger.remove(c);
                    Serveur serveur = serveurQuiAServi.remove(c);

                    dayStatistics.addVenteRecette(commande.getNomRecette());

                    int total = SimulationUtility.calculerTotal(c, commande, serveur, dayStatistics);

                    argentRepository.ajouterMonnaie(total);

                    manager.libererTable(c);
                    manager.donnerDestinationClient(c, entree);

                    logger.info("Le client a fini de manger et le total (prix + pourboire) est de  : " + total + "gold");
                } else {
                    tempsManger.put(c, temps);
                }
            } else {
                Block destination = manager.getDestinationClient(c);
                if (destination != null) {
                    manager.moveElementUse(c, destination);

                    if (c.getPosition().equals(destination)) {
                        if (destination.equals(entree)) {
                            Meuble table = manager.getTableClient(c);
                            if (table != null) {
                                manager.donnerDestinationClient(c, table.getPosition());
                                logger.info("un client entre dans le restaurant");
                            } else {
                                manager.retirerClient(c);
                                logger.info("un client est sorti");
                            }
                        } else {
                            Recette recette = SimulationUtility.choisirRecetteAlea(recettes, manager.getNiveauMaxCuisinier());

                            if (recette != null) {
                                stockageRepository.recetteUtilisee(recette);

                                Commande commande = new Commande(c, recette);
                                commandesEnAttente.add(commande);
                                manager.donnerDestinationClient(c, null);

                                dayStatistics.addCommande();

                                logger.info("commande créée" + recette.getNom());
                            } else {
                                manager.libererTable(c);
                                manager.donnerDestinationClient(c, entree);
                                logger.info("aucun plat disponible, le client part");
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateClient() {
        if (manager.aDesTablesVides()) {

            int reputation = reputationRepository.getReputation();
            if (reputation < 25) {
                int chance = SimulationUtility.getRandomNumber(1, 100);
                if (chance <= 50) {
                    return;
                }
            }

            int middleColumn = map.getColumnCount() / 2;
            int randomLine = SimulationUtility.getRandomNumber(0, map.getLineCount() - 1);
            Block position = map.getBlock(randomLine, middleColumn);

            int tirage = SimulationUtility.getRandomNumber(1, 100);
            Client client;
            if (tirage <= 5) {
                client = ClientFactory.createClient("STAR", position);
                logger.info("un client star arrive !!!!");
            } else if (tirage <= 10) {
                client = ClientFactory.createClient("CRITIQUE", position);
                logger.info("un client critique arrive !!!!");
            } else {
                client = ClientFactory.createClient("NORMAL", position);
            }

            Meuble tableChoisie = manager.getProchaineTableVide();
            manager.ajouterClient(client, tableChoisie);
        }
    }

    private void satisfactionUpdate() {
        ArrayList<Client> part = new ArrayList<>();

        for (Client client : manager.getClients()) {
            if (SimulationUtility.estEnTrainAttendre(client, manager, tempsManger)) {
                if (client.getSatisfaction() > 0) {
                    client.setSatisfaction(client.getSatisfaction() - 1);
                }
            }
            if (client.getSatisfaction() <= 0) {
                part.add(client);
            }
        }

        for (Client client : part) {
            clientPart0satisfaction(client);
        }
    }

    private void clientPart0satisfaction(Client client) {
        manager.nettoyerServeurPourClient(client);
        manager.nettoyerCuisinierPourClient(client);

        nettoyerCommandesClient(client);
        tempsCuisson.remove(client);
        tempsManger.remove(client);
        clientEnTrainManger.remove(client);

        serveurQuiAServi.remove(client);

        manager.libererTable(client);
        manager.donnerDestinationClient(client, entree);
    }

    private void assignerServeurPrendre() {
        if (commandesEnAttente.size() > 0) {
            Serveur libre = manager.trouverServeurLibre();
            if (libre != null) {
                Commande commande = commandesEnAttente.remove(0);

                Block tableClient = commande.getClient().getPosition();
                Block acote = map.getBlock(
                        tableClient.getLine() - 1,
                        tableClient.getColumn());

                manager.assignerCommandeServeur(libre, commande);
                manager.changerEtatServeur(libre, "VA_PRENDRE");
                logger.info("serveur va chercher commande en attente");
                manager.donnerDestinationServeur(libre, acote);
            }
        }
    }

    private void verifierCommandesPretes() {
        if (commandesPretes.size() > 0) {
            Serveur libre = manager.trouverServeurLibre();
            if (libre != null) {
                Commande commande = commandesPretes.remove(0);

                manager.assignerCommandeServeur(libre, commande);
                manager.changerEtatServeur(libre, "VA_CHERCHER");
                logger.info("serveur va chercher commande prête");
                manager.donnerDestinationServeur(libre, comptoirS);
            }
        }
    }

    private void moveServeurs() {
        ArrayList<Serveur> arrives = new ArrayList<>();

        for (Serveur serveur : manager.getServeurs()) {
            Block destination = manager.getDestinationServeur(serveur);
            if (destination == null) continue;

            boolean arrive = manager.moveElementUse(serveur, destination);
            if (arrive) {
                arrives.add(serveur);
            }
        }

        Iterator<Serveur> it = arrives.iterator();
        while (it.hasNext()) {
            Serveur serveur = it.next();
            String etat = manager.getEtatServeur(serveur);

            if ("VA_PRENDRE".equals(etat)) {
                manager.changerEtatServeur(serveur, "VA_DEPOSER");
                manager.donnerDestinationServeur(serveur, comptoirS);
                logger.info(serveur.getName() + " a pris la commande, va au comptoir");

            } else if ("VA_DEPOSER".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                commandesACuisiner.add(commande);
                manager.libererServeur(serveur);
                logger.info(serveur.getName() + " a déposé la commande au comptoir");

            } else if ("VA_CHERCHER".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Block tableClient = commande.getClient().getPosition();
                Block acote = map.getBlock(tableClient.getLine() - 1, tableClient.getColumn());
                manager.changerEtatServeur(serveur, "VA_SERVIR");
                manager.donnerDestinationServeur(serveur, acote);
                logger.info(serveur.getName() + " a le plat, va servir le client");

            } else if ("VA_SERVIR".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Client client = commande.getClient();

                tempsManger.put(client, 10);
                clientEnTrainManger.put(client, commande);
                serveurQuiAServi.put(client, serveur);

                manager.libererServeur(serveur);
                logger.info(serveur.getName() + " a servi, le client mange");
            }
        }
    }

    private void assignerCuisinier() {
        ArrayList<Commande> aRetirer = new ArrayList<>();

        for(Commande commande : commandesACuisiner){
            Recette recette = commande.getPlat().getRecette();

            Cuisinier libre = manager.trouverCuisinierLibre(recette);

            if (libre != null) {
                aRetirer.add(commande);

                manager.assignerCommandeCuisinier(libre, commande);
                manager.changerEtatCuisinier(libre, "VA_CHERCHER_COMMANDE");
                manager.donnerDestinationCuisinier(libre, comptoirC);
            }
        }
        commandesACuisiner.removeAll(aRetirer);
    }

    private void moveCuisiniers() {
        ArrayList<Cuisinier> arrives = new ArrayList<>();

        for (Cuisinier cuisinier : manager.getCuisiniers()) {
            Block destination = manager.getDestinationCuisinier(cuisinier);
            if (destination == null) continue;

            boolean arrive = manager.moveElementUse(cuisinier, destination);
            if (arrive) {
                arrives.add(cuisinier);
            }
        }

        Iterator<Cuisinier> it = arrives.iterator();
        while (it.hasNext()) {
            Cuisinier cuisinier = it.next();
            String etat = manager.getEtatCuisinier(cuisinier);

            if ("VA_CHERCHER_COMMANDE".equals(etat)) {
                manager.changerEtatCuisinier(cuisinier, "VA_CUISINER");
                manager.donnerDestinationCuisinier(cuisinier, four);
                logger.info(cuisinier.getName() + " a récupéré la commande, va cuisiner");

            } else if ("VA_CUISINER".equals(etat)) {
                Commande commande = manager.getCommandeCuisinier(cuisinier);
                int duree = commande.getPlat().getRecette().getTempsPreparation();

                tempsCuisson.put(cuisinier, duree);
                commandesCuisson.add(commande);

                manager.changerEtatCuisinier(cuisinier, "CUISINE");
                manager.donnerDestinationCuisinier(cuisinier, null);
                logger.info(cuisinier.getName() + " cuisine pendant " + duree + " tours");
            }
        }
    }

    private void updateCuisson() {
        ArrayList<Cuisinier> termines = new ArrayList<>();

        for (Cuisinier cuisinier : manager.getCuisiniers()) {
            if (tempsCuisson.containsKey(cuisinier)) {
                int restant = tempsCuisson.get(cuisinier) - 1;
                logger.info("le plat cuit...");

                if (restant <= 0) {
                    termines.add(cuisinier);
                } else {
                    tempsCuisson.put(cuisinier, restant);
                }
            }
        }

        for (Cuisinier cuisinier : termines) {
            tempsCuisson.remove(cuisinier);

            Commande commande = manager.getCommandeCuisinier(cuisinier);

            int qualite = SimulationUtility.calculerQualite(cuisinier);
            commande.getPlat().setQualite(qualite);

            commandesCuisson.remove(commande);
            commandesPretes.add(commande);
            manager.libererCuisinier(cuisinier);

            logger.info("Plat prêt ! Qualité : " + qualite
                    + " (chef " + cuisinier.getName() + ")");
        }
    }

    private void nettoyerCommandesClient(Client client) {
        Iterator<Commande> it1 = commandesEnAttente.iterator();
        Iterator<Commande> it2 = commandesACuisiner.iterator();
        Iterator<Commande> it3 = commandesCuisson.iterator();
        Iterator<Commande> it4 = commandesPretes.iterator();

        while (it1.hasNext()) {
            if (it1.hasNext()) {
                Client c = it1.next().getClient();
                if (c == client) {
                    it1.remove();
                }
            }
        }
        while (it2.hasNext()) {
            if (it2.hasNext()) {
                Client c = it2.next().getClient();
                if (c == client) {
                    it2.remove();
                }
            }
        }
        while (it3.hasNext()) {
            if (it3.hasNext()) {
                Client c = it3.next().getClient();
                if (c == client) {
                    it3.remove();
                }
            }
        }
        while (it4.hasNext()) {
            if (it4.hasNext()) {
                Client c = it4.next().getClient();
                if (c == client) {
                    it4.remove();
                }
            }
        }
    }

    public void agrandirZone(int ligne, int colonne) {
        if (zoneBlockSelec == null) {
            Block blockSelec = map.getBlock(ligne, colonne);
            zoneBlockSelec = ZoneManager.getZone(blockSelec, zones);

            if (zoneBlockSelec != null && !zoneBlockSelec.getNom().equals("CONSTRUCTIBLE")) {
                List<Block> blocksColonne = ZoneManager.getBlockColonne(colonne, zoneBlockSelec);
                List<Block> blocksLigne = ZoneManager.getBlockLigne(ligne, zoneBlockSelec);

                List<Block> voisinsConstructibles;
                voisinsConstructibles = ZoneManager.getVoisinsConstructiblesLigne(blocksLigne, zones, map);
                voisinsConstructibles.addAll(ZoneManager.getVoisinsConstructiblesColonne(blocksColonne, zones, map));
                ZoneManager.ajouterZoneConstructible(voisinsConstructibles, zones);
            }
        } else {
            Block blockCible = map.getBlock(ligne, colonne);
            Zone zoneDuBlockCible = ZoneManager.getZone(blockCible, zones);
            if (zoneDuBlockCible != null && zoneDuBlockCible.getNom().equals("CONSTRUCTIBLE")) {

                List<Block> construBlocksLigne = ZoneManager.getBlockLigne(ligne, zoneDuBlockCible);
                List<Block> construBlocksColonne = ZoneManager.getBlockColonne(colonne, zoneDuBlockCible);

                List<Block> construListTemp;
                if (construBlocksLigne.size() > construBlocksColonne.size()) {
                    construListTemp = new ArrayList<Block>(construBlocksLigne);
                } else {
                    construListTemp = new ArrayList<Block>(construBlocksColonne);
                }

                argentRepository.retirerMonnaie(calculerPrixConstruction());
                dayStatistics.addCoutConstruction(calculerPrixConstruction());

                for (Block block : construListTemp) {
                    ZoneManager.ajouterBlockDansZone(block, zoneDuBlockCible, zoneBlockSelec);
                }

                zoneBlockSelec = null;
                zones.get("CONSTRUCTIBLE").getBlocks().clear();
            } else {
                zoneBlockSelec = null;
                zones.get("CONSTRUCTIBLE").getBlocks().clear();
            }
        }
    }

    public int calculerPrixConstruction() {
        Zone zoneConstructible = zones.get("CONSTRUCTIBLE");
        int prix = 0;
        for (Block block : zoneConstructible.getBlocks()) {
            prix += 20;
        }
        return prix;
    }

    public boolean checkFinJournee() {
        if (chronometre.getHour().getValue() == GameConfiguration.END_OF_DAY_HOUR && chronometre.getMinute().getValue() == 0) {
            stop = true;
            chronometre.init();

            int loyer = ZoneManager.calculerLoyer(zones);
            argentRepository.retirerMonnaie(loyer);
            dayStatistics.addCoutLoyer(loyer);

            for (Serveur serveur : manager.getServeurs()) {
                int salaire = serveur.getSalaireBase();
                argentRepository.retirerMonnaie(salaire);
                dayStatistics.addCoutSalaires(salaire);
            }

            for (Cuisinier cuisinier : manager.getCuisiniers()) {
                int salaire = cuisinier.getSalaireBase();
                argentRepository.retirerMonnaie(salaire);
                dayStatistics.addCoutSalaires(salaire);
            }

            dayStatistics.calculRevenus();
            dayStatistics.calculDepenses();
            dayStatistics.calculBenefices();

            return true;
        }
        return false;
    }

    public boolean acheterServeur() {
        int prix = GameConfiguration.PRIX_SERVEUR;
        int nbServeurs = manager.getServeurs().size();

        if (!SimulationUtility.peutAcheterServeur(meubles, nbServeurs)) {
            logger.info("Pas assez de tables pour un nouveau serveur");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.info("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        dayStatistics.addCoutConstruction(prix);

        String nom = "Serveur " + (nbServeurs + 1);
        Serveur serveur = new Serveur(comptoirS, 1, GameConfiguration.SALAIRE_SERVEUR_BASE, nom);
        manager.ajouterServeur(serveur);

        logger.info("Nouveau serveur : " + nom);
        return true;
    }

    public boolean acheterCuisinier() {
        int prix = GameConfiguration.PRIX_CUISINIER;
        int nbCuisiniers = manager.getCuisiniers().size();

        if (!SimulationUtility.peutAcheterCuisinier(meubles, nbCuisiniers)) {
            logger.info("Pas assez de fours pour un nouveau cuisinier");
            return false;
        }
        if (argentRepository.getMonnaie() < prix) {
            logger.info("Pas assez d'argent");
            return false;
        }

        argentRepository.retirerMonnaie(prix);
        dayStatistics.addCoutConstruction(prix);

        String nom = "Chef " + (nbCuisiniers + 1);
        Cuisinier cuisinier = new Cuisinier(comptoirC, 1, GameConfiguration.SALAIRE_CUISINIER_BASE, nom);
        manager.ajouterCuisinier(cuisinier);

        logger.info("Nouveau cuisinier : " + nom);
        return true;
    }

    public boolean ameliorerServeur(Serveur serveur) {
        int prix = GameConfiguration.PRIX_AMELIORATION;

        if (serveur.getNiveau() >= 5) return false;
        if (argentRepository.getMonnaie() < prix) return false;

        argentRepository.retirerMonnaie(prix);
        serveur.setNiveau(serveur.getNiveau() + 1);
        serveur.setSalaireBase(serveur.getSalaireBase() + 20);
        return true;
    }

    public boolean ameliorerCuisinier(Cuisinier cuisinier) {
        int prix = GameConfiguration.PRIX_AMELIORATION;

        if (cuisinier.getNiveau() >= 5) return false;
        if (argentRepository.getMonnaie() < prix) return false;

        argentRepository.retirerMonnaie(prix);
        cuisinier.setNiveau(cuisinier.getNiveau() + 1);
        cuisinier.setSalaireBase(cuisinier.getSalaireBase() + 30);
        return true;
    }

    private static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    public Map getMap() {
        return map;
    }

    public HashMap<String, Zone> getZones() {
        return zones;
    }

    public ArrayList<Meuble> getMeubles() {
        return meubles;
    }

    public Chronometer getChronometre() {
        return chronometre;
    }

    public DayStatistics getDayStatistics() {
        return dayStatistics;
    }

    public int getConstructionMode() {
        return constructionMode;
    }

    public void setConstructionMode(int constructionMode) {
        this.constructionMode = constructionMode;
        zoneBlockSelec = null;
        zones.get("CONSTRUCTIBLE").getBlocks().clear();
        if (constructionMode == 1) {
            logger.info("mode construction = aggrandissement de zone activé");
        } else if (constructionMode == 2) {
            logger.info("mode construction = ajout de meuble activé");
        } else {
            logger.info("mode construction désactivé");
        }
    }

    public String getMeubleACreer() {
        return meubleACreer;
    }

    public ArrayList<Recette> getRecettes() {
        return recettes;
    }

    public void setMeubleACreer(String meubleACreer) {
        this.meubleACreer = meubleACreer;
    }

    public boolean isAlerteStock() {
        return alerteStock;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setSpeedMultiplier(int speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    public MobileElementManager getManager() {
        return this.manager;
    }

    public ArrayList<Commande> getCommandesEnAttente() {
        return new ArrayList<>(commandesEnAttente);
    }

    public ArrayList<Commande> getCommandesACuisiner() {
        return new ArrayList<>(commandesACuisiner);
    }

    public ArrayList<Commande> getCommandesCuisson() {
        return new ArrayList<>(commandesCuisson);
    }

    public ArrayList<Commande> getCommandesPretes() {
        return new ArrayList<>(commandesPretes);
    }
}