package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

public class Simulation {
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

    private ArgentRepository argentRepository = ArgentRepository.getInstance();
    private static ReputationRepository reputationRepository = ReputationRepository.getInstance();
    private static StockRepository stockageRepository = StockRepository.getInstance();

    private Block comptoirS;
    private Block comptoirC;
    private Block entree;
    private Block four;

    private boolean stop = false;
    private boolean constructionModeActive = false;
    private boolean alerteStock = false;


    private Zone zoneBlockSelec = null;

    private int speedMultiplier = 1;

    private DayStatistics dayStatistics = new DayStatistics();

    public Simulation() {
        map = GameBuilder.buildMap();
        zones = GameBuilder.buildZones(map);
        meubles = GameBuilder.buildMeubles(map);
        ArrayList<Serveur> serveurs = GameBuilder.buildServeurs(map);
        ArrayList<Cuisinier> cuisiniers = GameBuilder.buildCuisiniers(map);

        manager = new MobileElementManager(map, serveurs,cuisiniers);



        ingredients = GameBuilder.buildIngredients();
        Stockage stockage = GameBuilder.buildStockage(ingredients);
        stockageRepository.setStockage(stockage);
        recettes = GameBuilder.buildRecette(ingredients);

        chronometre = GameBuilder.buildChronometer();

        comptoirS = map.getBlock(22,10);
        comptoirC = map.getBlock(22,9);
        entree = map.getBlock(19,20);
        four = map.getBlock(17, 6);


        for(Meuble meuble : meubles) {
            ajouterTable(meuble);
        }

    }

    public void ajouterTable(Meuble meuble) {
        if (meuble.getType().equals("TABLE")) {
            manager.ajouterTableVide(meuble);
            System.out.println("table libre ajoutée");
        }
    }


