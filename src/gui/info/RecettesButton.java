package gui.info;

import engine.process.SimulationUtility;

import javax.swing.*;
import java.awt.*;

public class RecettesButton extends InfoBouton {

    public RecettesButton() {
        super("");
        Image img = SimulationUtility.lireImage("src/resources/recette.png");
        if (img != null) {
            setIcon(new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }
    }
}