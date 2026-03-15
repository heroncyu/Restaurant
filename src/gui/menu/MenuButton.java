package gui.menu;


import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.SwingConstants;

public class MenuButton extends JButton {
    private final int TAILLE = 40;

    private Color fondNormal = new Color(0, 0, 0, 180);
    private Color fondClique = new Color(70, 70, 70, 180);
    private Color texteNormal = new Color(200, 200, 200);
    private Color bordureNormal = new Color(100, 100, 100);

    public MenuButton(String text, int x, int y) {
        super(text);

        this.setBounds(x, y, TAILLE, TAILLE);

        // Style de base
        this.setOpaque(false);
        this.setBackground(fondNormal);
        this.setForeground(texteNormal);
        this.setBorder(BorderFactory.createLineBorder(bordureNormal, 2));


        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.setFont(new Font("Arial", Font.BOLD, 20));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(fondClique);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