    public void nextRound() {
        if (!constructionModeActive && !stop) {
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

                    int total = SimulationUtility.calculerTotal(c,commande,serveur,dayStatistics);

                    argentRepository.ajouterMonnaie(total);

                    manager.libererTable(c);
                    manager.donnerDestinationClient(c,entree);

                    System.out.println("Le client a fini de manger et le total (prix + pourboire) est de  : " + total +"gold");
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
                                manager.donnerDestinationClient(c,table.getPosition());
                                System.out.println("un client entre dans le restaurant");
                            } else {
                                manager.retirerClient(c);
                                System.out.println("un client est sorti");
                            }
                        } else {
                            Recette recette = SimulationUtility.choisirRecetteAlea(recettes);

                            if(recette != null){
                                stockageRepository.recetteUtilisee(recette);



                                Commande commande = new Commande(c, recette);
                                commandesEnAttente.add(commande);
                                manager.donnerDestinationClient(c,null);

                                dayStatistics.addCommande();

                                System.out.println("commande créée" + recette.getNom());


                            }
                            else{
                                manager.libererTable(c);
                                manager.donnerDestinationClient(c,entree);
                                System.out.println("aucun plat disponible, le client part");
                            }


                        }
                    }
                }
            }
        }
    }

    private void generateClient() {
        ArrayList<Recette> disponibles = stockageRepository.recettesDisponibles(recettes);

        if (manager.aDesTablesVides() && !disponibles.isEmpty()) {

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
                System.out.println("un client star arrive !!!!");
            } else if (tirage <= 10) {
                client = ClientFactory.createClient("CRITIQUE", position);
                System.out.println("un client critique arrive !!!!");
            } else {
                client = ClientFactory.createClient("NORMAL", position);
            }

            Meuble tableChoisie = manager.getProchaineTableVide();
            manager.ajouterClient(client,tableChoisie);
        }
    }

    private void satisfactionUpdate(){
        ArrayList<Client> part = new ArrayList<>();

        for(Client client : manager.getClients()){
            if(SimulationUtility.estEnTrainAttendre(client,manager,tempsManger)){
                if(client.getSatisfaction()>0){
                    client.setSatisfaction(client.getSatisfaction()-1);
                }
            }   if(client.getSatisfaction()<=0){
                part.add(client);
            }
        }

        for(Client client : part){
            clientPart0satisfaction(client);
        }
    }

    private void clientPart0satisfaction(Client client){
        manager.nettoyerServeurPourClient(client);
        manager.nettoyerCuisinierPourClient(client);

        nettoyerCommandesClient(client);
        tempsCuisson.remove(client);
        tempsManger.remove(client);
        clientEnTrainManger.remove(client);

        serveurQuiAServi.remove(client);

        manager.libererTable(client);
        manager.donnerDestinationClient(client,entree);

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
                System.out.println("serveur va chercher commande en attente");
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
                System.out.println("serveur va chercher commande prête");
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
                System.out.println(serveur.getName() + " a pris la commande, va au comptoir");

            } else if ("VA_DEPOSER".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                commandesACuisiner.add(commande);
                manager.libererServeur(serveur);
                System.out.println(serveur.getName() + " a déposé la commande au comptoir");

            } else if ("VA_CHERCHER".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Block tableClient = commande.getClient().getPosition();
                Block acote = map.getBlock(tableClient.getLine() - 1, tableClient.getColumn());
                manager.changerEtatServeur(serveur, "VA_SERVIR");
                manager.donnerDestinationServeur(serveur, acote);
                System.out.println(serveur.getName() + " a le plat, va servir le client");

            } else if ("VA_SERVIR".equals(etat)) {
                Commande commande = manager.getCommandeServeur(serveur);
                Client client = commande.getClient();

                tempsManger.put(client, 10);
                clientEnTrainManger.put(client,commande);
                serveurQuiAServi.put(client,serveur);


                manager.libererServeur(serveur);
                System.out.println(serveur.getName() + " a servi, le client mange");
            }
        }
    }

    //Cuisiniers

    private void assignerCuisinier() {
        if (commandesACuisiner.size() > 0) {
            Cuisinier libre = manager.trouverCuisinierLibre();
            if (libre != null) {
                Commande commande = commandesACuisiner.remove(0);

                manager.assignerCommandeCuisinier(libre, commande);
                manager.changerEtatCuisinier(libre, "VA_CHERCHER_COMMANDE");
                manager.donnerDestinationCuisinier(libre, comptoirC);
            }
        }
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
                System.out.println(cuisinier.getName() + " a récupéré la commande, va cuisiner");

            } else if ("VA_CUISINER".equals(etat)) {
                Commande commande = manager.getCommandeCuisinier(cuisinier);
                int duree = commande.getPlat().getRecette().getTempsPreparation();

                tempsCuisson.put(cuisinier, duree);
                commandesCuisson.add(commande);

                manager.changerEtatCuisinier(cuisinier, "CUISINE");
                manager.donnerDestinationCuisinier(cuisinier, null);
                System.out.println(cuisinier.getName() + " cuisine pendant " + duree + " tours");
            }
        }
    }

    private void updateCuisson() {
        ArrayList<Cuisinier> termines = new ArrayList<>();

        for (Cuisinier cuisinier : manager.getCuisiniers()) {
            if (tempsCuisson.containsKey(cuisinier)) {
                int restant = tempsCuisson.get(cuisinier) - 1;
                System.out.println("le plat cuit...");

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

            //double qualite = calculerQualite(cuisinier.getNiveauEtoile());
            int qualite = SimulationUtility.calculerQualite(cuisinier);
            commande.getPlat().setQualite(qualite);

            commandesCuisson.remove(commande);
            commandesPretes.add(commande);
            manager.libererCuisinier(cuisinier);

            System.out.println("Plat prêt ! Qualité : " + qualite
                    + " (chef " + cuisinier.getName() + ")");
        }
    }

    private void nettoyerCommandesClient(Client client){
        Iterator<Commande> it1 = commandesEnAttente.iterator();
        Iterator<Commande> it2 = commandesACuisiner.iterator();
        Iterator<Commande> it3 = commandesCuisson.iterator();
        Iterator<Commande> it4 = commandesPretes.iterator();

        while(it1.hasNext()){
            if(it1.hasNext()){
                Client c = it1.next().getClient();

                if(c == client){
                    it1.remove();
                }

            }
        }
        while(it2.hasNext()){
            if(it2.hasNext()){
                Client c = it2.next().getClient();

                if(c == client){
                    it2.remove();
                }

            }
        }
        while(it3.hasNext()){
            if(it3.hasNext()){
                Client c = it3.next().getClient();

                if(c == client){
                    it3.remove();
                }

            }
        }
        while(it4.hasNext()){
            if(it4.hasNext()){
                Client c = it4.next().getClient();

                if(c == client){
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
                
                for(Block block : construListTemp){
                    ZoneManager.ajouterBlockDansZone(block, zoneDuBlockCible, zoneBlockSelec);
                }

                argentRepository.retirerMonnaie(20);
                dayStatistics.addCoutConstruction(20);

                zoneBlockSelec = null;
                zones.get("CONSTRUCTIBLE").getBlocks().clear();
            } else {
                zoneBlockSelec = null;
                zones.get("CONSTRUCTIBLE").getBlocks().clear();
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

            for(Serveur serveur : manager.getServeurs()){
                int salaire = serveur.getSalaireBase();
                argentRepository.retirerMonnaie(salaire);
                dayStatistics.addCoutSalaires(salaire);
            }

            for(Cuisinier cuisinier : manager.getCuisiniers()){
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

    public boolean isConstructionModeActive() {
        return constructionModeActive;
    }

    public void setConstructionModeActive(boolean constructionModeActive) {
        this.constructionModeActive = constructionModeActive;
        zoneBlockSelec = null;
        zones.get("CONSTRUCTIBLE").getBlocks().clear();
        if (constructionModeActive) {
            System.out.println("mode construction activé");
        } else {
            System.out.println("mode construction désactivé");
        }
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

    public MobileElementManager getManager(){
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