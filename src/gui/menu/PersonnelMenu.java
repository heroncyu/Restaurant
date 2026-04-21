package gui.menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import engine.mobile.Serveur;
import engine.mobile.Cuisinier;
import engine.process.RestaurantManager;
import engine.process.Simulation;
import engine.process.SimulationUtility;

/**
 * Menu pour embaucher ou améliorer ses cuisiniers et serveurs.
 * 
 * Il bloque l'achat si on n'a plus de place (pas assez de fours ou de tables).
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class PersonnelMenu extends JDialog {
    private Simulation simulation;
    private JPanel contenu;
    private Font font = new Font("Segoe UI", Font.BOLD, 20);
    private RestaurantManager restaurantManager;

    /**
     * Ouvre le menu complet du personnel.
     * 
     * @param owner la fenêtre principale du jeu
     * @param simulation le jeu pour vérifier les chaises et l'argent
     */
    public PersonnelMenu(JFrame owner, Simulation simulation) {
        super(owner, "Personnel", true);
        this.simulation = simulation;
        this.restaurantManager = simulation.getRestaurantManager();

        setSize(820, 520);
        setLocationRelativeTo(owner);

        contenu = new JPanel();
        contenu.setLayout(new GridLayout(0, 1, 0, 10));
        contenu.setBackground(Color.gray);
        contenu.setBorder(new EmptyBorder(20, 20, 20, 20));

        init();

        JScrollPane scroll = new JScrollPane(contenu);
        add(scroll);
        setVisible(true);
    }

    private void init() {
        JLabel titreServeurs = new JLabel("Serveurs");
        titreServeurs.setFont(font);
        titreServeurs.setHorizontalAlignment(JLabel.CENTER);
        contenu.add(titreServeurs);

        for (Serveur serveur : simulation.getManager().getServeurs()) {
            contenu.add(creerLigneServeur(serveur));
        }

        contenu.add(creerBoutonAchatServeur());

        JLabel titreCuisiniers = new JLabel("Cuisiniers");
        titreCuisiniers.setFont(font);
        titreCuisiniers.setHorizontalAlignment(JLabel.CENTER);
        contenu.add(titreCuisiniers);

        for (Cuisinier cuisinier : simulation.getManager().getCuisiniers()) {
            contenu.add(creerLigneCuisinier(cuisinier));
        }

        contenu.add(creerBoutonAchatCuisinier());

        int nbFours = SimulationUtility.getNombreFours(simulation.getMeubles());
        int nbTables = SimulationUtility.getNombreTables(simulation.getMeubles());
        JLabel infoLabel = new JLabel("Fours: " + nbFours + " | Tables: " + nbTables + " | 1 cuisinier/four, 1 serveur/2 tables");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        contenu.add(infoLabel);

        contenu.revalidate();
        contenu.repaint();
    }

    private JPanel creerLigneServeur(Serveur serveur) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        ligne.setBorder(BorderFactory.createLineBorder(Color.black));

        String effet = "Pourboire x" + (1.0 + (serveur.getNiveau() - 1) * 0.5);
        JLabel info = new JLabel(serveur.getName() + " | Niv: " + serveur.getNiveau() + "/5 | Salaire: " + serveur.getSalaireBase() + "G | " + effet);
        info.setFont(font);

        JButton ameliorer = creerBoutonStyle(serveur.getNiveau() >= 5 ? "MAX" : "Ameliorer (50G)");
        ameliorer.setEnabled(serveur.getNiveau() < 5);
        ameliorer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (restaurantManager.ameliorerServeur(serveur)) {
                    refresh();
                }
            }
        });

        ligne.add(info);
        ligne.add(ameliorer);
        return ligne;
    }

    private JPanel creerLigneCuisinier(Cuisinier cuisinier) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        ligne.setBorder(BorderFactory.createLineBorder(Color.black));

        String effet = "Qualite: " + SimulationUtility.calculerQualite(cuisinier) + "%";
        JLabel info = new JLabel(cuisinier.getName() + " | Niv: " + cuisinier.getNiveau() + "/5 | Salaire: " + cuisinier.getSalaireBase() + "G | " + effet);
        info.setFont(font);

        JButton ameliorer = creerBoutonStyle(cuisinier.getNiveau() >= 5 ? "MAX" : "Ameliorer (50G)");
        ameliorer.setEnabled(cuisinier.getNiveau() < 5);
        ameliorer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (restaurantManager.ameliorerCuisinier(cuisinier)) {
                    refresh();
                }
            }
        });

        ligne.add(info);
        ligne.add(ameliorer);
        return ligne;
    }

    private JPanel creerBoutonAchatServeur() {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        ligne.setBorder(BorderFactory.createLineBorder(Color.black));

        boolean peut = SimulationUtility.peutAcheterServeur(
                simulation.getMeubles(),
                simulation.getManager().getServeurs().size());

        JButton acheter = creerBoutonStyle(peut ? "Recruter un serveur (200G)" : "Recruter serveur (pas assez de tables)");
        acheter.setEnabled(peut);
        acheter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (restaurantManager.acheterServeur()) {
                    refresh();
                }
            }
        });

        ligne.add(acheter);
        return ligne;
    }

    private JPanel creerBoutonAchatCuisinier() {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        ligne.setBorder(BorderFactory.createLineBorder(Color.black));

        boolean peut = SimulationUtility.peutAcheterCuisinier(
                simulation.getMeubles(),
                simulation.getManager().getCuisiniers().size());

        JButton acheter = creerBoutonStyle(peut ? "Recruter un cuisinier (300G)" : "Recruter cuisinier (pas assez de fours)");
        acheter.setEnabled(peut);
        acheter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (restaurantManager.acheterCuisinier()) {
                    refresh();
                }
            }
        });

        ligne.add(acheter);
        return ligne;
    }

    private void refresh() {
        contenu.removeAll();
        init();
        repaint();
    }

    private JButton creerBoutonStyle(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setFont(font);
        bouton.setBackground(Color.green);
        bouton.setFocusPainted(false);
        bouton.setContentAreaFilled(false);
        bouton.setOpaque(true);
        return bouton;
    }
}