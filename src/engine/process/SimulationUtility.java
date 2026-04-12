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

public class SimulationUtility {

    private static ReputationRepository reputationRepository = ReputationRepository.getInstance();
    private static StockRepository stockageRepository = StockRepository.getInstance();
    private static PropreteRepository propreteRepository = PropreteRepository.getInstance();

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

    public static int calculerTotal(Client client, Commande commande, Serveur serveur,DayStatistics dayStatistics){
        SimulationUtility.appliquerBonusQualite(client,commande);

        int prixPlat = commande.getPrixRecette();
        int pourboire = SimulationUtility.calculerPourboire(client,commande,serveur);

        dayStatistics.addRevenusCommandes(prixPlat);
        dayStatistics.addRevenusPourboire(pourboire);

        return prixPlat + pourboire;
    }

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

    public static int getNombreFours(ArrayList<Meuble> meubles){
        int cpt = 0;
        for(Meuble meuble : meubles){
            if(meuble.getType().equals("FOUR")){
                cpt++;
            }
        }
        return cpt;
    }

    public static int getNombreTables(ArrayList<Meuble> meubles){
        int cpt = 0;
        for(Meuble meuble : meubles){
            if(meuble.getType().equals("TABLE")){
                cpt++;
            }
        }
        return cpt;
    }

    public static boolean peutAcheterCuisinier(ArrayList<Meuble> meubles, int nbCuisiniers){
        int nbFours = getNombreFours(meubles);
        return nbCuisiniers < nbFours;
    }

    public static boolean peutAcheterServeur(ArrayList<Meuble> meubles, int nbServeurs){
        int nbTable = getNombreTables(meubles);
        int maxServeurs = nbTable/2;
        return nbServeurs < maxServeurs;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    public static Image lireImage(String chemin) {
        try {
            return ImageIO.read(new File(chemin));
        } catch (IOException e) {
            System.err.println("-- Impossible de lire le fichier image !--");
            return null;
        }
    }
}
