package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private RestaurantManager restaurantManager;

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

    private boolean stop = false;
    private boolean alerteStock = false;


    private ArrayList<Block> blocksOccupees = new ArrayList<>();

    private int speedMultiplier = 1;

    private DayStatistics dayStatistics = new DayStatistics();
    private HashMap<String, Integer> gameStats = new HashMap<>();

    public Simulation() {
        map = GameBuilder.buildMap();
        zones = GameBuilder.buildZones(map);
        meubles = GameBuilder.buildMeubles(map);
        ArrayList<Serveur> serveurs = GameBuilder.buildServeurs(map);
        ArrayList<Cuisinier> cuisiniers = GameBuilder.buildCuisiniers(map);

        manager = new MobileElementManager(map, serveurs, cuisiniers);
        restaurantManager = new RestaurantManager(this);

        ingredients = GameBuilder.buildIngredients();
        Stockage stockage = GameBuilder.buildStockage(ingredients);
        stockageRepository.setStockage(stockage);
        stockageRepository.setNbCases(ingredients.size());
        recettes = GameBuilder.buildRecette(ingredients);

        chronometre = GameBuilder.buildChronometer();

        SuccesRepository.getInstance().setSucces(GameBuilder.buildSucces());

        comptoirS = map.getBlock(22, 10);
        comptoirC = map.getBlock(22, 9);
        entree = map.getBlock(19, 20);

        for (Meuble meuble : new ArrayList<>(meubles)) {
            Zone zoneMeuble = ZoneManager.getZone(meuble.getPosition(), zones);
            restaurantManager.enregistrerMeuble(meuble, zoneMeuble);
        }

        initGameStats();
    }

    private void initGameStats() {
        gameStats.put("depenses", 0);
        gameStats.put("revenus", 0);
        gameStats.put("reputation", 0);
        gameStats.put("nbMeubles", 0);
        gameStats.put("nbServeurs", 0);
        gameStats.put("nbCuisiniers", 0);
        gameStats.put("nbCommandes", 0);
        gameStats.put("achats", 0);
        gameStats.put("loyers", 0);
        gameStats.put("salaires", 0);
        gameStats.put("construction", 0);
        gameStats.put("pourboires", 0);
        gameStats.put("jour", 1);
    }



    public void nextRound() {
        if (restaurantManager.getConstructionMode() == 0 && !stop) {
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

                    logger.trace("Le client a fini de manger et le total (prix + pourboire) est de  : " + total + "gold");
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
                                logger.trace("un client entre dans le restaurant");
                            } else {
                                manager.retirerClient(c);
                                logger.trace("un client est sorti");
                            }
                        } else {
                            Recette recette = SimulationUtility.choisirRecetteAlea(recettes, manager.getNiveauMaxCuisinier());

                            if (recette != null) {
                                stockageRepository.recetteUtilisee(recette);

                                Commande commande = new Commande(c, recette);
                                commandesEnAttente.add(commande);
                                manager.donnerDestinationClient(c, null);

                                dayStatistics.addCommande();

                                logger.trace("commande créée " + recette.getNom());
                            } else {
                                manager.libererTable(c);
                                manager.donnerDestinationClient(c, entree);
                                logger.trace("aucun plat disponible, le client part");
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
                logger.trace("un client star arrive !!!!");
            } else if (tirage <= 10) {
                client = ClientFactory.createClient("CRITIQUE", position);
                logger.trace("un client critique arrive !!!!");
            } else {
                client = ClientFactory.createClient("NORMAL", position);
            }

            Meuble tableChoisie = manager.getProchaineTableVide();
            manager.ajouterClient(client, tableChoisie);
        }
    }

    private void satisfactionUpdate() {
        ArrayList<Client> part = new ArrayList<>();

        boolean minutePaire = (chronometre.getMinute().getValue() % 2 == 0);

        for (Client client : manager.getClients()) {
            if (SimulationUtility.estEnTrainAttendre(client, manager, tempsManger)) {
                if (client.getSatisfaction() > 0 && minutePaire) {
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
        boolean serveurDisponible = true;

        while(commandesEnAttente.size() > 0 && serveurDisponible) {
            Serveur libre = manager.trouverServeurLibre();

            if (libre != null) {
                Commande commande = commandesEnAttente.remove(0);

                Block tableClient = commande.getClient().getPosition();
                Block acote = map.getBlock(
                        tableClient.getLine() - 1,
                        tableClient.getColumn());

                manager.assignerCommandeServeur(libre, commande);
                manager.changerEtatServeur(libre, GameConfiguration.ETAT_VA_PRENDRE);
                logger.trace("serveur va chercher commande en attente");
                manager.donnerDestinationServeur(libre, acote);
            }
            else{
                serveurDisponible = false;
            }
        }
    }

    private void verifierCommandesPretes() {
        boolean serveurDisponible = true;

        while (commandesPretes.size() > 0 && serveurDisponible) {
            Serveur libre = manager.trouverServeurLibre();
            if (libre != null) {
                Commande commande = commandesPretes.remove(0);

                manager.assignerCommandeServeur(libre, commande);
                manager.changerEtatServeur(libre, GameConfiguration.ETAT_VA_CHERCHER);
                logger.trace("serveur va chercher commande prête");
                manager.donnerDestinationServeur(libre, comptoirS);
            }
            else{
                serveurDisponible = false;
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

            if (GameConfiguration.ETAT_VA_PRENDRE.equals(etat)) {
                manager.changerEtatServeur(serveur, GameConfiguration.ETAT_VA_DEPOSER);
                manager.donnerDestinationServeur(serveur, comptoirS);
                logger.trace(serveur.getName() + " a pris la commande, va au comptoir");

            } else if (GameConfiguration.ETAT_VA_DEPOSER.equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                commandesACuisiner.add(commande);
                manager.libererServeur(serveur);
                logger.trace(serveur.getName() + " a déposé la commande au comptoir");

            } else if (GameConfiguration.ETAT_VA_CHERCHER.equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Block tableClient = commande.getClient().getPosition();
                Block acote = map.getBlock(tableClient.getLine() - 1, tableClient.getColumn());
                manager.changerEtatServeur(serveur, GameConfiguration.ETAT_VA_SERVIR);
                manager.donnerDestinationServeur(serveur, acote);
                logger.trace(serveur.getName() + " a le plat, va servir le client");

            } else if (GameConfiguration.ETAT_VA_SERVIR.equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Client client = commande.getClient();

                tempsManger.put(client, 10);
                clientEnTrainManger.put(client, commande);
                serveurQuiAServi.put(client, serveur);

                manager.libererServeur(serveur);
                logger.trace(serveur.getName() + " a servi, le client mange");
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
                manager.changerEtatCuisinier(libre, GameConfiguration.ETAT_VA_CHERCHER_COMMANDE);
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

            if (GameConfiguration.ETAT_VA_CHERCHER_COMMANDE.equals(etat)) {
                manager.changerEtatCuisinier(cuisinier, GameConfiguration.ETAT_VA_CUISINER);
                Meuble fourReserve = manager.occuperProchainFour(cuisinier);
                if(fourReserve != null){
                    manager.donnerDestinationCuisinier(cuisinier,fourReserve.getPosition());
                    logger.trace(cuisinier.getName() + " a récupéré la commande, va cuisiner au four en " + fourReserve.getPosition().toString());
                }

            } else if (GameConfiguration.ETAT_VA_CUISINER.equals(etat)) {
                Commande commande = manager.getCommandeCuisinier(cuisinier);
                int duree = commande.getPlat().getRecette().getTempsPreparation();

                tempsCuisson.put(cuisinier, duree);
                commandesCuisson.add(commande);

                manager.changerEtatCuisinier(cuisinier, GameConfiguration.ETAT_CUISINE);
                manager.donnerDestinationCuisinier(cuisinier, null);
                logger.debug(cuisinier.getName() + " cuisine pendant " + duree + " tours");
            }
        }
    }

    private void updateCuisson() {
        ArrayList<Cuisinier> termines = new ArrayList<>();

        for (Cuisinier cuisinier : manager.getCuisiniers()) {
            if (tempsCuisson.containsKey(cuisinier)) {
                int restant = tempsCuisson.get(cuisinier) - 1;
                logger.debug("le plat cuit...");

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
            manager.libererFour(cuisinier);

            logger.trace("Plat prêt ! Qualité : " + qualite + " (chef " + cuisinier.getName() + ")");
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

            updateGameStats();

            logger.info("Fin de journée");

            return true;
        }
        return false;
    }

    private void updateGameStats() {
        gameStats.put("depenses", dayStatistics.getDepensesDuJour() + gameStats.get("depenses"));
        gameStats.put("revenus", dayStatistics.getRevenusDuJour() + gameStats.get("revenus"));
        gameStats.put("reputation", dayStatistics.getReputationJourPrecedent() + gameStats.get("reputation"));
        gameStats.put("nbMeubles", meubles.size() + gameStats.get("nbMeubles"));
        gameStats.put("nbServeurs", manager.getServeurs().size() + gameStats.get("nbServeurs"));
        gameStats.put("nbCuisiniers", manager.getCuisiniers().size() + gameStats.get("nbCuisiniers"));
        gameStats.put("nbCommandes", dayStatistics.getNbCommandesTotal() + gameStats.get("nbCommandes"));
        gameStats.put("achats", dayStatistics.getAchatDujour() + gameStats.get("achats"));
        gameStats.put("loyers", dayStatistics.getCoutLoyerDuJour() + gameStats.get("loyers"));
        gameStats.put("salaires", dayStatistics.getCoutSalairesDuJour() + gameStats.get("salaires"));
        gameStats.put("construction", dayStatistics.getCoutConstructionDuJour() + gameStats.get("construction"));
        gameStats.put("pourboires", dayStatistics.getRevenusPourboireDuJour() + gameStats.get("pourboires"));
        gameStats.put("jour", dayStatistics.getNbJour());
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

    public ArrayList<Recette> getRecettes() {
        return recettes;
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

    public ArrayList<Block> getBlocksOccupees() {
        return blocksOccupees;
    }

    public Block getComptoirC(){
        return comptoirC;
    }
    public Block getComptoirS(){
        return comptoirS;
    }

    public RestaurantManager getRestaurantManager() { 
        return restaurantManager; 
    }

    public HashMap<String, Integer> getGameStats() { 
        return gameStats; 
    }
}