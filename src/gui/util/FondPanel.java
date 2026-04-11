package gui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class FondPanel extends JPanel {
    private Image fond;
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
