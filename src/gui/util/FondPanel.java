package gui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Un panneau tout simple qui affiche une image en fond au lieu d'une couleur unie.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class FondPanel extends JPanel {
    private Image fond;
    /**
     * Prépare le panneau avec l'image qu'on a choisie.
     * 
     * @param fond l'image de fond
     */
    public FondPanel(Image fond) {
        this.fond = fond;
        setBackground(Color.gray);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fond != null) {
            g.drawImage(fond, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
