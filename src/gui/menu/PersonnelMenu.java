package gui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import engine.mobile.Serveur;
import engine.mobile.Cuisinier;
import engine.process.Simulation;
import engine.process.SimulationUtility;

public class PersonnelMenu extends JDialog {
    private Simulation simulation;
    private JPanel contenu;

    public PersonnelMenu(JFrame owner, Simulation simulation) {
        super(owner, "Personnel", true);
        this.simulation = simulation;
        setSize(600, 500);
        setLocationRelativeTo(owner);

        contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));

        init();

        JScrollPane scroll = new JScrollPane(contenu);
        add(scroll);
        setVisible(true);
    }

    private void init() {

        JLabel titreServeurs = new JLabel("-- Serveurs --");
        titreServeurs.setFont(new Font("SansSerif", Font.BOLD, 18));
        contenu.add(titreServeurs);

        for (Serveur serveur : simulation.getManager().getServeurs()) {
            contenu.add(creerLigneServeur(serveur));
        }

        contenu.add(creerBoutonAchatServeur());

        JLabel titreCuisiniers = new JLabel("-- Cuisiniers --");
        titreCuisiniers.setFont(new Font("SansSerif", Font.BOLD, 18));
        contenu.add(titreCuisiniers);

        for (Cuisinier cuisinier : simulation.getManager().getCuisiniers()) {
            contenu.add(creerLigneCuisinier(cuisinier));
        }

        contenu.add(creerBoutonAchatCuisinier());

        int nbFours = SimulationUtility.getNombreFours(simulation.getMeubles());
        int nbTables = SimulationUtility.getNombreTables(simulation.getMeubles());
        JLabel infoLabel = new JLabel("Fours: " + nbFours
                + " | Tables: " + nbTables
                + " | 1 cuisinier par four, 1 serveur pour 2 tables");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        contenu.add(infoLabel);

        contenu.revalidate();
        contenu.repaint();
    }

    private JPanel creerLigneServeur(Serveur serveur) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String effet = "Pourboire x" + (1.0 + (serveur.getNiveau() - 1) * 0.5);
        JLabel info = new JLabel(serveur.getName()
                + " | Niv: " + serveur.getNiveau() + "/5"
                + " | Salaire: " + serveur.getSalaireBase() + "G"
                + " | " + effet);
        info.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton ameliorer = new JButton("Ameliorer (50G)");

        if (serveur.getNiveau() >= 5) {
            ameliorer.setEnabled(false);
            ameliorer.setText("MAX");
        }

        ameliorer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation.ameliorerServeur(serveur)) {
                    repaint();
                }
            }
        });

        ligne.add(info);
        ligne.add(ameliorer);
        return ligne;
    }

    private JPanel creerLigneCuisinier(Cuisinier cuisinier) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));

        String effet = "Qualite: " + SimulationUtility.calculerQualite(cuisinier) + "%";
        JLabel info = new JLabel(cuisinier.getName()
                + " | Niv: " + cuisinier.getNiveau() + "/5"
                + " | Salaire: " + cuisinier.getSalaireBase() + "G"
                + " | " + effet);
        info.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton ameliorer = new JButton("Ameliorer (50G)");

        if (cuisinier.getNiveau() >= 5) {
            ameliorer.setEnabled(false);
            ameliorer.setText("MAX");
        }

        ameliorer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation.ameliorerCuisinier(cuisinier)) {
                    repaint();
                }
            }
        });

        ligne.add(info);
        ligne.add(ameliorer);
        return ligne;
    }

    private JPanel creerBoutonAchatServeur() {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));

        boolean peut = SimulationUtility.peutAcheterServeur(
                simulation.getMeubles(),
                simulation.getManager().getServeurs().size());

        JButton acheter = new JButton("Recruter un serveur (200G)");
        acheter.setFont(new Font("SansSerif", Font.BOLD, 14));

        if (!peut) {
            acheter.setEnabled(false);
            acheter.setText("Recruter serveur (pas assez de tables)");
        }

        acheter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation.acheterServeur()) {
                    repaint();
                }
            }
        });

        ligne.add(acheter);
        return ligne;
    }

    private JPanel creerBoutonAchatCuisinier() {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));

        boolean peut = SimulationUtility.peutAcheterCuisinier(
                simulation.getMeubles(),
                simulation.getManager().getCuisiniers().size());

        JButton acheter = new JButton("Recruter un cuisinier (300G)");
        acheter.setFont(new Font("SansSerif", Font.BOLD, 14));

        if (!peut) {
            acheter.setEnabled(false);
            acheter.setText("Recruter cuisinier (pas assez de fours)");
        }

        acheter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (simulation.acheterCuisinier()) {
                    repaint();
                }
            }
        });

        ligne.add(acheter);
        return ligne;
    }
}