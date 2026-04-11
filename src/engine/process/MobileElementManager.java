package engine.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import config.GameConfiguration;
import org.apache.log4j.Logger;

import engine.item.Commande;
import engine.item.Recette;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.*;
import log.LoggerUtility;

public class MobileElementManager {
    private static Logger logger = LoggerUtility.getLogger(MobileElementManager.class, "html");

    private Map map;

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Serveur> serveurs;
    private ArrayList<Cuisinier> cuisiniers;

    private HashMap<Client, Block> clientDestinations = new HashMap<>();
    private HashMap<Serveur, Block> serveurDestinations = new HashMap<>();
    private HashMap<Cuisinier, Block> cuisinierDestinations = new HashMap<>();


    private HashMap<Serveur, String> serveurEtats = new HashMap<>();
    private HashMap<Serveur, Commande> serveurCommandes = new HashMap<>();

    private HashMap<Cuisinier, String> cuisinierEtats = new HashMap<>();
    private HashMap<Cuisinier, Commande> cuisinierCommandes = new HashMap<>();

    private HashMap<Client, Meuble> tableOccupee = new HashMap<>();
    private List<Meuble> tablesVides = new ArrayList<>();

    private List<Meuble> foursVides = new ArrayList<>();
    private HashMap<Cuisinier, Meuble> fourOccupe = new HashMap<>();

    public MobileElementManager(Map map, ArrayList<Serveur> serveurs,ArrayList<Cuisinier> cuisiniers) {
        this.map = map;
        this.serveurs = serveurs;
        this.cuisiniers = cuisiniers;

        for (Serveur s : serveurs) {
            serveurEtats.put(s, GameConfiguration.ETAT_LIBRE);
        }
        for (Cuisinier s : cuisiniers) {
            cuisinierEtats.put(s, GameConfiguration.ETAT_LIBRE);
        }
    }



    public void moveElementLeft(MobileElement element) {
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.GAUCHE);

