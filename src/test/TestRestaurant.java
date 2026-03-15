package test;

import gui.MainGUI;

public class TestRestaurant {
    public static void main(String[] args) {
        MainGUI GUI = new MainGUI();
        Thread gameThread = new Thread(GUI);
        gameThread.start();
    }
}