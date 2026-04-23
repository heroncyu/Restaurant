package gui.menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import engine.item.Ingredient;
import engine.item.Recette;
import engine.process.SimulationUtility;
import engine.process.Simulation;

/**
 * Livre de recettes qui montre tous les plats que l'on peut servir.
 * 
 * Certaines recettes restent grisées si aucun cuisinier n'a un niveau assez élevé.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class RecettesMenu extends JDialog {

    /**
     * Affiche la fenêtre avec la liste des plats.
     * 
     * @param owner fenêtre ancrée et parente
     * @param simulation le jeu en cours
     */
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
        for (Recette recette : simulation.getRecettes()) {
            panel.add(creerCarte(recette, niveauMax, simulation));
        }
    }

    private JPanel creerCarte(Recette recette, int niveauMax, Simulation simulation) {
        boolean debloquee = recette.estDebloquee(niveauMax);
        JPanel carte = new JPanel(new BorderLayout(10, 0));
        if (debloquee) {
            carte.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.green, 2),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            carte.setBackground(new Color(230, 255, 230));
        } else {
            carte.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 2),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            carte.setBackground(new Color(255, 230, 230));
        }

        JLabel imgLabel = new JLabel();
        String nomFichier = recette.getNom().toLowerCase().replace(" ", "_");
        Image img = SimulationUtility.lireImage("src/resources/" + nomFichier + ".png");
        if (img != null) {
            imgLabel.setIcon(new ImageIcon(img.getScaledInstance(120, 80, Image.SCALE_SMOOTH)));
        }
        imgLabel.setPreferredSize(new Dimension(80, 80));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);

        JLabel nomLabel = new JLabel(recette.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel prixLabel = new JLabel("Prix : " + recette.getPrix() + " G  |  Temps : " + recette.getTempsPreparation() + " tours");
        prixLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel niveauLabel = new JLabel("Niveau requis : " + recette.getNiveauRequis());
        niveauLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (!debloquee) niveauLabel.setForeground(Color.RED);

        String ingredientsStr = "Ingredients : ";
        for (Ingredient ingredient : recette.getIngredients().keySet()) {
            ingredientsStr += ingredient.getNom() + " x" + recette.getIngredients().get(ingredient) + "  ";
        }
        JLabel ingredientsLabel = new JLabel(ingredientsStr.trim());
        ingredientsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        int ventes = simulation.getDayStatistics().getVentesRecette(recette.getNom());
        JLabel ventesLabel = new JLabel("Vendues : " + ventes);
        ventesLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        infos.add(nomLabel);
        infos.add(prixLabel);
        infos.add(niveauLabel);
        infos.add(ingredientsLabel);
        infos.add(ventesLabel);

        if (!debloquee) {
            JLabel verrou = new JLabel("VERROUILLE");
            verrou.setFont(new Font("Segoe UI", Font.BOLD, 12));
            verrou.setForeground(Color.RED);
            infos.add(verrou);
        }

        carte.add(imgLabel, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);
        carte.setMaximumSize(new Dimension(560, 110));
        carte.setPreferredSize(new Dimension(560, 110));
        return carte;
    }
}