package gui.menu;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.process.Simulation;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonnelMenu extends JDialog{

    private CardLayout cl;
    private JPanel conteneur;

    private JPanel accueilPanel, monPersonnelPanel;
    private JButton monPersonnelBouton;

    public PersonnelMenu(JFrame owner, Simulation simulation){
        super(owner, "Personnel Menu", true);
        cl = new CardLayout();
        conteneur = new JPanel(cl);
        add(conteneur);

        init(simulation);

        setSize(820, 520);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void init(Simulation simulation) {
        accueilPanel = new JPanel();
        monPersonnelPanel = new MonPersonnelMenu(simulation);

        conteneur.add(accueilPanel, "Accueil");
        conteneur.add(monPersonnelPanel, "Mon Personnel"); 

        accueilPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        monPersonnelBouton = new JButton("Mon Personnel");
        monPersonnelBouton.addActionListener(new MonPersonnelBoutonAction());
        accueilPanel.add(monPersonnelBouton);

    }

    private class MonPersonnelBoutonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cl.show(conteneur, "Mon Personnel");
        }
    }



}
