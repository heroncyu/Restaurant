package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.process.SimulationUtility;
import gui.util.FondPanel;

import java.awt.Image;

/**
 * Fenêtre du menu principal qui s'ouvre au lancement du jeu.
 * 
 * Contient les boutons pour jouer, voir le tutoriel ou quitter.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class EcranTitreGUI extends JFrame {
    private Image fondImage = SimulationUtility.lireImage("src/resources/ecran_titre_fond.png");

    private JPanel boutonPanel = new JPanel();
    private FondPanel fondPanel;

    private JButton jouerButton = new JButton("JOUER");
    private JButton quitterButton = new JButton("QUITTER");
    private JButton tutoButton = new JButton("TUTORIEL");

    private JLabel creditLabel = new JLabel();
    private JLabel titreLabel = new JLabel();

    
    
    /**
     * Crée et affiche l'interface de l'écran titre.
     */
    public EcranTitreGUI() {
        super("Restaurant");
        setSize(1000, 800);
        this.fondPanel = new FondPanel(fondImage);

        init();
 
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        boutonPanel.setLayout(new GridLayout(3, 1, 0, 20));

        boutonPanel.setPreferredSize(new Dimension(350, 250));
        boutonPanel.setOpaque(false);

        initBouton();
        initLabel();

        boutonPanel.add(jouerButton);
        boutonPanel.add(tutoButton);
        boutonPanel.add(quitterButton);

        fondPanel.setLayout(new BorderLayout());

        JPanel taillePanel = new JPanel();
        taillePanel.setOpaque(false);
        taillePanel.setBorder(new EmptyBorder(100, 0, 0, 0));
        taillePanel.add(boutonPanel);

        fondPanel.add(taillePanel, BorderLayout.CENTER);
        fondPanel.add(creditLabel, BorderLayout.SOUTH);
        fondPanel.add(titreLabel, BorderLayout.NORTH);
        add(fondPanel);
    }

    private void initBouton() {
        jouerButton.addActionListener(new JouerButtonAction());
        tutoButton.addActionListener(new TutoButtonAction());
        quitterButton.addActionListener(new QuitterButtonAction());

        jouerButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tutoButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        quitterButton.setFont(new Font("Segoe UI", Font.BOLD, 20));

        jouerButton.setForeground(Color.white);
        tutoButton.setForeground(Color.white);
        quitterButton.setForeground(Color.white);

        jouerButton.setBackground(Color.gray);
        tutoButton.setBackground(Color.gray);
        quitterButton.setBackground(Color.gray);

        jouerButton.setBorder(BorderFactory.createLineBorder(Color.black));
        tutoButton.setBorder(BorderFactory.createLineBorder(Color.black));
        quitterButton.setBorder(BorderFactory.createLineBorder(Color.black));


        jouerButton.setContentAreaFilled(false);
        tutoButton.setContentAreaFilled(false);
        quitterButton.setContentAreaFilled(false);

        jouerButton.setFocusPainted(false);
        tutoButton.setFocusPainted(false);
        quitterButton.setFocusPainted(false);

        jouerButton.setOpaque(true);
        tutoButton.setOpaque(true);
        quitterButton.setOpaque(true);
    }

    private void initLabel() {
        creditLabel.setText("Par : EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim");

        creditLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        creditLabel.setForeground(Color.white);
        creditLabel.setOpaque(false);
        creditLabel.setHorizontalAlignment(JLabel.CENTER);

        titreLabel.setText("Restaurant");

        titreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 80));
        titreLabel.setForeground(Color.red);
        titreLabel.setOpaque(false);
        titreLabel.setHorizontalAlignment(JLabel.CENTER);
        titreLabel.setBorder(new EmptyBorder(100, 0, 0, 0));
    }

    private class JouerButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            MainGUI GUI = new MainGUI();
            Thread gameThread = new Thread(GUI);
            gameThread.start();
        }
    }

    private class TutoButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //new TutoGUI();
            //dispose();
        }
    }

    private class QuitterButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
