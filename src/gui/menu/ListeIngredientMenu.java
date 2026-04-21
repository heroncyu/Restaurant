package gui.menu;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import engine.item.Ingredient;
import engine.process.ArgentRepository;
import engine.process.DayStatistics;
import engine.process.StockRepository;

/**
 * Fait la liste de tous les ingrédients qu'on peut acheter au supermarché.
 * 
 * Vérifie si on a assez d'argent et de place avant d'ajouter au stock.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ListeIngredientMenu extends JPanel {

    private ArrayList<JPanel> ligneIngredientList = new ArrayList<JPanel>();
    private DayStatistics dayStatistics;
    private Font font = new Font("Segoe UI", Font.BOLD, 20);
    private JProgressBar barreStock;
    private JLabel labelStock;

    /**
     * Prépare la liste avec la barre de défilement pour qu'on puisse la parcourir de haut en bas.
     * 
     * @param dayStatistics pour noter tout ce qu'on dépense cette journée
     */
    public ListeIngredientMenu(DayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
        setLayout(new GridLayout(0, 1, 0, 10));
        setBackground(Color.gray);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        init();
    }

    private void init() {
        add(creerBarreStockage());

        for (Ingredient ingredient : StockRepository.getInstance().getStockage().getIngredients().keySet()) {
            ajouterLigne(creerLigne(ingredient));
        }
        for (JPanel ligne : ligneIngredientList) {
            add(ligne);
        }
    }

    private JPanel creerBarreStockage() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setBackground(Color.gray);

        StockRepository stockRepository = StockRepository.getInstance();
        int actuel = stockRepository.getStockage().quantiteTotale();
        int max = stockRepository.getCapaciteMax();

        labelStock = new JLabel("Stockage : " + actuel + " / " + max);
        labelStock.setFont(font);
        labelStock.setHorizontalAlignment(JLabel.CENTER);

        barreStock = new JProgressBar(0, max);
        barreStock.setValue(actuel);
        barreStock.setForeground(couleurBarre(actuel, max));
        barreStock.setPreferredSize(new java.awt.Dimension(0, 30));

        panel.add(labelStock);
        panel.add(barreStock);

        return panel;
    }

    private Color couleurBarre(int actuel, int max) {
        if (actuel >= max) return Color.red;
        if (actuel >= max * 0.75) return Color.orange;
        return Color.green;
    }

    private void mettreAJourBarre() {
        StockRepository stockRepository = StockRepository.getInstance();
        int actuel = stockRepository.getStockage().quantiteTotale();
        int max = stockRepository.getCapaciteMax();
        barreStock.setValue(actuel);
        barreStock.setForeground(couleurBarre(actuel, max));
        labelStock.setText("Stockage : " + actuel + " / " + max);
    }

    private void ajouterLigne(JPanel ligne) {
        ligneIngredientList.add(ligne);
    }

    private JPanel creerLigne(Ingredient ingredient) {
        JPanel ligne = new JPanel(new GridLayout(1, 3));
        ligne.setBorder(BorderFactory.createLineBorder(Color.black));

        StockRepository stockRepository = StockRepository.getInstance();

        JLabel nom = new JLabel("Nom : " + ingredient.getNom());
        nom.setFont(font);
        nom.setHorizontalAlignment(JLabel.CENTER);

        JLabel infos = new JLabel("Stock : " + stockRepository.getStockage().getIngredients().getOrDefault(ingredient, 0) + "  |  Prix : " + ingredient.getPrix() + " G");
        infos.setFont(font);
        infos.setHorizontalAlignment(JLabel.CENTER);

        JButton acheter = new JButton("Acheter");
        acheter.setFont(font);
        acheter.setBackground(Color.green);
        acheter.setFocusPainted(false);
        acheter.setContentAreaFilled(false);
        acheter.setOpaque(true);

        JPanel acheterPanel = new JPanel(new GridBagLayout());
        acheterPanel.add(acheter);

        acheter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArgentRepository argentRepository = ArgentRepository.getInstance();
                if (argentRepository.getMonnaie() >= ingredient.getPrix()) {
                    if (!stockRepository.peutApprovisionner(1)) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Stockage plein !");
                    } else {
                        argentRepository.retirerMonnaie(ingredient.getPrix());
                        stockRepository.approvisionner(ingredient, 1);
                        dayStatistics.addAchat(ingredient.getPrix());
                        infos.setText("Stock : " + stockRepository.getStockage().getIngredients().getOrDefault(ingredient, 0) + "  |  Prix : " + ingredient.getPrix() + " G");
                        mettreAJourBarre();
                    }
                }
                repaint();
            }
        });

        ligne.add(nom);
        ligne.add(infos);
        ligne.add(acheterPanel);

        return ligne;
    }
}