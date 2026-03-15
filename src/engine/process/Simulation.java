package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import config.GameConfiguration;
import engine.item.Commande;
import engine.item.Recette;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import engine.mobile.MobileElement;
import engine.mobile.Client;
import engine.mobile.Serveur;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.process.chrono.Chronometer;

public class Simulation {
    private Map map;
    private HashMap<String, Zone> zones;

    private ArrayList<Meuble> meubles;
    private List<Client> clients = new ArrayList<Client>();
    private List<Serveur> serveurs;
    private List<Cuisinier> cuisiniers;
    private List<Recette> recettes;

    private List<Meuble> tablesVides = new ArrayList<Meuble>();

    private HashMap<Client, Block> clientDestinations = new HashMap<Client, Block>();
    private HashMap<Serveur, Block> serveurDestinations = new HashMap<Serveur, Block>();

    private List<Commande> commandeEnAttente = new ArrayList<Commande>();
    private HashMap<Serveur, Commande> serveurCommandes = new HashMap<Serveur, Commande>();

    private HashMap<Client, Meuble> tableOccupee = new HashMap<Client, Meuble>();

    private Chronometer chronometre;

    private ArgentRepository argentRepository = ArgentRepository.getInstance();

    private Block comptoir;
    private Block entree;

    private boolean stop = false;
    private boolean constructionModeActive = false;

    private Zone zoneBlockSelec = null;

    private int speedMultiplier = 1;

    private DayStatistics dayStatistics = new DayStatistics();

    public Simulation() {
        map = GameBuilder.buildMap();
        zones = GameBuilder.buildZones(map);
        meubles = GameBuilder.buildMeubles(map);

        serveurs = GameBuilder.buildServeurs(map);
        cuisiniers = GameBuilder.buildCuisiniers(map);
        recettes = GameBuilder.buildRecette();
        chronometre = GameBuilder.buildChronometer();
        comptoir = map.getBlock(7, 6);
        entree = map.getBlock(6, 10);

        for (Meuble meuble : meubles) {
            ajouterTable(meuble);
        }

    }

    public void ajouterTable(Meuble meuble) {
        if (meuble.getType().equals("TABLE")) {
            tablesVides.add(meuble);
            System.out.println("table libre ajoutée");
        }
    }

    public void add(Client client) {
        clients.add(client);
    }

    public void add(Serveur serveur) {
        serveurs.add(serveur);
    }

    public void add(Cuisinier cuisinier) {
        cuisiniers.add(cuisinier);
    }

    public void nextRound() {
        if (!constructionModeActive && !stop) {
            generateClient();
            moveClients();
            assignerServeurPrendre();
            moveServeurs();
            satisfactionUpdate();
            chronometre.increment();
        }

    }

