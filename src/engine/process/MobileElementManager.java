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

/**
 * Classe gérant les déplacements et les activités de tous les éléments mobiles (serveurs, cuisiniers, clients).
 * 
 * Elle orchestre ce que doit faire un employé ou un client case après case ("pathfinding").
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
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

    /**
     * Initialise le gestionnaire avec les employés de départ.
     * 
     * @param map carte du jeu
     * @param serveurs liste des serveurs du restaurant
     * @param cuisiniers liste des cuisiniers du restaurant
     */
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

    /**
     * Vide complètement les listes d'employés, de clients et tous leurs états.
     * Utilisé lors du chargement d'une sauvegarde pour repartir de zéro.
     */
    public void vider() {
        this.serveurs.clear();
        this.cuisiniers.clear();
        this.clients.clear();
        this.serveurEtats.clear();
        this.cuisinierEtats.clear();
        this.serveurDestinations.clear();
        this.cuisinierDestinations.clear();
        this.serveurCommandes.clear();
        this.cuisinierCommandes.clear();
        this.tablesVides.clear();
        this.foursVides.clear();
        this.tableOccupee.clear();
        this.fourOccupe.clear();
        this.clientDestinations.clear();
    }



    /**
     * Bouge un personnage d'une case vers la gauche.
     * 
     * @param element le personnage à déplacer
     */
    public void moveElementLeft(MobileElement element) {
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.GAUCHE);

        if (position.getColumn() > 0) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() - 1);
            element.setPosition(newPosition);
        }
    }

    /**
     * Bouge un personnage d'une case vers la droite.
     * 
     * @param element le personnage à déplacer
     */
    public void moveElementRight(MobileElement element) {
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.DROITE);

        if (position.getColumn() < map.getColumnCount() - 1) {
            Block newPosition = map.getBlock(position.getLine(), position.getColumn() + 1);
            element.setPosition(newPosition);
        }
    }

    /**
     * Bouge un personnage vers le bas.
     * 
     * @param element le personnage à déplacer
     */
    public void moveElementBottom(MobileElement element){
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.BAS);

        if(position.getLine()<map.getLineCount()-1){
            Block newPosition=map.getBlock(position.getLine()+1, position.getColumn());
            element.setPosition(newPosition);
        }

    }

    /**
     * Bouge un personnage vers le haut.
     * 
     * @param element le personnage à déplacer
     */
    public void moveElementTop(MobileElement element){
        Block position = element.getPosition();
        element.setDirection(GameConfiguration.HAUT);

        if(position.getLine()>0){
            Block newPosition=map.getBlock(position.getLine()-1, position.getColumn());
            element.setPosition(newPosition);
        }
    }



    /**
     * Fait avancer un personnage vers la case de destination en choisissant la bonne direction.
     * 
     * @param element personnage en mouvement
     * @param pos la case qu'on veut atteindre
     * @return vrai si le personnage est arrivé, faux s'il est encore en chemin
     */
    public boolean moveElementUse(MobileElement element, Block pos) {
        if (pos == null || element == null) {
            logger.warn("moveElementUse appelé avec un élément ou une position null !");
            return false;
        }

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


    /**
     * Mémorise qu'une table est libre et prête pour des clients.
     * 
     * @param table la table libre
     */
    public void ajouterTableVide(Meuble table) {
        tablesVides.add(table);
    }

    /**
     * Installe un client sur une table.
     * 
     * @param client le client arrivant
     * @param table la table choisie pour s'asseoir
     */
    public void ajouterClient(Client client, Meuble table) {
        clients.add(client);
        tablesVides.remove(table);
        occuperTable(client, table);
        clientDestinations.put(client, table.getPosition());
    }

    /**
     * Retire un client parti du restaurant de la mémoire du code.
     * 
     * @param client le client qui s'en va
     */
    public void retirerClient(Client client) {
        clients.remove(client);
        clientDestinations.remove(client);
    }

    /**
     * Le client part et rend sa table disponible pour de futurs clients.
     * 
     * @param client client partant
     */
    public void libererTable(Client client) {
        Meuble table = tableOccupee.remove(client);
        if (table != null) {
            tablesVides.add(table);
        }
    }

    /**
     * Attribue une table à un client.
     * 
     * @param client client qui cherche une place
     * @param tableChoisie la table où il se mettra
     */
    public void occuperTable(Client client, Meuble tableChoisie){
        tableOccupee.put(client,tableChoisie);
    }

    /**
     * Offre un bloc final imposant un appel à {@link #moveElementUse(engine.mobile.MobileElement, Block)}.
     * 
     * @param client entité
     * @param destination block cible
     */
    public void donnerDestinationClient(Client client, Block destination) {
        clientDestinations.put(client, destination);
    }



    //Serveur

    /**
     * Dit au serveur où il doit marcher.
     * 
     * @param serveur serveur
     * @param destination case destination
     */
    public void donnerDestinationServeur(Serveur serveur, Block destination) {
        serveurDestinations.put(serveur, destination);
    }

    /**
     * Change l'état actuel d'un serveur ("LIBRE", "PRET_A_DONNER", etc).
     * 
     * @param serveur le serveur
     * @param etat son nouvel état
     */
    public void changerEtatServeur(Serveur serveur, String etat) {
        serveurEtats.put(serveur, etat);
    }

    /**
     * Associe une commande de plat à un serveur pour qu'il aille la livrer.
     * 
     * @param serveur serveur
     * @param commande la commande qu'il porte
     */
    public void assignerCommandeServeur(Serveur serveur, Commande commande) {
        serveurCommandes.put(serveur, commande);
    }

    /**
     * Rend le serveur disponible pour prendre un autre travail.
     * 
     * @param serveur le serveur devenu inactif
     */
    public void libererServeur(Serveur serveur) {
        serveurEtats.put(serveur, GameConfiguration.ETAT_LIBRE);
        serveurDestinations.remove(serveur);
        serveurCommandes.remove(serveur);
    }

    /**
     * Cherche dans la liste un serveur qui ne fait rien.
     * 
     * @return serveur "LIBRE", sinon null
     */
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

    /**
     * Annule la course du serveur si son client est finalement parti parce qu'il a trop attendu.
     * 
     * @param client le client impatient
     */
    public void nettoyerServeurPourClient(Client client){
        for(Serveur serveur : serveurs){
            Commande cmd = serveurCommandes.get(serveur);
            if(cmd != null && cmd.getClient() == client){
                libererServeur(serveur);
            }
        }
    }
    /**
     * Rajoute un serveur venant d'être acheté.
     * 
     * @param serveur le serveur à embaucher
     */
    public void ajouterServeur(Serveur serveur) {
        serveurs.add(serveur);
        serveurEtats.put(serveur, GameConfiguration.ETAT_LIBRE);
    }

    // Cuisinier

    /**
     * Cherche un cuisinier disponible et avec un niveau suffisant pour faire cette recette.
     * 
     * @param recette la recette à faire
     * @return cuisinier prêt ou null si tout le monde travaille
     */
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

    /**
     * Associe la commande au cuisinier pour qu'il commence la préparation.
     * 
     * @param cuisinier auteur du plat
     * @param commande plat désiré
     */
    public void assignerCommandeCuisinier(Cuisinier cuisinier,
                                          Commande commande) {
        cuisinierCommandes.put(cuisinier, commande);
    }

    /**
     * Change l'activité d'un cuisinier ("CUISINE", "LIBRE", etc).
     * 
     * @param cuisinier chef ciblé
     * @param etat son nouvel état
     */
    public void changerEtatCuisinier(Cuisinier cuisinier, String etat) {
        cuisinierEtats.put(cuisinier, etat);
    }

    /**
     * Dit au cuisinier d'aller à une position précise (son four de cuisine).
     * 
     * @param cuisinier le chef
     * @param destination la case du four
     */
    public void donnerDestinationCuisinier(Cuisinier cuisinier,
                                           Block destination) {
        cuisinierDestinations.put(cuisinier, destination);
    }

    /**
     * Quand un plat est préparé, on vide ses infos pour qu'il fasse un autre plat.
     * 
     * @param cuisinier chef qui a fini sa cuisson
     */
    public void libererCuisinier(Cuisinier cuisinier) {
        cuisinierEtats.put(cuisinier, GameConfiguration.ETAT_LIBRE);
        cuisinierDestinations.remove(cuisinier);
        cuisinierCommandes.remove(cuisinier);
    }

    /**
     * Arrête de cuisiner le plat si le client est parti de rage.
     * 
     * @param client le client furieux
     */
    public void nettoyerCuisinierPourClient(Client client){
        for(Cuisinier cuisinier: cuisiniers){
            Commande cmd = cuisinierCommandes.get(cuisinier);
            if(cmd != null && cmd.getClient() == client){
                libererCuisinier(cuisinier);
                libererFour(cuisinier);
            }
        }
    }

    /**
     * Récupère le plus haut niveau (level) parmi tous les cuisiniers actuels.
     * Utile pour afficher ce qu'on peut cuisiner.
     * 
     * @return le niveau maximal actuel dans tout le staff
     */
    public int getNiveauMaxCuisinier(){
        int max = 0;
        for(Cuisinier cuisinier : cuisiniers){
            if(cuisinier.getNiveau() > max){
                max = cuisinier.getNiveau();
            }
        }
        return max;
    }

    /**
     * Embauche un nouveau cuisinier.
     * 
     * @param cuisinier nouveau du personnel
     */
    public void ajouterCuisinier(Cuisinier cuisinier) {
        cuisiniers.add(cuisinier);
        cuisinierEtats.put(cuisinier, GameConfiguration.ETAT_LIBRE);
    }

    //Four

    /**
     * Dit au jeu qu'on a un meuble "Four" qu'un cuisinier peut utiliser.
     * 
     * @param four ajout du four à la liste des dispo
     */
    public void ajouterFourVide(Meuble four) {
        foursVides.add(four);
    }

    /**
     * Donne un four à un cuisinier.
     * 
     * @param cuisinier chef voulant cuisiner
     * @return le four qui lui a été lié
     */
    public Meuble occuperProchainFour(Cuisinier cuisinier) {
        if (foursVides.isEmpty()) return null;
        Meuble four = foursVides.remove(0);
        fourOccupe.put(cuisinier, four);
        return four;
    }

    /**
     * Le cuisinier a fini, le four peut être pris par un collègue.
     * 
     * @param cuisinier le chef qui lâche le four
     */
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