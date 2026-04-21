package engine.mobile;

import engine.map.Block;

/**
 * Définit la main d'œuvre rattachée à la cuisson des {@link Plat}.
 * 
 * Possède un salaire, une compétence et occupe un état variable piloté
 * par le manager.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Cuisinier extends MobileElement{
    private int salaireBase;
    private int niveau;
    private String name;

    /**
     * Définit minimalement les propriétés physiques du personnel en cuisine.
     * 
     * @param position sa case fixe
     */
    public Cuisinier(Block position) {
        super(position);
    }
    /**
     * Engage un profil plus précis des compétences d'employé de l'entreprise.
     * 
     * @param position case d'assignation
     * @param niveau d'accès et capacité de travail de la recrue
     * @param salaireBase la retenue prélevée à la fin de chaque période
     * @param name nom personnel rattaché d'affichage
     */
    public Cuisinier(Block position,int niveau, int salaireBase,String name){
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