        if (position.getColumn() > 0) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() - 1);
            element.setPosition(newPosition);
        }
    }

    public void moveElementRight(MobileElement element) {
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.DROITE);

        if (position.getColumn() < map.getColumnCount() - 1) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() + 1);
            element.setPosition(newPosition);
        }
    }

    public void moveElementBottom(MobileElement element){
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.BAS);

        if(position.getLine()<map.getLineCount()-1){
            Block newPosition=map.getBlock(position.getLine()+1, position.getColumn());
            element.setPosition(newPosition);
        }

    }

    public void moveElementTop(MobileElement element){
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.HAUT);

        if(position.getLine()>0){
            Block newPosition=map.getBlock(position.getLine()-1, position.getColumn());
            element.setPosition(newPosition);
        }
    }



    public boolean moveElementUse(MobileElement element, Block pos) {
        if (pos == null || element == null) return false;

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


    //Client
    public void ajouterTableVide(Meuble table) {
        tablesVides.add(table);
    }

    public void ajouterClient(Client client, Meuble table) {
        clients.add(client);
        tablesVides.remove(table);
        occuperTable(client, table);
        clientDestinations.put(client, table.getPosition());
    }

    public void retirerClient(Client client) {
        clients.remove(client);
        clientDestinations.remove(client);
    }

    public void libererTable(Client client) {
        Meuble table = tableOccupee.remove(client);
        if (table != null) {
            tablesVides.add(table);
        }
    }

    public void occuperTable(Client client, Meuble tableChoisie){
        tableOccupee.put(client,tableChoisie);
    }

    public void donnerDestinationClient(Client client, Block destination) {
        clientDestinations.put(client, destination);
    }



    //Serveur

    public void donnerDestinationServeur(Serveur serveur, Block destination) {
        serveurDestinations.put(serveur, destination);
    }

    public void changerEtatServeur(Serveur serveur, String etat) {
        serveurEtats.put(serveur, etat);
    }

    public void assignerCommandeServeur(Serveur serveur, Commande commande) {
        serveurCommandes.put(serveur, commande);
    }

    public void libererServeur(Serveur serveur) {
        serveurEtats.put(serveur, GameConfiguration.ETAT_LIBRE);
        serveurDestinations.remove(serveur);
        serveurCommandes.remove(serveur);
    }

    public Serveur trouverServeurLibre() {
        Serveur libre = null;
        Iterator<Serveur> it = serveurs.iterator();
        while (it.hasNext() && libre == null) {
            Serveur s = it.next();
            if (GameConfiguration.ETAT_LIBRE.equals(serveurEtats.get(s))) {
                libre = s;
            }
        }
        return libre;
    }

    public void nettoyerServeurPourClient(Client client){
        for(Serveur serveur : serveurs){
            Commande cmd = serveurCommandes.get(serveur);
            if(cmd != null && cmd.getClient() == client){
                libererServeur(serveur);
            }
        }
    }
    public void ajouterServeur(Serveur serveur) {
        serveurs.add(serveur);
        serveurEtats.put(serveur, GameConfiguration.ETAT_LIBRE);
    }

    // Cuisinier

    public Cuisinier trouverCuisinierLibre(Recette recette) {
        Cuisinier libre = null;
        Iterator<Cuisinier> it = cuisiniers.iterator();
        while (it.hasNext() && libre == null) {
            Cuisinier c = it.next();
            logger.info("Cuisinier " + c.getName()
                    + " etat=" + cuisinierEtats.get(c)
                    + " niveau=" + c.getNiveau()
                    + " requis=" + recette.getNiveauRequis());
            if (GameConfiguration.ETAT_LIBRE.equals(cuisinierEtats.get(c)) && c.getNiveau()>= recette.getNiveauRequis()) {
                libre = c;
            }
        }
        return libre;
    }

    public void assignerCommandeCuisinier(Cuisinier cuisinier,
                                          Commande commande) {
        cuisinierCommandes.put(cuisinier, commande);
    }

    public void changerEtatCuisinier(Cuisinier cuisinier, String etat) {
        cuisinierEtats.put(cuisinier, etat);
    }

    public void donnerDestinationCuisinier(Cuisinier cuisinier,
                                           Block destination) {
        cuisinierDestinations.put(cuisinier, destination);
    }

    public void libererCuisinier(Cuisinier cuisinier) {
        cuisinierEtats.put(cuisinier, GameConfiguration.ETAT_LIBRE);
        cuisinierDestinations.remove(cuisinier);
        cuisinierCommandes.remove(cuisinier);
    }

    public void nettoyerCuisinierPourClient(Client client){
        for(Cuisinier cuisinier: cuisiniers){
            Commande cmd = cuisinierCommandes.get(cuisinier);
            if(cmd != null && cmd.getClient() == client){
                libererCuisinier(cuisinier);
                libererFour(cuisinier);
            }
        }
    }

    public int getNiveauMaxCuisinier(){
        int max = 0;
        for(Cuisinier cuisinier : cuisiniers){
            if(cuisinier.getNiveau() > max){
                max = cuisinier.getNiveau();
            }
        }
        return max;
    }

    public void ajouterCuisinier(Cuisinier cuisinier) {
        cuisiniers.add(cuisinier);
        cuisinierEtats.put(cuisinier, GameConfiguration.ETAT_LIBRE);
    }

    //Four

    public void ajouterFourVide(Meuble four) {
        foursVides.add(four);
    }

    public Meuble occuperProchainFour(Cuisinier cuisinier) {
        if (foursVides.isEmpty()) return null;
        Meuble four = foursVides.remove(0);
        fourOccupe.put(cuisinier, four);
        return four;
    }

    public void libererFour(Cuisinier cuisinier) {
        Meuble four = fourOccupe.remove(cuisinier);
        if (four != null) {
            foursVides.add(four);
        }
    }




    // GETTERS

    public Block getDestinationCuisinier(Cuisinier cuisinier) {
        return cuisinierDestinations.get(cuisinier);
    }

    public String getEtatCuisinier(Cuisinier cuisinier) {
        return cuisinierEtats.get(cuisinier);
    }

    public Commande getCommandeCuisinier(Cuisinier cuisinier) {
        return cuisinierCommandes.get(cuisinier);
    }

    public List<Cuisinier> getCuisiniers() {
        return new ArrayList<>(cuisiniers);
    }

    public Block getDestinationClient(Client client) {
        return clientDestinations.get(client);
    }

    public Block getDestinationServeur(Serveur serveur) {
        return serveurDestinations.get(serveur);
    }

    public String getEtatServeur(Serveur serveur) {
        return serveurEtats.get(serveur);
    }

    public Commande getCommandeServeur(Serveur serveur) {
        return serveurCommandes.get(serveur);
    }

    public Meuble getTableClient(Client client) {
        return tableOccupee.get(client);
    }

    public boolean aDesTablesVides() {
        return !tablesVides.isEmpty();
    }

    public Meuble getProchaineTableVide() {
        return tablesVides.get(0);
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public List<Serveur> getServeurs() {
        return new ArrayList<>(serveurs);
    }
}