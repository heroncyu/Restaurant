package gui;

import engine.map.Map;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
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

        for (Meuble meuble : simulation.getMeubles()) {
            paintStrategy.paint(meuble, g);
        }

        for (Client client : simulation.getClients()) {
            paintStrategy.paint(client, g);
        }

        for (Cuisinier cuisinier : simulation.getCuisiniers()) {
            paintStrategy.paint(cuisinier, g);
        }

        for (Serveur serveur : simulation.getServeurs()) {
            paintStrategy.paint(serveur, g);
        }

        if (simulation.isConstructionModeActive()) {
            paintStrategy.paint(g);
        }

    }

}
