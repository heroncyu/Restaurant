package gui.menu;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTextArea;

import engine.item.Ingredient;
import engine.process.ArgentRepository;
import engine.process.DayStatistics;
import engine.process.StockRepository;
import org.jfree.data.time.Day;

public class ListeIngredientMenu extends JPanel {

    private ArrayList<JPanel> ligneIngredientList = new ArrayList<JPanel>();
    private DayStatistics dayStatistics;

    public ListeIngredientMenu(DayStatistics dayStatistics) {
        this.dayStatistics = dayStatistics;
        setLayout(new GridLayout(0, 2, 20, 20));
        init();
    }

    private void init() {
        for (Ingredient ingredient : StockRepository.getInstance().getStockage().getIngredients().keySet()) {
            ajouterLigne(creerLigne(ingredient));
        }

        for (JPanel ligne : ligneIngredientList) {
            add(ligne);
        }
    }

    private void ajouterLigne(JPanel ligne) {
        ligneIngredientList.add(ligne);
    }

    private JPanel creerLigne(Ingredient ingredient) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));

        StockRepository stockRepository = StockRepository.getInstance();

        JTextArea description = new JTextArea();
        StringBuilder text = new StringBuilder();
        text.append("Nom : " + ingredient.getNom() + "\n");
        text.append("Stock : " + stockRepository.getStockage().getIngredients().getOrDefault(ingredient, 0) + "\n");
        text.append("Prix : " + ingredient.getPrix() + " G");
        description.setText(text.toString());
        description.setEditable(false);
        description.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton acheter = new JButton("Acheter");
        acheter.setFont(new Font("SansSerif", Font.BOLD, 14));
        acheter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArgentRepository argentRepository = ArgentRepository.getInstance();
                if (argentRepository.getMonnaie() >= ingredient.getPrix()) {

                    argentRepository.retirerMonnaie(ingredient.getPrix());
                    stockRepository.approvisionner(ingredient, 1);
                    dayStatistics.addAchat(ingredient.getPrix());


                    description.setText(
                            "Nom : " + ingredient.getNom() + "\n" +
                                    "Stock : " + stockRepository.getStockage().getIngredients().getOrDefault(ingredient, 0) + "\n" +
                                    "Prix : " + ingredient.getPrix() + " G"
                    );
                }
                repaint();
            }
        });

        ligne.add(description);
        ligne.add(acheter);
        return ligne;
    }
}