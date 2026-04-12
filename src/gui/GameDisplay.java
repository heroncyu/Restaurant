package gui;

import engine.map.Map;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import engine.process.RestaurantManager;
import engine.process.Simulation;

import javax.swing.*;
import java.awt.*;

public class GameDisplay extends JPanel {
    private Map map;
    private Simulation simulation;
    private PaintStrategy paintStrategy = new PaintStrategy();
    private int animationTick;
    private Succes succesEnCours = null;
    private int tempsRestants = 0;

    public GameDisplay(Map map, Simulation simulation) {
        this.map = map;
        this.simulation = simulation;
        this.animationTick = 0;
    }
    public void afficherSucces(Succes s) {
        succesEnCours = s;
        tempsRestants = 30;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        animationTick = (animationTick + 1) % 2;

        paintStrategy.paint(map, simulation.getZones(), g);

        RestaurantManager restaurantManager = simulation.getRestaurantManager();

        for (Meuble meuble : simulation.getMeubles()) {
            paintStrategy.paint(meuble, g);
        }

        for (Client client : simulation.getManager().getClients()) {
            paintStrategy.paint(client, g);
        }

        for (Cuisinier cuisinier : simulation.getManager().getCuisiniers()) {
            paintStrategy.paint(cuisinier, g);
        }

        for (Serveur serveur : simulation.getManager().getServeurs()) {
            paintStrategy.paint(serveur, g,animationTick);
        }

        if (restaurantManager.getConstructionMode() == 1) {
            paintStrategy.paint(g, "Mode construction : Agrandissement du terrain", restaurantManager.calculerPrixConstruction());
        }
        if (succesEnCours != null) {
            paintStrategy.paint(succesEnCours, g, getWidth(), getHeight());
            tempsRestants--;
            if (tempsRestants <= 0) {
                succesEnCours = null;
            }
        }
        
        if (restaurantManager.getConstructionMode() == 2) {
            paintStrategy.paint(g, "Mode construction : Placez votre nouveau meuble", 0);
            paintStrategy.paint(simulation.getBlocksOccupees(), g);
        }

        if(simulation.isAlerteStock()){
            paintStrategy.paint(g,getWidth());
        }

    }

}
