package gui.info;

import engine.process.SimulationUtility;
import engine.process.SuccesRepository;

import javax.swing.*;
import java.awt.*;

/**
 * Bouton avec la coupe (succès).
 * 
 * Il affiche un petit point rouge si on vient de réussir une mission pour nous dire d'aller cliquer.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class SuccesButton extends InfoBouton {

    /**
     * Met l'image de la coupe sur le bouton.
     */
    public SuccesButton() {
        super("");
        Image img = SimulationUtility.lireImage("src/resources/succes.png");
        if (img != null) {
            setIcon(new ImageIcon(img.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        }
    }

    /**
     * Dessine le bouton normal et ajoute un rond rouge s'il y a une récompense à récupérer.
     * 
     * @param g l'outil de dessin
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (SuccesRepository.getInstance().aUnSuccesEnAttente()) {
            g.setColor(Color.RED);
            g.fillOval(getWidth() - 10, 5, 10, 10);
        }
    }
}