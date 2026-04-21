package engine.mobile;

import config.GameConfiguration;
import engine.map.Block;

/**
 * Représente l'entité de base capable d'occuper une position physique sur la {@link Map}.
 * 
 * Cette classe abstraite donne des coordonnées de position et de direction que partagent
 * le personnel, les clients de même que les structures de décors.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public abstract class MobileElement {
    private Block position;
    private String direction;

    /**
     * Initialise l'entité mobile en lui fournissant une base d'apparition.
     * 
     * @param position la case spatiale par défaut de cet élément
     */
    public MobileElement(Block position) {
        this.position = position;
        this.direction = GameConfiguration.BAS;
    }

    public Block getPosition() {
        return position;
    }

    public void setPosition(Block position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
