package gui.info;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Bouton transparent utilisé en haut de l'écran pour les menus (Pause, Nettoyer, etc).
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class InfoBouton extends JButton {

    /**
     * Crée le bouton et enlève son fond gris et ses bordures pour ne garder que le texte ou l'image.
     * 
     * @param texte ce qu'il y a écrit sur le bouton
     */
    public InfoBouton(String texte) {
        super(texte);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 5));
    }
}
