package gui;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import engine.process.FloatingText;
import engine.process.SimulationUtility;
import gui.info.JourLabel;
import gui.info.MonnaieLabel;
import gui.info.PropreteLabel;
import gui.info.ReputationLabel;
import gui.info.TempsLabel;

import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;

import static config.GameConfiguration.BLOCK_SIZE;

/**
 * La classe qui contient toutes les règles de dessin.
 *
 * C'est ici qu'on décide à quoi ressemble un client, un four, ou une table.
 *
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class PaintStrategy {
    private Image solHerbe = SimulationUtility.lireImage("src/resources/herbe.jpg");
    private Image solSalle = SimulationUtility.lireImage("src/resources/salle.jpg");
    private Image solCuisine = SimulationUtility.lireImage("src/resources/cuisine.jpg");
    private Image table = SimulationUtility.lireImage("src/resources/table.png");
    private Image four = SimulationUtility.lireImage("src/resources/four.png");
    private Image fourAllume = SimulationUtility.lireImage("src/resources/four_allume.png");
    private Image plante = SimulationUtility.lireImage("src/resources/plante.png");
    private Image tapisEntree = SimulationUtility.lireImage("src/resources/tapis.png");
    private Image porteManteau = SimulationUtility.lireImage("src/resources/porte_manteau.png");

    private Image clientLambda = SimulationUtility.lireImage("src/resources/client.png");

    // Sprites Satisfaction
    private Image satisfactionHigh = SimulationUtility.lireImage("src/resources/satisfactionHIGH.png");
    private Image satisfactionMID = SimulationUtility.lireImage("src/resources/satisfactionMID.png");
    private Image satisfactionLOW = SimulationUtility.lireImage("src/resources/satisfactionLOW.png");
    private Image satisfactionRED = SimulationUtility.lireImage("src/resources/satisfactionRED.png");

    // Sprites Serveur
    private Image servBasRepos = SimulationUtility.lireImage("src/resources/serveuse/serv_bas_repos.png");
    private Image servBas1 = SimulationUtility.lireImage("src/resources/serveuse/serv_bas_1.png");
    private Image servBas2 = SimulationUtility.lireImage("src/resources/serveuse/serv_bas_2.png");
    private Image servHautRepos = SimulationUtility.lireImage("src/resources/serveuse/serv_haut_repos.png");
    private Image servHaut1 = SimulationUtility.lireImage("src/resources/serveuse/serv_haut_1.png");
    private Image servHaut2 = SimulationUtility.lireImage("src/resources/serveuse/serv_haut_2.png");
    private Image servGaucheRepos = SimulationUtility.lireImage("src/resources/serveuse/serv_gauche_repos.png");
    private Image servGauche1 = SimulationUtility.lireImage("src/resources/serveuse/serv_gauche_1.png");
    private Image servGauche2 = SimulationUtility.lireImage("src/resources/serveuse/serv_gauche_2.png");
    private Image servDroiteRepos = SimulationUtility.lireImage("src/resources/serveuse/serv_droite_repos.png");
    private Image servDroite1 = SimulationUtility.lireImage("src/resources/serveuse/serv_droite_1.png");
    private Image servDroite2 = SimulationUtility.lireImage("src/resources/serveuse/serv_droite_2.png");

    // Sprites Cuisinier
    private Image cuisHautRepos = SimulationUtility.lireImage("src/resources/cuisinier/cuis_HAUT_repos.png");
    private Image cuisHaut1 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_HAUT_1.png");
    private Image cuisHaut2 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_HAUT_2.png");
    private Image cuisBasRepos = SimulationUtility.lireImage("src/resources/cuisinier/cuis_BAS_repos.png");
    private Image cuisBas1 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_BAS_1.png");
    private Image cuisBas2 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_BAS_2.png");
    private Image cuisGaucheRepos = SimulationUtility.lireImage("src/resources/cuisinier/cuis_GAUCHE_repos.png");
    private Image cuisGauche1 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_GAUCHE_1.png");
    private Image cuisGauche2 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_GAUCHE_2.png");
    private Image cuisDroiteRepos = SimulationUtility.lireImage("src/resources/cuisinier/cuis_DROITE_repos.png");
    private Image cuisDroite1 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_DROITE_1.png");
    private Image cuisDroite2 = SimulationUtility.lireImage("src/resources/cuisinier/cuis_DROITE_2.png");

    //Sprites client riche
    private Image richeHautRepos = SimulationUtility.lireImage("src/resources/client/riche/client_haut_repos.png");
    private Image richeHaut1 = SimulationUtility.lireImage("src/resources/client/riche/client_haut_1.png");
    private Image richeHaut2 = SimulationUtility.lireImage("src/resources/client/riche/client_haut_2.png");
    private Image richeBasRepos = SimulationUtility.lireImage("src/resources/client/riche/client_bas_repos.png");
    private Image richeBas1 = SimulationUtility.lireImage("src/resources/client/riche/client_bas_1.png");
    private Image richeBas2 = SimulationUtility.lireImage("src/resources/client/riche/client_bas_2.png");
    private Image richeGaucheRepos = SimulationUtility.lireImage("src/resources/client/riche/client_gauche_repos.png");
    private Image richeGauche1 = SimulationUtility.lireImage("src/resources/client/riche/client_gauche_1.png");
    private Image richeGauche2 = SimulationUtility.lireImage("src/resources/client/riche/client_gauche_2.png");
    private Image richeDroiteRepos = SimulationUtility.lireImage("src/resources/client/riche/client_droite_repos.png");
    private Image richeDroite1 = SimulationUtility.lireImage("src/resources/client/riche/client_droite_1.png");
    private Image richeDroite2 = SimulationUtility.lireImage("src/resources/client/riche/client_droite_2.png");

    //Sprites client lambda
    private Image lambdaHautRepos = SimulationUtility.lireImage("src/resources/client/lambda/client_haut_repos.png");
    private Image lambdaHaut1 = SimulationUtility.lireImage("src/resources/client/lambda/client_haut_1.png");
    private Image lambdaHaut2 = SimulationUtility.lireImage("src/resources/client/lambda/client_haut_2.png");
    private Image lambdaBasRepos = SimulationUtility.lireImage("src/resources/client/lambda/client_bas_repos.png");
    private Image lambdaBas1 = SimulationUtility.lireImage("src/resources/client/lambda/client_bas_1.png");
    private Image lambdaBas2 = SimulationUtility.lireImage("src/resources/client/lambda/client_bas_2.png");
    private Image lambdaGaucheRepos = SimulationUtility.lireImage("src/resources/client/lambda/client_gauche_repos.png");
    private Image lambdaGauche1 = SimulationUtility.lireImage("src/resources/client/lambda/client_gauche_1.png");
    private Image lambdaGauche2 = SimulationUtility.lireImage("src/resources/client/lambda/client_gauche_2.png");
    private Image lambdaDroiteRepos = SimulationUtility.lireImage("src/resources/client/lambda/client_droite_repos.png");
    private Image lambdaDroite1 = SimulationUtility.lireImage("src/resources/client/lambda/client_droite_1.png");
    private Image lambdaDroite2 = SimulationUtility.lireImage("src/resources/client/lambda/client_droite_2.png");

    //Sprites client critique
    private Image critiqueHautRepos = SimulationUtility.lireImage("src/resources/client/critique/client_haut_repos.png");
    private Image critiqueHaut1 = SimulationUtility.lireImage("src/resources/client/critique/client_haut_1.png");
    private Image critiqueHaut2 = SimulationUtility.lireImage("src/resources/client/critique/client_haut_2.png");
    private Image critiqueBasRepos = SimulationUtility.lireImage("src/resources/client/critique/client_bas_repos.png");
    private Image critiqueBas1 = SimulationUtility.lireImage("src/resources/client/critique/client_bas_1.png");
    private Image critiqueBas2 = SimulationUtility.lireImage("src/resources/client/critique/client_bas_2.png");
    private Image critiqueGaucheRepos = SimulationUtility.lireImage("src/resources/client/critique/client_gauche_repos.png");
    private Image critiqueGauche1 = SimulationUtility.lireImage("src/resources/client/critique/client_gauche_1.png");
    private Image critiqueGauche2 = SimulationUtility.lireImage("src/resources/client/critique/client_gauche_2.png");
    private Image critiqueDroiteRepos = SimulationUtility.lireImage("src/resources/client/critique/client_droite_repos.png");
    private Image critiqueDroite1 = SimulationUtility.lireImage("src/resources/client/critique/client_droite_1.png");
    private Image critiqueDroite2 = SimulationUtility.lireImage("src/resources/client/critique/client_droite_2.png");

    private Image route = SimulationUtility.lireImage("src/resources/route.png");
    private Image arbre1 = SimulationUtility.lireImage("src/resources/arbre_1.png");
    private Image arbre2 = SimulationUtility.lireImage("src/resources/arbre_2.png");

    /**
     * Dessine le sol de toutes les cases (herbe, salle, cuisine...).
     *
     * @param map la carte du restaurant
     * @param zones la liste des zones pour savoir quoi dessiner
     * @param graphics pinceau de dessin
     */
    public void paint(Map map, HashMap<String, Zone> zones, Graphics graphics) {
        int blockSize = BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();


        for (int ligne = 0; ligne < map.getLineCount(); ligne++) {
            for (int colonne = 0; colonne < map.getColumnCount(); colonne++) {
                Block block = blocks[ligne][colonne];

                int x = colonne * blockSize;
                int y = ligne * blockSize;

                String cord = String.valueOf(block.getLine());
                graphics.drawString(cord, x + 4, y + 15);

                if (ligne == map.getLineCount() - 1) {
                    if (route != null) {
                        graphics.drawImage(route, x, y, blockSize, blockSize, null);
                    } else {
                        graphics.setColor(new Color(80, 80, 80));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }
                    continue;
                }

                String nomZone = null;
                Iterator<Zone> it = zones.values().iterator();
                while (it.hasNext()) {
                    Zone zone = it.next();
                    if (zone.appartientBlock(block)) {
                        nomZone = zone.getNom();
                    }
                }

                if (nomZone == null) {
                    if (solHerbe != null){
                        graphics.drawImage(solHerbe, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(34, 139, 34));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }
                } else if (nomZone.equals("CUISINE")) {
                    if(solSalle != null){
                        graphics.drawImage(solCuisine, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(200, 200, 200));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }


                } else if (nomZone.equals("SALLE")) {
                    if(solSalle != null){
                        graphics.drawImage(solSalle, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(222, 184, 135));
                        graphics.fillRect(x, y, blockSize, blockSize);
                    }

                } else if (nomZone.equals("RESERVE")) {
                    graphics.setColor(new Color(100, 100, 100));
                    graphics.fillRect(x, y, blockSize, blockSize);
                } else if (nomZone.equals("CONSTRUCTIBLE")) {
                    graphics.setColor(Color.cyan);
                    graphics.fillRect(x, y, blockSize, blockSize);
                }

                if(block.equals(map.getBlock(22, 15))){
                    if(tapisEntree != null){
                        graphics.drawImage(tapisEntree, x, y, blockSize, blockSize, null);
                    }
                    else{
                        graphics.setColor(new Color(255, 255, 255));
                        graphics.fillRect(x, y, blockSize-10, blockSize-10);
                    }
                }



                //graphics.setColor(Color.BLACK);
                //graphics.drawRect(colonne * blockSize, ligne * blockSize, blockSize, blockSize);
            }
        }
    }

    /**
     * Met en rouge les cases déjà occupées quand on essaie de construire un meuble.
     *
     * @param blocksOccupees les cases où il y a déjà un mur ou un meuble
     * @param graphics pinceau
     */
    public void paint(List<Block> blocksOccupees, Graphics graphics) {
        int blockSize = BLOCK_SIZE;
        for (Block block : blocksOccupees) {
            graphics.setColor(new Color(255, 0, 0, 32));
            graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
        }
    }

    /**
     * Dessine les meubles (fours, tables, plantes) sur l'écran.
     *
     * @param meuble le meuble à dessiner
     * @param graphics pinceau
     */
    public void paint(Meuble meuble, Graphics graphics, boolean estAllume) {
        Block position = meuble.getPosition();
        int blockSize = BLOCK_SIZE;

        int x = position.getColumn() * blockSize;
        int y = position.getLine() * blockSize;

        String type = meuble.getType();

        if (type.equals("FOUR")) {
            Image imageFour = estAllume ? fourAllume : four;
            if (four != null) {
                graphics.drawImage(imageFour, x - 10, y - 10, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.setColor(new Color(240, 231, 230));
                graphics.fillRect(x + 4, y + 4, blockSize - 10, blockSize - 10);
                graphics.setColor(new Color(105, 100, 99));
                graphics.drawRect(x + 4, y + 4, blockSize - 10, blockSize - 10);
                graphics.setColor(new Color(23, 20, 20));
                graphics.fillRect(x + 11, y + 11, blockSize - 24, blockSize - 24);
            }
        } else if (type.equals("TABLE")) {
            if (table != null) {
                graphics.drawImage(table, x - 30, y - 30, blockSize + 60, blockSize + 60, null);
            } else {
                graphics.setColor(new Color(139, 69, 19));
                graphics.fillOval(x + 6, y + 6, blockSize - 12, blockSize - 12);
                graphics.setColor(new Color(100, 50, 10));
                graphics.drawOval(x + 6, y + 6, blockSize - 12, blockSize - 12);
            }

        } else if (type.equals("PLANTE")) {
            if (plante != null) {
                graphics.drawImage(plante, x - 10, y - 15, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.setColor(new Color(139, 90, 43));
                graphics.fillOval(x + 10, y + 10, blockSize - 20, blockSize - 20);
                graphics.setColor(new Color(0, 160, 0));
                graphics.fillOval(x + 15, y + 15, blockSize - 30, blockSize - 30);
                graphics.setColor(new Color(177, 110, 186));
                graphics.fillOval(x + 30, y + 25, blockSize - 70, blockSize - 70);
                graphics.fillOval(x + 50, y + 35, blockSize - 70, blockSize - 70);
            }
        } else if (type.equals("PORTE_MANTEAU")) {
            if (porteManteau != null) {
                graphics.drawImage(porteManteau, x - 10, y - 18, blockSize + 20, blockSize + 20, null);
            } else {
                graphics.drawString("P.Manteau", x, y);
            }
        }
    }

    /**
     * Dessine le client (riche, lambda ou critique) avec sa jauge de satisfaction au-dessus.
     *
     * @param client la personne
     * @param graphics pinceau
     */
    public void paint(Client client, Graphics graphics, int animationTick,boolean enMouvement) {
        Block position = client.getPosition();
        int blockSize = BLOCK_SIZE;

        int x = position.getColumn() * blockSize;
        int y = position.getLine() * blockSize;

        Image imageADessiner = null;

        int agrandissement = 10;
        int tailleImage = blockSize + agrandissement;

        int decalageX = agrandissement / 2;
        int decalageY = agrandissement - 5;
        String direction = client.getDirection();

        boolean estArrete = !enMouvement;

        if (client instanceof engine.mobile.ClientStar) {
            switch (direction) {
                case GameConfiguration.BAS:
                    if (estArrete) imageADessiner = richeBasRepos;
                    else imageADessiner = (animationTick == 0) ? richeBas1 : richeBas2;
                    break;

                case GameConfiguration.HAUT:
                    if (estArrete) imageADessiner = richeHautRepos;
                    else imageADessiner = (animationTick == 0) ? richeHaut1 : richeHaut2;
                    break;

                case GameConfiguration.GAUCHE:
                    if (estArrete) imageADessiner = richeGaucheRepos;
                    else imageADessiner = (animationTick == 0) ? richeGauche1 : richeGauche2;
                    break;

                case GameConfiguration.DROITE:
                    if (estArrete) imageADessiner = richeDroiteRepos;
                    else imageADessiner = (animationTick == 0) ? richeDroite1 : richeDroite2;
                    break;
            }
        } else if (client instanceof engine.mobile.ClientCritique) {
            switch (direction) {
                case GameConfiguration.BAS:
                    if (estArrete) imageADessiner = critiqueBasRepos;
                    else imageADessiner = (animationTick == 0) ? critiqueBas1 : critiqueBas2;
                    break;

                case GameConfiguration.HAUT:
                    if (estArrete) imageADessiner = critiqueHautRepos;
                    else imageADessiner = (animationTick == 0) ? critiqueHaut1 : critiqueHaut2;
                    break;

                case GameConfiguration.GAUCHE:
                    if (estArrete) imageADessiner = critiqueGaucheRepos;
                    else imageADessiner = (animationTick == 0) ? critiqueGauche1 : critiqueGauche2;
                    break;

                case GameConfiguration.DROITE:
                    if (estArrete) imageADessiner = critiqueDroiteRepos;
                    else imageADessiner = (animationTick == 0) ? critiqueDroite1 : critiqueDroite2;
                    break;
            }
        }
        else{
            switch (direction) {
                case GameConfiguration.BAS:
                    if (estArrete) imageADessiner = lambdaBasRepos;
                    else imageADessiner = (animationTick == 0) ? lambdaBas1 : lambdaBas2;
                    break;

                case GameConfiguration.HAUT:
                    if (estArrete) imageADessiner = lambdaHautRepos;
                    else imageADessiner = (animationTick == 0) ? lambdaHaut1 : lambdaHaut2;
                    break;

                case GameConfiguration.GAUCHE:
                    if (estArrete) imageADessiner = lambdaGaucheRepos;
                    else imageADessiner = (animationTick == 0) ? lambdaGauche1 : lambdaGauche2;
                    break;

                case GameConfiguration.DROITE:
                    if (estArrete) imageADessiner = lambdaDroiteRepos;
                    else imageADessiner = (animationTick == 0) ? lambdaDroite1 : lambdaDroite2;
                    break;
            }
        }

        if (imageADessiner != null) {
            graphics.drawImage(imageADessiner, x -decalageX, y-decalageY, tailleImage, tailleImage, null);
        } else {
            graphics.setColor(Color.BLUE);
            graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);
        }

        int satisfaction = client.getSatisfaction();
        Image aDessiner = satisfactionHigh;

        if (satisfaction > 50 && satisfaction <= 75) {
            aDessiner = satisfactionMID;
        } else if (satisfaction <= 50) {
            aDessiner = satisfactionLOW;
        } else if(satisfaction <= 25) {
            aDessiner = satisfactionRED;
        }

        if(aDessiner != null){
            graphics.drawImage(aDessiner, x+30, y-10, blockSize-20, blockSize-20, null);
        }
        else{
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Dialog", Font.BOLD, 14));
            graphics.drawString(String.valueOf(satisfaction), x + 2, y + 12);
        }
    }

    /**
     * Dessine les cuisiniers dans la cuisine avec leur nom.
     *
     * @param cuisinier l'employé
     * @param graphics pinceau
     */
    public void paint(Cuisinier cuisinier, Graphics graphics, int animationTick,boolean enMouvement, int pourcentageCuisson) {
        Block position = cuisinier.getPosition();
        int blockSize = BLOCK_SIZE;

        int y = position.getLine() * blockSize;
        int x = position.getColumn() * blockSize;

        int agrandissement = 10;
        int tailleImage = blockSize + agrandissement;

        int decalageX = agrandissement / 2;
        int decalageY = agrandissement - 5;

        Image imageADessiner = cuisBasRepos;
        String direction = cuisinier.getDirection();

        boolean estArrete = !enMouvement;

        switch (direction) {
            case GameConfiguration.BAS:
                if (estArrete) imageADessiner = cuisBasRepos;
                else imageADessiner = (animationTick == 0) ? cuisBas1 : cuisBas2;
                break;

            case GameConfiguration.HAUT:
                if (estArrete) imageADessiner = cuisHautRepos;
                else imageADessiner = (animationTick == 0) ? cuisHaut1 : cuisHaut2;
                break;

            case GameConfiguration.GAUCHE:
                if (estArrete) imageADessiner = cuisGaucheRepos;
                else imageADessiner = (animationTick == 0) ? cuisGauche1 : cuisGauche2;
                break;

            case GameConfiguration.DROITE:
                if (estArrete) imageADessiner = cuisDroiteRepos;
                else imageADessiner = (animationTick == 0) ? cuisDroite1 : cuisDroite2;
                break;
        }

        if(imageADessiner != null){
            graphics.drawImage(imageADessiner, x - decalageX, y - decalageY, tailleImage, tailleImage, null);
        }
        else {
            graphics.setColor(Color.YELLOW);
            graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);
        }

        if (pourcentageCuisson >= 0) {
            int largeurBarre = blockSize;
            int remplissage = (int) (largeurBarre * (pourcentageCuisson / 100.0));

            graphics.setColor(Color.BLACK);
            graphics.fillRect(x, y + 30, largeurBarre, 6);

            graphics.setColor(new Color(255, 165, 0));
            graphics.fillRect(x + 1, y + 30, remplissage - 2, 4);
        }


        graphics.setColor(new Color(0, 0, 0));
        graphics.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        graphics.drawString(cuisinier.getName(), x + 5, y + 5);
    }

    /**
     * Dessine les serveurs qui bougent dans le restaurant avec leur nom.
     *
     * @param serveur l'employé
     * @param graphics pinceau
     * @param animationTick variable pour une future animation de marche
     */
    public void paint(Serveur serveur, Graphics graphics, int animationTick,boolean enMouvement) {
        Block position = serveur.getPosition();
        int blockSize = BLOCK_SIZE;

        int y = position.getLine() * blockSize;
        int x = position.getColumn() * blockSize;


        int agrandissement = 10;
        int tailleImage = blockSize + agrandissement;

        int decalageX = agrandissement / 2;
        int decalageY = agrandissement - 5;

        Image imageADessiner = servBasRepos;
        String direction = serveur.getDirection();

        boolean estArrete = !enMouvement;

        switch (direction) {
            case GameConfiguration.BAS:
                if (estArrete) imageADessiner = servBasRepos;
                else imageADessiner = (animationTick == 0) ? servBas1 : servBas2;
                break;

            case GameConfiguration.HAUT:
                if (estArrete) imageADessiner = servHautRepos;
                else imageADessiner = (animationTick == 0) ? servHaut1 : servHaut2;
                break;

            case GameConfiguration.GAUCHE:
                if (estArrete) imageADessiner = servGaucheRepos;
                else imageADessiner = (animationTick == 0) ? servGauche1 : servGauche2;
                break;

            case GameConfiguration.DROITE:
                if (estArrete) imageADessiner = servDroiteRepos;
                else imageADessiner = (animationTick == 0) ? servDroite1 : servDroite2;
                break;
        }

        if (imageADessiner != null) {
            graphics.drawImage(imageADessiner, x - decalageX, y - decalageY, tailleImage, tailleImage, null);
        } else {
            graphics.setColor(Color.RED);
            graphics.fillOval(x + (blockSize - 20) / 2, y + (blockSize - 20) / 2, 20, 20);
        }

        graphics.setColor(new Color(0, 0, 0));
        graphics.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        graphics.drawString(serveur.getName(), x + 5, y + 5);
    }

    /**
     * Permet de dessiner de beaux contours et fonds ronds pour les étiquettes en haut de l'écran (argent, jour).
     *
     * @param label l'étiquette à décorer
     * @param graphics pinceau
     */
    public void paint(JLabel label, Graphics graphics) {
        int weight = label.getWidth();
        int height = label.getHeight();

        if (label instanceof MonnaieLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillOval(5, height / 2 - 8, 16, 16);
            g2d.setColor(new Color(184, 134, 11));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(5, height / 2 - 8, 16, 16);

        } else if (label instanceof JourLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.darkGray);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof TempsLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.darkGray);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof ReputationLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.CYAN);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);

        } else if (label instanceof PropreteLabel) {
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setColor(Color.GREEN);
            g2d.fillRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(0, height / 2 - 12, weight - 1, 24, 20, 20);
        }
    }

    /**
     * Affiche un message rouge en haut de l'écran quand on est en mode "Construire".
     *
     * @param graphics pinceau
     * @param message le texte
     * @param prix combien ça coûte (0 si ça ne s'achète pas direct)
     */
    public void paint(Graphics graphics, String message, int prix) {
        int largeur = 420;
        int hauteur = prix > 0 ? 90 : 70;
        int x = 20;
        int y = 30;
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(new Color(30, 30, 30, 220));
        g2d.fillRoundRect(x, y, largeur, hauteur, 20, 20);

        g2d.setColor(new Color(200, 50, 50));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, largeur, hauteur, 20, 20);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(200, 50, 50));
        g2d.drawString("Mode construction", x + 20, y + 28);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x + 20, y + 52);

        if (prix > 0) {
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2d.setColor(new Color(80, 200, 80));
            g2d.drawString("Cout : " + prix + " gold", x + 20, y + 75);
        }
    }

    /**
     * Affiche un gros message rouge s'il n'y a plus d'ingrédients.
     *
     * @param graphics pinceau
     * @param panelWidth largeur de l'écran
     */
    public void paint(Graphics graphics, int panelWidth) {
        String titre = "Manque d'ingredients !";
        String message = "Veuillez recharger les ingredients pour accueillir de nouveaux clients.";

        int largeur = 460;
        int hauteur = 90;
        int x = (panelWidth - largeur) / 2;
        int y = 140;

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(new Color(30, 30, 30, 220));
        g2d.fillRoundRect(x, y, largeur, hauteur, 20, 20);
        g2d.setColor(new Color(50, 120, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, largeur, hauteur, 20, 20);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(50, 120, 200));
        g2d.drawString(titre, x + 20, y + 28);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x + 20, y + 55);
    }
    /**
     * Dessine le petit encart noir "Succès débloqué !" quand on réussit un palier.
     *
     * @param s le succès en question
     * @param g pinceau
     * @param panelWidth largeur visée pour centrer
     * @param panelHeight hauteur d'ajustement
     */
    public void paint(Succes s, Graphics g, int panelWidth, int panelHeight) {
        int largeur = 300;
        int hauteur = 100;
        int x = (panelWidth - largeur) / 2;
        int y = 30;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(30, 30, 30, 220));
        g2d.fillRoundRect(x, y, largeur, hauteur, 20, 20);
        g2d.setColor(new Color(200, 160, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, largeur, hauteur, 20, 20);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.setColor(new Color(200, 160, 0));
        g2d.drawString("Succes debloque !", x + 20, y + 28);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);
        g2d.drawString(s.getNom(), x + 20, y + 55);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        g2d.setColor(new Color(200, 160, 0));
        g2d.drawString("+" + s.getRecompense() + " gold", x + 20, y + 80);
    }

    /**
     * Dessine un texte flottant (ex: "+150 G") et une petite pièce d'or à côté.
     * Le texte se déplace vers le haut à chaque tour grâce à la mise à jour de ses coordonnées y.
     *
     * @param ft L'objet FloatingText contenant le texte et les coordonnées
     * @param graphics L'outil de dessin de l'interface
     */
    public void paint(FloatingText ft, Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 15));
        String msg = "+" + ft.getText();

        g2d.setColor(Color.BLACK);
        g2d.drawString(msg, ft.getX() + 1, ft.getY() + 1);
        g2d.setColor(Color.WHITE);
        g2d.drawString(msg, ft.getX(), ft.getY());

        int textWidth = g2d.getFontMetrics().stringWidth(msg);
        int coinX = ft.getX() + textWidth + 3;
        int coinY = ft.getY() - 12;

        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(coinX, coinY, 14, 14);
        g2d.setColor(new Color(184, 134, 11));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(coinX, coinY, 14, 14);
    }

    /**
     * Dessine les arbres
     * @param position la position des arbres sur la carte
     * @param graphics pinceau
     * @param animationTick ce qui nous permet de savoir quel sprite afficher
     */
    public void paint(Block position, Graphics graphics, int animationTick) {
        Image imageArbre = (animationTick == 0) ? arbre1 : arbre2;

        if (imageArbre != null) {
            int blockSize = BLOCK_SIZE;
            int x = position.getColumn() * blockSize;
            int y = position.getLine() * blockSize;

            graphics.drawImage(imageArbre, x - 20, y - 40, blockSize + 40, blockSize + 50, null);
        }
    }
}