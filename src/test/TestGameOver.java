package test;

import java.util.HashMap;

import gui.GameOverWindow;

public class TestGameOver {
    public static void main(String[] args) {
        HashMap<String, Integer> gameStats = new HashMap<>();
        gameStats.put("depenses", 542);
        gameStats.put("revenus", 678);
        gameStats.put("reputation", 100);
        gameStats.put("nbMeubles", 45);
        gameStats.put("nbServeurs", 2);
        gameStats.put("nbCuisiniers", 1);
        gameStats.put("nbCommandes", 120);
        gameStats.put("achats", 10);
        gameStats.put("loyers", 5);
        gameStats.put("salaires", 100);
        gameStats.put("construction", 20);
        gameStats.put("pourboires", 50);
        gameStats.put("jour", 1);
        new GameOverWindow(null, gameStats);
    }
}
