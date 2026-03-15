package engine.mobile;

import engine.map.Block;

public class Serveur extends MobileElement{
    private int salaireBase;
    private int niveau;
    private String name;

    public Serveur(Block position) {
        super(position);
    }
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
