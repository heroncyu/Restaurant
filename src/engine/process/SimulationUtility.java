package engine.process;

import engine.item.Commande;
import engine.item.Recette;
import engine.map.Block;
import engine.mobile.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe boîte à outils avec plein de petites fonctions (random, pourboire, niveau...).
 * 
 * Permet d'alléger un peu le gros fichier Simulation.java.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class SimulationUtility {

    private static ReputationRepository reputationRepository = ReputationRepository.getInstance();
    private static StockRepository stockageRepository = StockRepository.getInstance();
    private static PropreteRepository propreteRepository = PropreteRepository.getInstance();

    /**
     * Calcule la qualité d'un plat en fonction du niveau en étoiles du cuisinier.
     * 
     * @param cuisinier la personne qui prépare
     * @return un pourcentage de qualité (ex: 50, 75, 100)
     */
    public static int calculerQualite(Cuisinier cuisinier){
        int niveauEtoile = cuisinier.getNiveau();
        if(niveauEtoile <= 1){
            return 50;
        }
        if(niveauEtoile <= 2){
            return 65;
        }
        if(niveauEtoile <= 3){
            return 75;
        }
        if(niveauEtoile <= 4){
            return 85;
        }
        return 100;
    }

    /**
     * Change le bonheur du client en fonction de la qualité du plat servi.
     * S'il est très bon, le client est heureux. S'il est mauvais, il sera fâché.
     * 
     * @param client le mangeur
     * @param commande la trace des infos du plat mangé
     */
    public static void appliquerBonusQualite(Client client, Commande commande){
        int qualite = commande.getPlat().getQualite();

        if(qualite >= 85){
            client.setSatisfaction(client.getSatisfaction()+20);
        } else if (qualite >= 70) {
            client.setSatisfaction(client.getSatisfaction()+10);
        } else if (qualite < 50) {
            client.setSatisfaction(client.getSatisfaction()-15);
        }

        if(client.getSatisfaction()>100){
            client.setSatisfaction(100);
        }
        if(client.getSatisfaction()<0){
            client.setSatisfaction(0);
        }

    }

    /**
     * Vérifie si le client est juste assis à attendre (il ne mange pas et on ne l'emmène nulle part).
     * 
     * @param client examiné
     * @param manager gère ses déplacements
     * @param tempsManger compteur de temps pour ceux actifs
     * @return vrai s'il attend les mains vides
     */
    public static boolean estEnTrainAttendre(Client client, MobileElementManager manager, HashMap<Client,Integer> tempsManger){
        Block destination = manager.getDestinationClient(client);

        boolean assis;
        if(destination == null){
            assis = true;
        }
        else{
            assis = false;
        }
        boolean mange = tempsManger.containsKey(client);
        return assis && !mange;
    }

    /**
     * Calcule le pourboire (extra) qu'un client va donner. Un client critique est radin, 
     * une star est généreuse. Ça dépend aussi du service.
     * 
     * @param client celui qui paye
     * @param commande la recette qu'il a prise
     * @param serveur celui qui l'a servi (les bons serveurs ont + de pourboires)
     * @return l'argent d'extra en nombre entier
     */
    public static int calculerPourboire(Client client, Commande commande, Serveur serveur) {

        int prixPlat = commande.getPrixRecette();

        if (client instanceof ClientCritique) {
            ClientCritique critique = (ClientCritique) client;
            int satisfaction = client.getSatisfaction();
            if (satisfaction > 80) {
                critique.setImpactReputation(20);
                reputationRepository.ajouterReputation(20);
            } else if (satisfaction >= 50) {
                critique.setImpactReputation(0);
            } else {
                critique.setImpactReputation(-20);
                reputationRepository.ajouterReputation(-20);
            }
            return 0;
        }
        if (client instanceof ClientStar) {
            reputationRepository.ajouterReputation(10);
            return 200;
        }

        double pourcentage;
        int satisfaction = client.getSatisfaction();
        if (satisfaction > 80) {
            pourcentage = 0.40;
        } else if (satisfaction >= 50) {
            pourcentage = 0.25;
        } else {
            pourcentage = 0.1;
        }

        double bonusServeur = 1.0 + (serveur.getNiveau()-1)*0.5;
        return (int)(prixPlat * pourcentage * bonusServeur);
    }

    /**
     * Fonction rapide qui rassemble la facture du client + son pourboire et l'enregistre
     * dans la moyenne de la journée.
     * 
     * @param client le mangeur
     * @param commande repas
     * @param serveur qui l'a amené
     * @param dayStatistics les stats de fin de journée
     * @return prix du plat avec tip (pourboire)
     */
    public static int calculerTotal(Client client, Commande commande, Serveur serveur,DayStatistics dayStatistics){
        SimulationUtility.appliquerBonusQualite(client,commande);

        int prixPlat = commande.getPrixRecette();
        int pourboire = SimulationUtility.calculerPourboire(client,commande,serveur);

        dayStatistics.addRevenusCommandes(prixPlat);
        dayStatistics.addRevenusPourboire(pourboire);

        return prixPlat + pourboire;
    }

    /**
     * Demande à un client de tirer au sort ce qu'il va vouloir manger.
     * Il évite de demander un truc impossible ou qu'un chef n'a pas le niveau de cuisiner.
     * 
     * @param recettes la liste des plats entiers du code
     * @param niveauMaxCuisinier niveau maximum de ceux dans la salle
     * @return recette choisie, ou null s'il y a rien
     */
    public static Recette choisirRecetteAlea(ArrayList<Recette> recettes, int niveauMaxCuisinier){
        ArrayList<Recette> disponibles = stockageRepository.recettesDisponibles(recettes);

        ArrayList<Recette> bonNiveau = new ArrayList<>();
        for(Recette recette : disponibles){
            if(recette.estDebloquee(niveauMaxCuisinier)){
                bonNiveau.add(recette);
            }
        }

        if(bonNiveau.isEmpty()){
            return null;
        }

        int index = (int) (Math.random() * bonNiveau.size());
        return bonNiveau.get(index);
    }

    /**
     * Compte combien il y a de fours posés dans la cuisine.
     * 
     * @param meubles ensemble global de pièces
     * @return nombre de fours posés
     */
    public static int getNombreFours(ArrayList<Meuble> meubles){
        int cpt = 0;
        for(Meuble meuble : meubles){
            if(meuble.getType().equals("FOUR")){
                cpt++;
            }
        }
        return cpt;
    }

    /**
     * Compte combien de tables ont été placées dans le restaurant.
     * 
     * @param meubles ensemble global
     * @return nombre de tables
     */
    public static int getNombreTables(ArrayList<Meuble> meubles){
        int cpt = 0;
        for(Meuble meuble : meubles){
            if(meuble.getType().equals("TABLE")){
                cpt++;
            }
        }
        return cpt;
    }

    /**
     * Vérifie si on peut embaucher (s'il y a un four de libre pour le petit nouveau).
     * 
     * @param meubles stock
     * @param nbCuisiniers quantité d'humains
     * @return vrai si on a un four en plus
     */
    public static boolean peutAcheterCuisinier(ArrayList<Meuble> meubles, int nbCuisiniers){
        int nbFours = getNombreFours(meubles);
        return nbCuisiniers < nbFours;
    }

    /**
     * Vérifie si on peut embaucher un serveur (max 1 serveur pour 2 tables).
     * 
     * @param meubles mobilier présent
     * @param nbServeurs les serveurs déjà présents
     * @return vrai s'il y a trop de tables par rapport aux serveurs, donc ok
     */
    public static boolean peutAcheterServeur(ArrayList<Meuble> meubles, int nbServeurs){
        int nbTable = getNombreTables(meubles);
        int maxServeurs = nbTable/2;
        return nbServeurs < maxServeurs;
    }

    /**
     * Math.Random qui tire un nombre au hasard.
     * 
     * @param min plus petit possible
     * @param max le plus grand
     * @return un entier aléatoire
     */
    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    /**
     * Charge une image depuis le disque (utile pour les sprites).
     * 
     * @param chemin local sur le disque du PC
     * @return variable de Image pour Java
     */
    public static Image lireImage(String chemin) {
        try {
            return ImageIO.read(new File(chemin));
        } catch (IOException e) {
            System.err.println("-- Impossible de lire le fichier image !--");
            return null;
        }
    }

    public static void resetGame() {
        ArgentRepository.getInstance().reset();
        ReputationRepository.getInstance().reset();
        PropreteRepository.getInstance().reset();
        SuccesRepository.getInstance().reset();
    }
}
