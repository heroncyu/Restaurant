package gui.menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import engine.prestige.Succes;
import engine.process.ArgentRepository;
import engine.process.SuccesRepository;
import engine.process.SimulationUtility;

public class SuccesMenu extends JDialog {

    public SuccesMenu(JFrame owner) {
        super(owner, "Succès", true);
        setSize(600, 500);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.darkGray);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        init(panel);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);

        add(scroll);
        setVisible(true);
    }

    private void init(JPanel panel) {
        ArrayList<Succes> succes = SuccesRepository.getInstance().getSucces();
        for (Succes s : succes) {
            panel.add(creerCarte(s));
            panel.add(Box.createVerticalStrut(10));
        }
    }

    private JPanel creerCarte(Succes s) {
        boolean debloque = s.isEstDebloque();
        boolean reclame = s.isEstReclame();


        JPanel carte = new JPanel(new BorderLayout(10, 0));
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        carte.setBackground(new Color(50, 50, 50));

        JLabel imgLabel = new JLabel();
        String nomFichier = s.getNom().toLowerCase().replace(" ", "_");
        Image img = SimulationUtility.lireImage("src/resources/" + nomFichier + ".png");

        if (img == null) {
            img = SimulationUtility.lireImage("src/resources/trophee.png");
        }

        if (img != null) {
            imgLabel.setIcon(new ImageIcon(img.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        }

        imgLabel.setPreferredSize(new Dimension(90, 90));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);

        JLabel nomLabel = new JLabel(s.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nomLabel.setForeground(Color.WHITE);

        JLabel descLabel = new JLabel(
                s.getDescription() + "  —  Récompense : " + s.getRecompense() + " gold");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.LIGHT_GRAY);


        JButton reclamer = new JButton();
        reclamer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reclamer.setFocusPainted(false);
        reclamer.setOpaque(true);
        reclamer.setBorderPainted(false);

        if (!debloque) {
            reclamer.setText("Verrouillé");
            reclamer.setEnabled(false);
            reclamer.setBackground(Color.RED);
            reclamer.setForeground(Color.WHITE);

        } else if (reclame) {
            reclamer.setText("Réclamé");
            reclamer.setEnabled(false);
            reclamer.setBackground(Color.GRAY);
            reclamer.setForeground(Color.WHITE);

        } else {
            reclamer.setText("Réclamer");
            reclamer.setBackground(new Color(50, 180, 50));
            reclamer.setForeground(Color.BLACK);
        }

        reclamer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.setEstReclame(true);
                ArgentRepository.getInstance().ajouterMonnaie(s.getRecompense());

                reclamer.setEnabled(false);
                reclamer.setText("Réclamé");
                reclamer.setBackground(Color.GRAY);
                reclamer.setForeground(Color.WHITE);
            }
        });

        infos.add(nomLabel);
        infos.add(Box.createVerticalStrut(6));
        infos.add(descLabel);
        infos.add(Box.createVerticalStrut(8));
        infos.add(reclamer);

        carte.add(imgLabel, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        carte.setMaximumSize(new Dimension(560, 110));
        carte.setPreferredSize(new Dimension(560, 110));

        return carte;
    }
}