package engine.item;

public class Plat {
    private Recette recette;
    private int qualite;
    
    public Plat(Recette recette) {
        this.recette = recette;
        this.qualite = 0;
    }

    public Recette getRecette() {
        return recette;
    }

    public void setRecette(Recette recette) {
        this.recette = recette;
    }

    public int getQualite() {
        return qualite;
    }
    
    public void setQualite(int qualite) {
        this.qualite = qualite;
    }

    
}
