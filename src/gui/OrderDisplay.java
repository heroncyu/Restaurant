package gui;

import engine.item.Commande;
import engine.process.Simulation;
import engine.process.SimulationUtility;
import gui.util.FondPanel;
import log.LoggerUtility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class OrderDisplay extends FondPanel {
    private static Logger logger = LoggerUtility.getLogger(OrderDisplay.class, "html");

    private static final int MAX_COMMANDES = 10;

    private static Font labelTitreFont = new Font("Comis Sans MS", Font.BOLD, 18);
    private static Font labelCommandeFont = new Font("Comis Sans MS", Font.PLAIN, 16);

    private JLabel labelEnAttente;
    private JLabel labelACuisiner;
    private JLabel labelCuisson;
    private JLabel labelPretes;

    private JPanel panelCommandesEnAttente;
    private JPanel panelCommandesACuisiner;
    private JPanel panelCommandesCuisson;
    private JPanel panelCommandesPretes;

    private JLabel[] labelsEnAttente = new JLabel[MAX_COMMANDES];
    private JLabel[] labelsACuisiner = new JLabel[MAX_COMMANDES];
    private JLabel[] labelsCuisson   = new JLabel[MAX_COMMANDES];
    private JLabel[] labelsPretes    = new JLabel[MAX_COMMANDES];

    private Simulation simulation;

    public OrderDisplay(Simulation simulation) {
        super(SimulationUtility.lireImage("src/resources/fond_bois_moyen.png"));
        this.simulation = simulation;

        setLayout(new GridLayout(1, 4, 1, 0));

        initPanels();
    }

    private void initPanels() {
        panelCommandesEnAttente = new JPanel();
        panelCommandesACuisiner = new JPanel();
        panelCommandesCuisson = new JPanel();
        panelCommandesPretes = new JPanel();

        panelCommandesEnAttente.setLayout(new BoxLayout(panelCommandesEnAttente, BoxLayout.Y_AXIS));
        panelCommandesACuisiner.setLayout(new BoxLayout(panelCommandesACuisiner, BoxLayout.Y_AXIS));
        panelCommandesCuisson.setLayout(new BoxLayout(panelCommandesCuisson, BoxLayout.Y_AXIS));
        panelCommandesPretes.setLayout(new BoxLayout(panelCommandesPretes, BoxLayout.Y_AXIS));

        panelCommandesEnAttente.setOpaque(false);
        panelCommandesACuisiner.setOpaque(false);
        panelCommandesCuisson.setOpaque(false);
        panelCommandesPretes.setOpaque(false);

        labelEnAttente = new JLabel("Commandes en attente : 0");
        labelACuisiner = new JLabel("Commandes à cuisiner : 0");
        labelCuisson = new JLabel("Commandes en cuisson : 0");
        labelPretes = new JLabel("Commandes prêtes : 0");

        labelEnAttente.setFont(labelTitreFont);
        labelACuisiner.setFont(labelTitreFont);
        labelCuisson.setFont(labelTitreFont);
        labelPretes.setFont(labelTitreFont);

        labelEnAttente.setAlignmentX(CENTER_ALIGNMENT);
        labelACuisiner.setAlignmentX(CENTER_ALIGNMENT);
        labelCuisson.setAlignmentX(CENTER_ALIGNMENT);
        labelPretes.setAlignmentX(CENTER_ALIGNMENT);

        panelCommandesEnAttente.add(labelEnAttente);
        panelCommandesACuisiner.add(labelACuisiner);
        panelCommandesCuisson.add(labelCuisson);
        panelCommandesPretes.add(labelPretes);

        for (int i = 0; i < MAX_COMMANDES; i++) {
            labelsEnAttente[i] = new JLabel("");
            labelsEnAttente[i].setFont(labelCommandeFont);
            labelsEnAttente[i].setAlignmentX(CENTER_ALIGNMENT);
            panelCommandesEnAttente.add(labelsEnAttente[i]);

            labelsACuisiner[i] = new JLabel("");
            labelsACuisiner[i].setFont(labelCommandeFont);
            labelsACuisiner[i].setAlignmentX(CENTER_ALIGNMENT);
            panelCommandesACuisiner.add(labelsACuisiner[i]);

            labelsCuisson[i] = new JLabel("");
            labelsCuisson[i].setFont(labelCommandeFont);
            labelsCuisson[i].setAlignmentX(CENTER_ALIGNMENT);
            panelCommandesCuisson.add(labelsCuisson[i]);

            labelsPretes[i] = new JLabel("");
            labelsPretes[i].setFont(labelCommandeFont);
            labelsPretes[i].setAlignmentX(CENTER_ALIGNMENT);
            panelCommandesPretes.add(labelsPretes[i]);
        }

        JScrollPane scrollPaneEnAttente = new JScrollPane(panelCommandesEnAttente);
        JScrollPane scrollPaneACuisiner = new JScrollPane(panelCommandesACuisiner);
        JScrollPane scrollPaneCuisson = new JScrollPane(panelCommandesCuisson);
        JScrollPane scrollPanePretes = new JScrollPane(panelCommandesPretes);

        scrollPaneEnAttente.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPaneACuisiner.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPaneCuisson.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPanePretes.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        scrollPaneEnAttente.setOpaque(false);
        scrollPaneEnAttente.getViewport().setOpaque(false);
        scrollPaneEnAttente.setBorder(null);

        scrollPaneACuisiner.setOpaque(false);
        scrollPaneACuisiner.getViewport().setOpaque(false);
        scrollPaneACuisiner.setBorder(null);

        scrollPaneCuisson.setOpaque(false);
        scrollPaneCuisson.getViewport().setOpaque(false);
        scrollPaneCuisson.setBorder(null);

        scrollPanePretes.setOpaque(false);
        scrollPanePretes.getViewport().setOpaque(false);
        scrollPanePretes.setBorder(null);

        add(scrollPaneEnAttente);
        add(scrollPaneACuisiner);
        add(scrollPaneCuisson);
        add(scrollPanePretes);

    }

    public void updateCommandesEnAttente() {
        ArrayList<Commande> commandesEnAttente = simulation.getCommandesEnAttente();

        for (int i = 0; i < MAX_COMMANDES; i++) {
            if (i < commandesEnAttente.size()) {
                Commande commande = commandesEnAttente.get(i);
                labelsEnAttente[i].setText(commande.getNomRecette());
            } else {
                labelsEnAttente[i].setText("");
            }
        }
    }

    public void updateCommandesACuisiner() {
        ArrayList<Commande> commandesACuisiner = simulation.getCommandesACuisiner();

        for (int i = 0; i < MAX_COMMANDES; i++) {
            if (i < commandesACuisiner.size()) {
                Commande commande = commandesACuisiner.get(i);
                labelsACuisiner[i].setText(commande.getNomRecette());
            } else {
                labelsACuisiner[i].setText("");
            }
        }
    }

    public void updateCommandesCuisson() {
        ArrayList<Commande> commandesCuisson = simulation.getCommandesCuisson();

        for (int i = 0; i < MAX_COMMANDES; i++) {
            if (i < commandesCuisson.size()) {
                Commande commande = commandesCuisson.get(i);
                labelsCuisson[i].setText(commande.getNomRecette());
            } else {
                labelsCuisson[i].setText("");
            }
        }
    }

    public void updateCommandesPretes() {
        ArrayList<Commande> commandesPretes = simulation.getCommandesPretes();

        for (int i = 0; i < MAX_COMMANDES; i++) {
            if (i < commandesPretes.size()) {
                Commande commande = commandesPretes.get(i);
                labelsPretes[i].setText(commande.getNomRecette());
            } else {
                labelsPretes[i].setText("");
            }
        }
    }

    public void updateLabelsTitres() {
        labelEnAttente.setText("Commandes en attente : " + simulation.getCommandesEnAttente().size());
        labelACuisiner.setText("Commandes à cuisiner : " + simulation.getCommandesACuisiner().size());
        labelCuisson.setText("Commandes en cuisson : " + simulation.getCommandesCuisson().size());
        labelPretes.setText("Commandes prêtes : " + simulation.getCommandesPretes().size());
    }

    public void updateAll() {
        updateLabelsTitres();
        updateCommandesEnAttente();
        updateCommandesACuisiner();
        updateCommandesCuisson();
        updateCommandesPretes();
    }

    @Override
    public void paintComponent(Graphics g) {
        updateAll();
        super.paintComponent(g);
    }
}
