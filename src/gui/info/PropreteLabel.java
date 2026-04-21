package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.PropreteRepository;
import gui.PaintStrategy;

/**
 * L'étiquette qui montre le score de propreté du restaurant.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class PropreteLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private PropreteRepository propreteRepository = PropreteRepository.getInstance();

    /**
     * Prépare le texte noir avec son fond et ses marges.
     */
    public PropreteLabel() {
        super();

        setFont(new Font("Segoe UI", Font.PLAIN, 20));
        setForeground(Color.BLACK);

        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setOpaque(false);
    }

    /**
     * Met à jour tout seul la propreté (si ça se salit ou si on nettoie) et redessine l'étiquette.
     * 
     * @param graphics l'outil de dessin
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        paintStrategy.paint(this, graphics);
        setText("Propreté : " + propreteRepository.getProprete() + "%");
        super.paintComponent(graphics);
    }
}