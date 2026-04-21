package engine.mobile;

import engine.map.Block;

/**
 * Représente un personnel apte à transférer les requêtes et les repas depuis/vers la salle.
 * 
 * Modèle similaire d'information d'employé que le {@link Cuisinier}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Serveur extends MobileElement{
    private int salaireBase;
    private int niveau;
    private String name;

    /**
     * Position de service générique de secours.
     * 
     * @param position espace du serveur
     */
    public Serveur(Block position) {
        super(position);
    }
    /**
     * Fiche descriptive du travailleur prêt à prendre son poste.
     * 
     * @param position zone de base
     * @param niveau efficacité
     * @param salaireBase le prix minimum perçu 
     * @param name la désignation textuelle
     */
    public Serveur(Block position,int niveau, int salaireBase,String name){
        this(position);
        this.salaireBase = salaireBase;
        this.name = name;
        this.niveau = niveau;
    }

    public int getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(int salaireBase) {
        this.salaireBase = salaireBase;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
