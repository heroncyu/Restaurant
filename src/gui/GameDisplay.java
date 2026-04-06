package gui;

import engine.map.Map;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.process.RestaurantManager;
import engine.process.Simulation;

import javax.swing.*;
import java.awt.*;

public class GameDisplay extends JPanel {
    private Map map;
    private Simulation simulation;
    private PaintStrategy paintStrategy = new PaintStrategy();

    public GameDisplay(Map map, Simulation simulation) {
        this.map = map;
        this.simulation = simulation;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

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
            paintStrategy.paint(serveur, g);
        }

        if (restaurantManager.getConstructionMode() == 1) {
            paintStrategy.paint(g, "Mode construction : Agrandissement du terrain", restaurantManager.calculerPrixConstruction());
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
