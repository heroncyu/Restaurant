package engine.item;

/**
 * Instanciation matérielle d'une {@link Recette} cuisinée pour le service.
 * 
 * Contient un score de qualité défini au terme de sa préparation par le {@link Cuisinier}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Plat {
    private Recette recette;
    private int qualite;
    
    /**
     * Construit le plat en attente de validation / cuisson qui se base sur une recette demandée.
     * 
     * @param recette base de préparation et d'informations {@link Recette}
     */
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