    public void moveElementLeft(MobileElement element) {
        Block position = element.getPosition();
        if (position.getColumn() > 0) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() - 1);
            element.setPosition(newPosition);
        }
    }

    public void moveElementRight(MobileElement element) {
        Block position = element.getPosition();
        if (position.getColumn() < map.getColumnCount() - 1) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() + 1);
            element.setPosition(newPosition);
        }
    }

    public void moveElementBottom(MobileElement element) {
        Block position = element.getPosition();
        if (position.getLine() < map.getLineCount() - 1) {
            Block newPosition = map.getBlock(position.getLine() + 1, position.getColumn());
            element.setPosition(newPosition);
        }

    }

    public void moveElementTop(MobileElement element) {
        Block position = element.getPosition();
        if (position.getLine() > 0) {
            Block newPosition = map.getBlock(position.getLine() - 1, position.getColumn());
            element.setPosition(newPosition);
        }
    }

    public boolean moveElementUse(MobileElement element, Block pos) {
        if (pos == null || element == null)
            return false;

        Block actuel = element.getPosition();

        if (actuel.getLine() == pos.getLine() && actuel.getColumn() == pos.getColumn()) {
            return true;
        }

        if (actuel.getLine() < pos.getLine()) {
            moveElementBottom(element);
        } else if (actuel.getLine() > pos.getLine()) {
            moveElementTop(element);
        } else if (actuel.getColumn() < pos.getColumn()) {
            moveElementRight(element);
        } else if (actuel.getColumn() > pos.getColumn()) {
            moveElementLeft(element);
        }

        return false;
    }

    private void moveClients() {
        Iterator<Client> it = clients.iterator();

        while (it.hasNext()) {
            Client c = it.next();
            Block destination = clientDestinations.get(c);

            if (destination != null) {
                moveElementUse(c, destination);

                if (c.getPosition().equals(destination)) {

                    if (destination.equals(entree)) {
                        it.remove();

                        clientDestinations.remove(c);
                        System.out.println("un client est sorti");
                    } else {
                        Commande commande = new Commande(c, recettes.get(0));
                        commandeEnAttente.add(commande);
                        clientDestinations.remove(c);
                        System.out.println("commande créée");
                    }
                }
            }
        }
    }

    private void generateClient() {
        if (!tablesVides.isEmpty()) {
            int middleColumn = map.getColumnCount() / 2;
            int randomLine = getRandomNumber(0, map.getLineCount() - 1);
            Block position = map.getBlock(randomLine, middleColumn);
            Client client = new Client(position);

            Meuble tableChoisie = tablesVides.remove(0);
            Block tablePosition = tableChoisie.getPosition();

            clientDestinations.put(client, tablePosition);
            tableOccupee.put(client, tableChoisie);
            clients.add(client);
        }
    }

    private void satisfactionUpdate() {
        for (Client client : clients) {
            if (client.getSatisfaction() > 0) {
                client.setSatisfaction(client.getSatisfaction() - 2);
            }
        }
    }

    private Serveur trouverServeurLibre() {
        Serveur libre = null;
        Iterator<Serveur> it = serveurs.iterator();
        while (it.hasNext() && libre == null) {
            Serveur s = it.next();
            if (!serveurDestinations.containsKey(s)) {
                libre = s;
            }
        }
        return libre;
    }

    private void assignerServeurPrendre() {
        if (commandeEnAttente.size() > 0) {
            Serveur libre = trouverServeurLibre();
            if (libre != null) {
                Commande commande = commandeEnAttente.remove(0);
                serveurCommandes.put(libre, commande);

                Block destination = map.getBlock(commande.getClient().getPosition().getLine() - 1,
                        commande.getClient().getPosition().getColumn());
                serveurDestinations.put(libre, destination);
                System.out.println("commande prise");
            }
        }

    }

    private void moveServeurs() {
        ArrayList<Serveur> arrives = new ArrayList<>();

        Iterator<Serveur> it = serveurDestinations.keySet().iterator();
        while (it.hasNext()) {
            Serveur serveur = it.next();
            Block destination = serveurDestinations.get(serveur);

            if (destination == serveur.getPosition()) {
                arrives.add(serveur);
            } else {
                moveElementUse(serveur, destination);
            }

        }

        Iterator<Serveur> it2 = arrives.iterator();
        while (it2.hasNext()) {
            Serveur serveur = it2.next();
            Block destination = serveurDestinations.get(serveur);
            if (destination == comptoir) {
                serveurDestinations.remove(serveur);
                System.out.println(serveur.getName() + " est libre");

                Commande commande = serveurCommandes.remove(serveur);
                serveurDestinations.remove(serveur);
                Client client = commande.getClient();

                Meuble table = tableOccupee.get(client);
                tablesVides.add(table);
                System.out.println("table libérée");

                argentRepository.ajouterMonnaie(commande.getPlat().getRecette().getPrix());
                clientDestinations.put(client, entree);

                System.out.println(serveur.getName() + " a fini, client part");
            } else {
                serveurDestinations.put(serveur, comptoir);
                System.out.println(serveur.getName() + " a servi, retourne au comptoir");

            }
        }
    }

    public void agrandirZone(int line, int column) {
        if (zoneBlockSelec == null) {
            Block blockSelec = map.getBlock(line, column);
            zoneBlockSelec = ZoneManager.getZone(blockSelec, zones);

            if (zoneBlockSelec != null && !zoneBlockSelec.getNom().equals("CONSTRUCTIBLE")) {
                List<Block> voisinsConstructibles = ZoneManager.getVoisinsConstructibles(blockSelec, zones, map);
                ZoneManager.afficherZoneConstructible(voisinsConstructibles, zones);
            }
        } else {
            Block blockCible = map.getBlock(line, column);
            Zone zoneDuBlockCible = ZoneManager.getZone(blockCible, zones);
            if (zoneDuBlockCible.getNom().equals("CONSTRUCTIBLE")) {
                ZoneManager.ajouterBlockDansZone(blockCible, zoneDuBlockCible, zoneBlockSelec);

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
            return true;
        }
        return false;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Serveur> getServeurs() {
        return serveurs;
    }

    public List<Cuisinier> getCuisiniers() {
        return cuisiniers;
    }

    public List<Meuble> getTablesVides() {
        return tablesVides;
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

}