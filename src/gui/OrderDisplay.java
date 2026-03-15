package gui;

import engine.item.Commande;
import engine.process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class OrderDisplay extends JPanel {
    private static Font labelTitreFont = new Font("Comis Sans MS", Font.BOLD, 24);
    private static Font labelCommandeFont = new Font("Comis Sans MS", Font.PLAIN, 16);

    private JLabel labelEnAttente;
    private JLabel labelACuisiner;
    private JLabel labelCuisson;
    private JLabel labelPretes;

    private JPanel panelCommandesEnAttente;
    private JPanel panelCommandesACuisiner;
    private JPanel panelCommandesCuisson;
    private JPanel panelCommandesPretes;

    private Simulation simulation;

    public OrderDisplay(Simulation simulation) {
        this.simulation = simulation;
        setBackground(Color.gray);

        setLayout(new GridLayout(1, 4));

        initPanels();
        updateAll();
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

        labelEnAttente = new JLabel("Commandes en attente : 0");
        labelACuisiner = new JLabel("Commandes à cuisiner : 0");
        labelCuisson = new JLabel("Commandes en cuisson : 0");
        labelPretes = new JLabel("Commandes prêtes : 0");

        labelEnAttente.setFont(labelTitreFont);
        labelACuisiner.setFont(labelTitreFont);
        labelCuisson.setFont(labelTitreFont);
        labelPretes.setFont(labelTitreFont);

        add(panelCommandesEnAttente);
        add(panelCommandesACuisiner);
        add(panelCommandesCuisson);
        add(panelCommandesPretes);
    
    }

    public void updateCommandesEnAttente() {
        ArrayList<Commande> commandesEnAttente = simulation.getCommandesEnAttente();
        System.out.println("Commandes en attente : " + commandesEnAttente.size());
        panelCommandesEnAttente.removeAll();
        panelCommandesEnAttente.add(labelEnAttente, BorderLayout.CENTER);
        for (Commande commande : commandesEnAttente) {
            JLabel labelCommande = new JLabel();
            labelCommande.setText(commande.getPlat().getRecette().getNom());
            labelCommande.setFont(labelCommandeFont);
            panelCommandesEnAttente.add(labelCommande);
        }
    }

    public void updateCommandesACuisiner() {
        ArrayList<Commande> commandesACuisiner = simulation.getCommandesACuisiner();
        panelCommandesACuisiner.removeAll();
        panelCommandesACuisiner.add(labelACuisiner, BorderLayout.CENTER);
        for (Commande commande : commandesACuisiner) {
            JLabel labelCommande = new JLabel();
            labelCommande.setText(commande.getPlat().getRecette().getNom());
            labelCommande.setFont(labelCommandeFont);
            panelCommandesACuisiner.add(labelCommande);
        }
    }

    public void updateCommandesCuisson() {
        ArrayList<Commande> commandesCuisson = simulation.getCommandesCuisson();
        panelCommandesCuisson.removeAll();
        panelCommandesCuisson.add(labelCuisson, BorderLayout.CENTER);
        for (Commande commande : commandesCuisson) {
            JLabel labelCommande = new JLabel();
            labelCommande.setText(commande.getPlat().getRecette().getNom());
            labelCommande.setFont(labelCommandeFont);
            panelCommandesCuisson.add(labelCommande);
        }
    }

    public void updateCommandesPretes() {
        ArrayList<Commande> commandesPretes = simulation.getCommandesPretes();
        panelCommandesPretes.removeAll();
        panelCommandesPretes.add(labelPretes, BorderLayout.CENTER);
        for (Commande commande : commandesPretes) {
            JLabel labelCommande = new JLabel();
            labelCommande.setText(commande.getPlat().getRecette().getNom());
            labelCommande.setFont(labelCommandeFont);
            panelCommandesPretes.add(labelCommande);
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
