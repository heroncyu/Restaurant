package gui.menu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import engine.item.Ingredient;
import engine.item.Recette;
import engine.process.Simulation;

public class RecettesMenu extends JDialog {

    public RecettesMenu(JFrame owner, Simulation simulation) {
        super(owner, "Menu des Recettes", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        init(panel, simulation);

        JScrollPane scroll = new JScrollPane(panel);
        add(scroll);
        setVisible(true);
    }

    private void init(JPanel panel, Simulation simulation) {
        int niveauMax = simulation.getManager().getNiveauMaxCuisinier();
        ArrayList<Recette> recettes = simulation.getRecettes();

        JLabel titre = new JLabel("Niveau max cuisinier : " + niveauMax);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(titre);

        for (Recette recette : recettes) {
            panel.add(creerCarte(recette, niveauMax, simulation));
        }
    }

    private JPanel creerCarte(Recette recette, int niveauMax, Simulation simulation) {
        boolean debloquee = recette.estDebloquee(niveauMax);

        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));

        if (debloquee) {
            carte.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.green,2),
                    BorderFactory.createEmptyBorder(8,8,8,8)
                    ));
            carte.setBackground(new Color(230, 255, 230));
        } else {
            carte.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED,2),
                    BorderFactory.createEmptyBorder(8,8,8,8)
            ));
            carte.setBackground(new Color(255, 230, 230));
        }

        JLabel nomLabel = new JLabel(recette.getNom());
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel prixLabel = new JLabel("Prix de vente : " + recette.getPrix() + " G");
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel tempsLabel = new JLabel("Temps : " + recette.getTempsPreparation() + " tours");
        tempsLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel niveauLabel = new JLabel("Niveau requis : " + recette.getNiveauRequis());
        niveauLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        if (!debloquee) {
            niveauLabel.setForeground(Color.RED);
        }

        // Ingrédients
        String ingredientsStr = "Ingredients : ";
        for (Ingredient ingredient : recette.getIngredients().keySet()) {
            int quantite = recette.getIngredients().get(ingredient);
            ingredientsStr += ingredient.getNom() + " x" + quantite + ", ";
        }
        if (ingredientsStr.endsWith(", ")) {
            ingredientsStr = ingredientsStr.substring(0, ingredientsStr.length() - 2);
        }
        JLabel ingredientsLabel = new JLabel(ingredientsStr);
        ingredientsLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        int ventes = simulation.getDayStatistics().getVentesRecette(recette.getNom());
        JLabel ventesLabel = new JLabel("Vendues : " + ventes);
        ventesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        carte.add(nomLabel);
        carte.add(prixLabel);
        carte.add(tempsLabel);
        carte.add(niveauLabel);
        carte.add(ingredientsLabel);
        carte.add(ventesLabel);

        if (!debloquee) {
            JLabel verrou = new JLabel("VERROUILLE");
            verrou.setFont(new Font("SansSerif", Font.BOLD, 14));
            verrou.setForeground(Color.RED);
            carte.add(verrou);
        }
        carte.setMaximumSize(new Dimension(560, 140));
        carte.setPreferredSize(new Dimension(560, 140));

        return carte;
    }
}