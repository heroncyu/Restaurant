package engine.mobile;

import engine.map.Block;

/**
 * Un meuble statique (Table, décorations, etc) héritant du socle d'objet visible.
 * 
 * Contribue à habiller l'espace constructible de l'établissement.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Meuble extends MobileElement{
    private int prix;
    private String type;

    /**
     * Constitue un socle de meuble positionnable.
     * 
     * @param position placement final choisi
     */
    public Meuble(Block position) {
        super(position);
    }

    /**
     * Crée l'item meuble lié par un type textuel qualitatif.
     * 
     * @param position place cible
     * @param type intitulé (ex: deco, siege, etalage)
     */
    public Meuble(Block position, String type){
        this(position);
        this.type = type;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
