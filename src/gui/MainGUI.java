package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import config.GameConfiguration;
import engine.process.ArgentRepository;
import engine.process.Simulation;
import gui.info.InfoDisplay;
import gui.menu.MenuDisplay;

public class MainGUI extends JFrame implements Runnable {
    private Simulation simulation;
    private GameDisplay dashboard;
    private InfoDisplay infoDisplay;
    private MenuDisplay buttonPanel;

    public MainGUI() {
        super("Restaurant");
        init();
    }

    private void init() {

        simulation = new Simulation();
        dashboard = new GameDisplay(simulation.getMap(), simulation);
        infoDisplay = new InfoDisplay(simulation);
        buttonPanel = new MenuDisplay(this, simulation);

        dashboard.addMouseListener(new CliqueGauche());

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(dashboard, BorderLayout.CENTER);
        contentPane.add(infoDisplay, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.EAST);

        setSize(GameConfiguration.WINDOW_WIDTH,
                GameConfiguration.WINDOW_HEIGHT);
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

                if (simulation.isConstructionModeActive()) {
                    simulation.agrandirZone(ligne, colonne);
                }

            }
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(GameConfiguration.GAME_SPEED / simulation.getSpeedMultiplier());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            simulation.nextRound();
            if (simulation.checkFinJournee()) {
                ArgentRepository.getInstance().nouveauJour();
                new BilanWindow(this, simulation.getDayStatistics());
                simulation.getDayStatistics().update();
                simulation.setStop(false);
            }
            dashboard.repaint();
            infoDisplay.repaint();
        }
    }
}