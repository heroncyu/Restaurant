package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ReputationRepository;
import gui.PaintStrategy;

/**
 * L'étiquette qui montre notre score de réputation (en nombre d'étoiles ou de points).
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ReputationLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ReputationRepository reputationRepository = ReputationRepository.getInstance();

    /**
     * Prépare le texte pour afficher la réputation.
     */
    public ReputationLabel() {
        super();

        setFont(new Font("Segoe UI", Font.PLAIN, 20));
        setForeground(Color.BLACK);

        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setOpaque(false);

    }

    /**
     * Met à jour tout seul le score si des clients sont contents ou fâchés, et le redessine.
     * 
     * @param graphics l'outil de dessin
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        paintStrategy.paint(this, graphics);
        setText("Réputation : " + reputationRepository.getReputation());
        super.paintComponent(graphics);
    }
}
