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
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.process.SaveManager;
import engine.process.SimulationUtility;
import gui.TutorielGUI;
import gui.util.FondPanel;
import org.apache.log4j.Logger;
import log.LoggerUtility;

import java.awt.Image;

/**
 * Fenêtre du menu principal qui s'ouvre au lancement du jeu.
 * 
 * Contient les boutons pour jouer, voir le tutoriel ou quitter.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class EcranTitreGUI extends JFrame {
    private static Logger logger = LoggerUtility.getLogger(EcranTitreGUI.class, "html");

    private Image fondImage = SimulationUtility.lireImage("src/resources/ecran_titre_fond.png");

    private JPanel boutonPanel = new JPanel();
    private FondPanel fondPanel;

    private JButton jouerButton    = new MenuButton("NOUVELLE PARTIE");
    private JButton chargerButton  = new MenuButton("CHARGER");
    private JButton quitterButton  = new MenuButton("QUITTER");
    private JButton tutoButton     = new MenuButton("TUTORIEL");

    private JLabel creditLabel = new JLabel();
    private JLabel titreLabel = new JLabel();

    
    
    /**
     * Crée et affiche l'interface de l'écran titre.
     */
    public EcranTitreGUI() {
        super("Restaurant");
        setSize(1280, 920);
        this.fondPanel = new FondPanel(fondImage);

        logger.info("Initialisation de l'écran titre");
        init();
 
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        boutonPanel.setLayout(new GridLayout(4, 1, 0, 20));

        boutonPanel.setPreferredSize(new Dimension(350, 330));
        boutonPanel.setOpaque(false);

        initBouton();
        initLabel();

        boutonPanel.add(jouerButton);
        boutonPanel.add(chargerButton);
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
        chargerButton.addActionListener(new ChargerButtonAction());
        tutoButton.addActionListener(new TutoButtonAction());
        quitterButton.addActionListener(new QuitterButtonAction());

        // Grise le bouton si aucune sauvegarde n'existe
        if (!SaveManager.sauvegardeExiste()) {
            chargerButton.setEnabled(false);
        }
    }

    private void initLabel() {
        creditLabel.setText("Par : EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim");

        creditLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        creditLabel.setForeground(Color.white);
        creditLabel.setOpaque(false);
        creditLabel.setHorizontalAlignment(JLabel.CENTER);

        Image logoImage = SimulationUtility.lireImage("src/resources/croustycoon_title_logo.png");
        if (logoImage != null) {
            Image scaledLogo = logoImage.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
            titreLabel.setIcon(new javax.swing.ImageIcon(scaledLogo));
        } else {
            titreLabel.setText("CrousTycoon");
            titreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 80));
            titreLabel.setForeground(Color.red);
        }
        
        titreLabel.setOpaque(false);
        titreLabel.setHorizontalAlignment(JLabel.CENTER);
        titreLabel.setBorder(new EmptyBorder(100, 0, 0, 0));
    }

    private class JouerButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.trace("Bouton JOUER cliqué, lancement de MainGUI");
            dispose();
            MainGUI GUI = new MainGUI();
            Thread gameThread = new Thread(GUI);
            gameThread.start();
        }
    }

    private class ChargerButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.trace("Bouton CHARGER cliqué, lancement de MainGUI avec sauvegarde");
            dispose();
            MainGUI GUI = new MainGUI();
            GUI.chargerSauvegarde();
            Thread gameThread = new Thread(GUI);
            gameThread.start();
        }
    }

    private class TutoButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.trace("Bouton TUTORIEL cliqué, ouverture du tutoriel");
            new TutorielGUI(EcranTitreGUI.this);
        }
    }

    private class QuitterButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.trace("Bouton QUITTER cliqué, fermeture de l'application");
            dispose();
        }
    }
    private class MenuButton extends JButton {
        private Color hoverBackgroundColor = new Color(255, 140, 0, 200); 
        private Color normalBackgroundColor = new Color(0, 0, 0, 180); 
        private Color pressedBackgroundColor = new Color(255, 69, 0, 220); 
        private Color borderColor = new Color(255, 215, 0); 

        public MenuButton(String text) {
            super(text);
            super.setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 26));
            setForeground(Color.WHITE);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(pressedBackgroundColor);
            } else if (getModel().isRollover()) {
                g2.setColor(hoverBackgroundColor);
            } else {
                g2.setColor(normalBackgroundColor);
            }
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
            
            g2.setStroke(new java.awt.BasicStroke(2.5f));
            g2.setColor(borderColor);
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 40, 40);
            
            super.paintComponent(g);
        }
    }
}
