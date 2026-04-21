package gui.info;

import engine.process.SimulationUtility;

import javax.swing.*;
import java.awt.*;

/**
 * Le petit bouton avec une icône qui permet d'ouvrir le livre de recettes.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class RecettesButton extends InfoBouton {

    /**
     * Met l'image du livre de recettes sur le bouton.
     */
    public RecettesButton() {
        super("");
        Image img = SimulationUtility.lireImage("src/resources/recette.png");
        if (img != null) {
            setIcon(new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }
    }
}