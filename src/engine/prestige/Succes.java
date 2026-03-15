package engine.prestige;

public class Succes {
    private String nom;
    private String description;
    private int recompense;
    private boolean estDebloque;

    public Succes(String nom,String description,int recompense){
        this.nom = nom;
        this.description = description;
        this.recompense = recompense;
        estDebloque = false;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecompense() {
        return recompense;
    }

    public void setRecompense(int recompense) {
        this.recompense = recompense;
    }

    public boolean isEstDebloque() {
        return estDebloque;
    }

    public void setEstDebloque(boolean estDebloque) {
        this.estDebloque = estDebloque;
    }
}
