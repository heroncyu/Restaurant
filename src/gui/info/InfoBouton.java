package gui.info;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class InfoBouton extends JButton {

    public InfoBouton(String texte) {
        super(texte);

        setBorderPainted(false); // Enlève la bordure
        setContentAreaFilled(false); // Rend le fond transparent
        setFocusPainted(false); // Enlève le petit cadre de sélection
        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 5));
    }
}
