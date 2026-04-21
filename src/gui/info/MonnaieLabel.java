package gui.info;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ArgentRepository;
import gui.PaintStrategy;

/**
 * L'étiquette qui affiche combien d'argent on a.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class MonnaieLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ArgentRepository argentRepository = ArgentRepository.getInstance();

    /**
     * Prépare le texte en jaune pour faire penser à de l'or.
     * 
     * @param font le style d'écriture utilisé
     */
    public MonnaieLabel(Font font) {
        super();

        setFont(font);
        setForeground(Color.YELLOW);

        setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        setOpaque(false);

    }

    /**
     * Met à jour tout seul notre argent si on gagne ou dépense, et redessine l'étiquette.
     * 
     * @param g l'outil de dessin
     */
    @Override
    protected void paintComponent(Graphics g) {
        paintStrategy.paint(this, g);
        setText(argentRepository.getMonnaieString());
        super.paintComponent(g);
    }
}
