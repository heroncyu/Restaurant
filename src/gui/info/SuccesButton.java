package gui.info;

import engine.process.SimulationUtility;
import engine.process.SuccesRepository;

import javax.swing.*;
import java.awt.*;

public class SuccesButton extends InfoBouton {

    public SuccesButton() {
        super("");
        Image img = SimulationUtility.lireImage("src/resources/succes.png");
        if (img != null) {
            setIcon(new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (SuccesRepository.getInstance().aUnSuccesEnAttente()) {
            g.setColor(Color.RED);
            g.fillOval(getWidth() - 10, 5, 10, 10);
        }
    }
}