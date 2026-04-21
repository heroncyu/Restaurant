package gui.info;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ArgentRepository;
import gui.PaintStrategy;

/**
 * L'étiquette (texte) qui affiche le nombre de jours joués depuis le début.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class JourLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ArgentRepository argentRepository = ArgentRepository.getInstance();

    /**
     * Prépare le style du texte du compteur de jours.
     * 
     * @param font le style d'écriture utilisé
     */
    public JourLabel(Font font) {
        super();
        setFont(font);
        setForeground(Color.white);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * Met à jour le texte tout seul (ex: passe de J-1 à J-2) et redessine l'étiquette.
     * 
     * @param g l'outil de dessin
     */
    protected void paintComponent(Graphics g) {
        paintStrategy.paint(this, g);
        setText("J-" + (argentRepository.getArgentHistory().size()));
        super.paintComponent(g);
    }

}
