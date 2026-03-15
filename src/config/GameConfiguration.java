package config;

public class GameConfiguration {
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;
    public static final int BLOCK_SIZE = 80;

    public static final int LINE_COUNT = WINDOW_HEIGHT / BLOCK_SIZE;
    public static final int COLUMN_COUNT = WINDOW_WIDTH / BLOCK_SIZE;

    public static final int GAME_SPEED = 250;

    public static final int INITIAL_MONEY = 1000;

    public static final int END_OF_DAY_HOUR = 22;
}