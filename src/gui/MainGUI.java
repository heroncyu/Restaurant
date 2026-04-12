package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import engine.prestige.Succes;
import engine.process.RestaurantManager;
import engine.process.SuccesRepository;
import org.apache.log4j.Logger;

import config.GameConfiguration;
import engine.map.Block;
import engine.mobile.Meuble;
import engine.process.ArgentRepository;
import engine.process.Simulation;
import gui.info.InfoDisplay;
import gui.menu.MenuDisplay;
import log.LoggerUtility;

public class MainGUI extends JFrame implements Runnable {
    private static Logger logger = LoggerUtility.getLogger(MainGUI.class, "html");
    
    private Simulation simulation;
    private GameDisplay dashboard;
    private InfoDisplay infoDisplay;
    private OrderDisplay orderDisplay;
    private MenuDisplay buttonPanel;

    private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

    public MainGUI() {
        super("Restaurant");

        Rectangle usable = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();


        GameConfiguration.init(usable.width, usable.height);

        init();
    }

    private void init() {
        simulation = new Simulation();
        dashboard = new GameDisplay(simulation.getMap(), simulation);
        infoDisplay = new InfoDisplay(this,simulation);
        buttonPanel = new MenuDisplay(this, simulation,simulation.getDayStatistics());
        orderDisplay = new OrderDisplay(simulation);

        infoDisplay.setPreferredSize(new Dimension(
                GameConfiguration.WINDOW_WIDTH,
                GameConfiguration.INFO_PANEL_HEIGHT
        ));

        orderDisplay.setPreferredSize(new Dimension(
                GameConfiguration.WINDOW_WIDTH,
                GameConfiguration.ORDERS_PANEL_HEIGHT
        ));

        buttonPanel.setPreferredSize(new Dimension(
                GameConfiguration.MENU_PANEL_WIDTH,
                GameConfiguration.GAME_HEIGHT
        ));

        dashboard.setPreferredSize(new Dimension(
                GameConfiguration.GAME_WIDTH,
                GameConfiguration.GAME_HEIGHT
        ));

        dashboard.addMouseListener(new CliqueGauche());

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(dashboard, BorderLayout.CENTER);
        contentPane.add(infoDisplay, BorderLayout.NORTH);
        contentPane.add(orderDisplay, BorderLayout.SOUTH);
        contentPane.add(buttonPanel, BorderLayout.EAST);

        pack();
        setPreferredSize(preferredSize);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }

    private class CliqueGauche extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int x = e.getX();
                int y = e.getY();

                int ligne = y / GameConfiguration.BLOCK_SIZE;
                int colonne = x / GameConfiguration.BLOCK_SIZE;

                RestaurantManager restaurantManager = simulation.getRestaurantManager();
                if (restaurantManager.getConstructionMode() == 1) {
                    restaurantManager.agrandirZone(ligne, colonne);
                } else if (restaurantManager.getConstructionMode() == 2) {
                    restaurantManager.ajouterMeuble(ligne, colonne);
                    restaurantManager.setConstructionMode(0);
                }

            }
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(GameConfiguration.GAME_SPEED / simulation.getSpeedMultiplier());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            simulation.nextRound();
            if (simulation.checkFinJournee()) {
                ArgentRepository.getInstance().nouveauJour();
                if (ArgentRepository.getInstance().getMonnaie() < 0) {
                    new GameOverWindow(this, simulation.getGameStats());
                } else {
                    new BilanWindow(this, simulation.getDayStatistics());
                    simulation.getDayStatistics().update();
                    simulation.setStop(false);
                }
            }
            Succes s = SuccesRepository.getInstance().Notification();
            if (s != null) {
                dashboard.afficherSucces(s);
            }
            dashboard.repaint();
            infoDisplay.repaint();
            orderDisplay.repaint();
        }
    }
}