package gui.menu;

import engine.process.DayStatistics;
import org.jfree.data.time.Day;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.CardLayout;

public class IngredientMenu extends JDialog {

    private CardLayout cl;
    private JPanel conteneur;

    private JPanel listeIngredientPanel;

    public IngredientMenu(JFrame owner,DayStatistics daystatistic) {
        super(owner, "Gestion des ingrédients", true);
        cl = new CardLayout();
        conteneur = new JPanel(cl);
        add(conteneur);

        init(daystatistic);

        setSize(820, 520);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void init(DayStatistics dayStatistics) {
        listeIngredientPanel = new ListeIngredientMenu(dayStatistics);
        JScrollPane scrollPane = new JScrollPane(listeIngredientPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        conteneur.add(scrollPane, "Liste");
        cl.show(conteneur, "Liste");
    }
}