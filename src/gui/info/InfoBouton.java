package gui.info;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class InfoBouton extends JButton {

    public InfoBouton(String texte) {
        super(texte);

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 5));
    }
}
