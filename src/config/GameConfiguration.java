package config;

import java.awt.*;

public class GameConfiguration {
    public static final int INFO_PANEL_HEIGHT   = 50;
    public static final int ORDERS_PANEL_HEIGHT = 69;
    public static final int MENU_PANEL_WIDTH    = 119;

    public static int BLOCK_SIZE = 40;
    public static int GAME_WIDTH;
    public static int GAME_HEIGHT;
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    public static final int LINE_COUNT = 24;
    public static final int COLUMN_COUNT = 45;

    public static final int GAME_SPEED = 250;

    public static final int INITIAL_MONEY = 1550; //argent de départ - 550 des premiers meubles

    public static final int END_OF_DAY_HOUR = 22;
    public static final int INITIAL_REPUTATION = 50;
    public static final int LOYER_PAR_CASE = 1;

    public static final int PRIX_SERVEUR = 200;
    public static final int PRIX_CUISINIER = 300;
    public static final int SALAIRE_SERVEUR_BASE = 50;
    public static final int SALAIRE_CUISINIER_BASE = 100;
    public static final int PRIX_AMELIORATION = 50;

    public static final int PRIX_TABLE = 100;
    public static final int PRIX_FOUR = 200;
    public static final int PRIX_PLANTE = 50;
    public static final int PRIX_PORTE_MANTEAU = 75;
    public static final int CAPACITE_PAR_CASE = 15;

    public static final String ETAT_LIBRE = "LIBRE";

    public static final String ETAT_VA_PRENDRE = "VA_PRENDRE";
    public static final String ETAT_VA_DEPOSER = "VA_DEPOSER";
    public static final String ETAT_VA_CHERCHER = "VA_CHERCHER";
    public static final String ETAT_VA_SERVIR = "VA_SERVIR";

    public static final String ETAT_VA_CHERCHER_COMMANDE = "VA_CHERCHER_COMMANDE";
    public static final String ETAT_VA_CUISINER = "VA_CUISINER";
    public static final String ETAT_CUISINE = "CUISINE";

    public static final String HAUT = "HAUT";
    public static final String BAS = "BAS";
    public static final String GAUCHE = "GAUCHE";
    public static final String DROITE = "DROITE";

    public static void init(int screenWidth, int screenHeight) {

        int maxGameWidth = screenWidth - MENU_PANEL_WIDTH;
        int maxGameHeight = screenHeight - INFO_PANEL_HEIGHT - ORDERS_PANEL_HEIGHT;


        int blockW = maxGameWidth / COLUMN_COUNT;
        int blockH = maxGameHeight / LINE_COUNT;

        BLOCK_SIZE = Math.min(blockW, blockH);


        BLOCK_SIZE = Math.min(BLOCK_SIZE, 40);


        GAME_WIDTH  = BLOCK_SIZE * COLUMN_COUNT;
        GAME_HEIGHT = BLOCK_SIZE * LINE_COUNT;
        WINDOW_WIDTH = GAME_WIDTH + MENU_PANEL_WIDTH;
        WINDOW_HEIGHT = GAME_HEIGHT + INFO_PANEL_HEIGHT + ORDERS_PANEL_HEIGHT;
    }
}