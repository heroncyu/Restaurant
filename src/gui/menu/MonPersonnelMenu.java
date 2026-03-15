package gui.menu;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.mobile.Serveur;
import engine.process.Simulation;

public class MonPersonnelMenu extends JPanel{

    private ArrayList<JTextArea> descriptionServeurList = new ArrayList<JTextArea>();

    private Simulation simulation;


    public MonPersonnelMenu(Simulation simulation) {
        this.simulation = simulation;
        init();

        setLayout(new FlowLayout());
    }

    private void init() {
        for (Serveur serveur : simulation.getServeurs()) {
            addDescription(createDescription(serveur));
        }

        for (JTextArea descriptionServeur : descriptionServeurList) {
            add(descriptionServeur);
        }
    }

    private void addDescription(JTextArea description){
        descriptionServeurList.add(description);
    }

    private JTextArea createDescription(Serveur serveur) {
        JTextArea description = new JTextArea();
        StringBuilder text = new StringBuilder();

        text.append("Nom : " + serveur.getName() + "\n");
        text.append("Salaire : " + serveur.getSalaireBase() + "\n");
        text.append("Niveau de compétence : " + serveur.getNiveau());

        description.setText(text.toString());
        description.setEditable(false);
        description.setSize(60, 60);
        description.setFont(new Font("SansSerif", Font.BOLD, 18));

        return description;
    }


}