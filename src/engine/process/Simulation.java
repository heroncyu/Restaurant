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

/**
 * Classe centrale qui est le moteur principal du jeu.
 * 
 * Elle gère l'avancement du temps à chaque tour ("tick") : fait bouger les personnages,
 * s'occupe de la cuisson, du service et mémorise les statistiques.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
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

    private int h;

    private Block comptoirS;
    private Block comptoirC;
    private Block entree;

    private boolean stop = false;
    private boolean alerteStock = false;

    private ArrayList<Block> blocksOccupees = new ArrayList<>();

    private int speedMultiplier = 1;

    private DayStatistics dayStatistics = new DayStatistics();
    private HashMap<String, Integer> gameStats = new HashMap<>();

    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    /**
     * Prépare toute la simulation de A à Z (carte, listes, recettes).
     */
    public Simulation() {
        ArgentRepository.getInstance().reset();
        PropreteRepository.getInstance().reset();
        ReputationRepository.getInstance().reset();
        SuccesRepository.getInstance().reset();

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

        int nbBlocsReserveInitiaux = zones.get("RESERVE").getBlocks().size();
        stockageRepository.setNbCases(nbBlocsReserveInitiaux);

        recettes = GameBuilder.buildRecette(ingredients);

        chronometre = GameBuilder.buildChronometer();

        SuccesRepository.getInstance().setSucces(GameBuilder.buildSucces());

        this.h = map.getLineCount();

        comptoirS = map.getBlock(h-5, 10);
        comptoirC = map.getBlock(h-5, 9);
        entree = map.getBlock(h-1, 15);

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



    /**
     * Fait avancer le jeu d'un instant (bouge les personnages, le chrono...).
     * Est appelée en boucle par le timer principal.
     */
    public void nextRound() {
        if (restaurantManager.getConstructionMode() == 0 && !stop) {
            ArrayList<Recette> recettesDispos = SimulationUtility.recettesParNiveau(recettes, manager.getNiveauMaxCuisinier());
            alerteStock = !stockageRepository.auMoinsUneRecetteDisponible(recettesDispos);
            if (alerteStock) {
                logger.warn("ALERTE STOCK : Plus d'ingrédients suffisants pour préparer les recettes disponibles !");
            }

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
            updateFloatingText();
        }
    }

    private void updateFloatingText(){
        if(floatingTexts.size()>0){
            ArrayList<FloatingText> aSupprimer = new ArrayList<>();
            for( FloatingText floatingText : floatingTexts){
                if(floatingText.getLife()>0){
                    SimulationUtility.lowerLife(floatingText);
                }
                else{
                    aSupprimer.add(floatingText);
                }
            }
            for (FloatingText floatingText : aSupprimer) {
                floatingTexts.remove(floatingText);
            }
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

                    int pixelX = c.getPosition().getColumn() * GameConfiguration.BLOCK_SIZE;
                    int pixelY = c.getPosition().getLine() * GameConfiguration.BLOCK_SIZE;
                    floatingTexts.add(new FloatingText(pixelX, pixelY, Integer.toString(total)) );

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
                                logger.warn("Aucun plat disponible (stock épuisé ou niveau insuffisant), le client part.");
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

            int spawnLine = map.getLineCount()-1;
            int randomColonne = SimulationUtility.getRandomNumber(0, map.getColumnCount() - 1);
            Block spawn = map.getBlock(spawnLine,randomColonne);

            int tirage = SimulationUtility.getRandomNumber(1, 100);
            Client client;
            if (tirage <= 5) {
                client = ClientFactory.createClient("STAR", spawn);
                logger.trace("un client star arrive !!!!");
            } else if (tirage <= 10) {
                client = ClientFactory.createClient("CRITIQUE", spawn);
                logger.trace("un client critique arrive !!!!");
            } else {
                client = ClientFactory.createClient("NORMAL", spawn);
            }

            Meuble tableChoisie = manager.getProchaineTableVide();
            manager.ajouterClient(client, tableChoisie);
            manager.donnerDestinationClient(client, entree);
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
        boolean cuisinierDisponible = true;

        while (commandesACuisiner.size() > 0 && cuisinierDisponible) {
            Commande commande = commandesACuisiner.get(0);
            Recette recette = commande.getPlat().getRecette();

            Cuisinier libre = manager.trouverCuisinierLibre(recette);
            if (libre != null) {
                commandesACuisiner.remove(0);
                manager.assignerCommandeCuisinier(libre, commande);
                manager.changerEtatCuisinier(libre, GameConfiguration.ETAT_VA_CHERCHER_COMMANDE);
                logger.trace("cuisinier va chercher une commande à cuisiner");
                manager.donnerDestinationCuisinier(libre, comptoirC);
            }
            else{
                cuisinierDisponible = false;
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

            if (GameConfiguration.ETAT_VA_CHERCHER_COMMANDE.equals(etat)) {
                manager.changerEtatCuisinier(cuisinier, GameConfiguration.ETAT_VA_CUISINER);
                Meuble fourReserve = manager.occuperProchainFour(cuisinier);

                if(fourReserve != null){
                    Block caseDevantFour = map.getBlock(fourReserve.getPosition().getLine() + 1, fourReserve.getPosition().getColumn());
                    manager.donnerDestinationCuisinier(cuisinier,caseDevantFour);
                    logger.trace(cuisinier.getName() + " a récupéré la commande, va cuisiner au four en " + fourReserve.getPosition().toString());
                }

            } else if (GameConfiguration.ETAT_VA_CUISINER.equals(etat)) {
                cuisinier.setDirection(GameConfiguration.HAUT);

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

    /**
     * Vérifie si c'est l'heure de fermer.
     * Si oui, on coupe la journée et on paye toutes les factures.
     * 
     * @return vrai si la journée vient de se terminer, sinon faux
     */
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
            dayStatistics.setNbJour(dayStatistics.getNbJour()+1);

            updateGameStats();

            logger.info("Fin de journée");

            return true;
        }
        return false;
    }

    private void updateGameStats() {
        gameStats.put("depenses", dayStatistics.getDepensesDuJour() + gameStats.get("depenses"));
        gameStats.put("revenus", dayStatistics.getRevenusDuJour() + gameStats.get("revenus"));
        gameStats.put("reputation", ReputationRepository.getInstance().getReputation());
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

    /**
     * Renvoie la carte du jeu.
     * 
     * @return la carte en 2 dimensions
     */
    public Map getMap() {
        return map;
    }

    /**
     * Renvoie un dictionnaire contenant les zones (Cuisine, Salle...).
     * 
     * @return les zones disponibles
     */
    public HashMap<String, Zone> getZones() {
        return zones;
    }

    /**
     * Renvoie la liste de tous les meubles du restaurant.
     * 
     * @return la liste des meubles
     */
    public ArrayList<Meuble> getMeubles() {
        return meubles;
    }

    /**
     * Renvoie l'horloge du jeu.
     * 
     * @return le chrono (Chronometer)
     */
    public Chronometer getChronometre() {
        return chronometre;
    }

    /**
     * Renvoie l'objet qui gère les calculs d'argent pour aujourd'hui.
     * 
     * @return les statistiques financières du jour
     */
    public DayStatistics getDayStatistics() {
        return dayStatistics;
    }

    /**
     * Renvoie le cahier de recettes avec les plats qu'on sait faire.
     * 
     * @return la liste des recettes
     */
    public ArrayList<Recette> getRecettes() {
        return recettes;
    }

    /**
     * Indique si on manque d'ingrédients pour cuisiner.
     * 
     * @return vrai si le ventre est presque vide
     */
    public boolean isAlerteStock() {
        return alerteStock;
    }

    /**
     * Indique si le jeu est actuellement arrêté (ex: la nuit ou entre 2 menus).
     * 
     * @return vrai si en pause
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * Force la simulation à s'arrêter ou à reprendre.
     * 
     * @param stop vrai pour mettre en pause
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * Change la vitesse du jeu.
     * 
     * @param speedMultiplier nouveau multiplicateur (x1, x2, x3)
     */
    public void setSpeedMultiplier(int speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    /**
     * Récupère la vitesse de la simulation.
     * 
     * @return la vitesse de jeu actuelle
     */
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    /**
     * Récupère le gestionnaire qui fait avancer tout le monde (clients, serveurs).
     * 
     * @return le coordinateur des mouvements
     */
    public MobileElementManager getManager() {
        return this.manager;
    }

    /**
     * Renvoie les commandes prises qu'aucun serveur n'a encore emmenées en cuisine.
     * 
     * @return liste des commandes tout juste passées
     */
    public ArrayList<Commande> getCommandesEnAttente() {
        return new ArrayList<>(commandesEnAttente);
    }

    /**
     * Renvoie toutes les commandes à faire par les cuisiniers.
     * 
     * @return liste des commandes prêtes à cuire
     */
    public ArrayList<Commande> getCommandesACuisiner() {
        return new ArrayList<>(commandesACuisiner);
    }

    /**
     * Renvoie la liste des plats en plein dans le four.
     * 
     * @return commandes en préparation
     */
    public ArrayList<Commande> getCommandesCuisson() {
        return new ArrayList<>(commandesCuisson);
    }

    /**
     * Renvoie la liste des plats cuits et chauds en attente d'être servis.
     * 
     * @return plats posés au comptoir
     */
    public ArrayList<Commande> getCommandesPretes() {
        return new ArrayList<>(commandesPretes);
    }

    /**
     * Récupère toutes les cases de la grille où on ne peut pas marcher (murs, meubles).
     * 
     * @return liste des blocs bloqués
     */
    public ArrayList<Block> getBlocksOccupees() {
        return blocksOccupees;
    }

    /**
     * Récupère la case qui sert de dépôt aux plats pour les cuisiniers.
     * 
     * @return case du comptoir
     */
    public Block getComptoirC(){
        return comptoirC;
    }

    /**
     * Récupère la case du comptoir où les serveurs posent/prennent les plats.
     * 
     * @return case du passe plat
     */
    public Block getComptoirS(){
        return comptoirS;
    }

    /**
     * Récupère le système gérant l'achat de meubles et de salariés.
     * 
     * @return manager du mode édition
     */
    public RestaurantManager getRestaurantManager() { 
        return restaurantManager; 
    }

    /**
     * Récupère les compteurs globaux du jeu (tout ce qui a été vendu depuis le début).
     * 
     * @return grand tableau des totaux de la partie (Dico)
     */
    public HashMap<String, Integer> getGameStats() { 
        return gameStats; 
    }

    public ArrayList<FloatingText> getFloatingTexts() {
        return floatingTexts;
    }

    /**
     * Getter pour récupérer le temps restant d'un cuisinier (pour la barre de progression)
     * @param cuisinier le cuisinier concerné
     * @return le temps restant
     */
    public int getPourcentageCuisson(Cuisinier cuisinier) {
        String etat = manager.getEtatCuisinier(cuisinier);
        if (GameConfiguration.ETAT_CUISINE.equals(etat)) {
            int tempsRestant = tempsCuisson.getOrDefault(cuisinier, 0);
            Commande commande = manager.getCommandeCuisinier(cuisinier);
            if (commande != null) {
                int tempsTotal = commande.getPlat().getRecette().getTempsPreparation();
                if (tempsTotal > 0) {
                    return (int) (((tempsTotal - tempsRestant) / (double) tempsTotal) * 100);
                }
            }
        }
        return -1; //signifie ne cuisine pas
    }
}