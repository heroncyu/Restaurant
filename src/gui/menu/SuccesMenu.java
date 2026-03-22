package gui.menu;

import engine.prestige.Succes;
import engine.process.ArgentRepository;
import engine.process.SuccesRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SuccesMenu extends JDialog {

    public SuccesMenu(JFrame owner) {
        super(owner, "Succès", true);
        setLayout(new BorderLayout());

        JPanel conteneur = new JPanel();
        conteneur.setLayout(new GridLayout(0, 1, 10, 10));
        conteneur.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        conteneur.setBackground(Color.GRAY);

        init(conteneur);

        JScrollPane scrollPane = new JScrollPane(conteneur);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        setSize(600, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void init(JPanel conteneur) {
        ArrayList<Succes> succes = SuccesRepository.getInstance().getSucces();

        for (Succes s : succes) {
            conteneur.add(creerLigne(s));
        }
    }

    private JPanel creerLigne(Succes s) {
        JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        ligne.setBackground(Color.LIGHT_GRAY);

        JTextArea description = new JTextArea();
        StringBuilder text = new StringBuilder();
        text.append(s.getNom() + "\n");
        text.append(s.getDescription() + "\n");
        text.append("Récompense : " + s.getRecompense() + " gold");
        description.setText(text.toString());
        description.setEditable(false);
        description.setFocusable(false);
        description.setBackground(Color.LIGHT_GRAY);
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JButton reclamer = new JButton("Réclamer");
        reclamer.setFont(new Font("SansSerif", Font.BOLD, 14));

        if (!s.isEstDebloque()) {
            reclamer.setEnabled(false);
            reclamer.setText("Verrouillé");
        } else if (s.isEstReclame()) {
            reclamer.setEnabled(false);
            reclamer.setText("Réclamé ");
        }

        reclamer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.setEstReclame(true);
                ArgentRepository.getInstance().ajouterMonnaie(s.getRecompense());
                reclamer.setEnabled(false);
                reclamer.setText("Réclamé ");
            }
        });

        ligne.add(description);
        ligne.add(reclamer);
        return ligne;
    }
}