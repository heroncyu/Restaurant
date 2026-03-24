package config;

public class GameConfiguration {
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;
    public static final int BLOCK_SIZE = 40;

    public static final int INFO_PANEL_HEIGHT   = 50;
    public static final int ORDERS_PANEL_HEIGHT = 69;
    public static final int MENU_PANEL_WIDTH    = 119;

    public static final int GAME_WIDTH  = WINDOW_WIDTH - MENU_PANEL_WIDTH ;
    public static final int GAME_HEIGHT = WINDOW_HEIGHT - INFO_PANEL_HEIGHT - ORDERS_PANEL_HEIGHT;

    public static final int LINE_COUNT = GAME_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = GAME_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 50;

    public static final int INITIAL_MONEY = 1000;

    public static final int END_OF_DAY_HOUR = 22;
    public static final int INITIAL_REPUTATION = 50;
    public static final int LOYER_PAR_CASE = 1;

    public static final int PRIX_SERVEUR = 200;
    public static final int PRIX_CUISINIER = 300;
    public static final int SALAIRE_SERVEUR_BASE = 50;
    public static final int SALAIRE_CUISINIER_BASE = 100;
    public static final int PRIX_AMELIORATION = 50;
}