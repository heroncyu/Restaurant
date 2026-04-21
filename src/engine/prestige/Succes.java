package engine.prestige;

/**
 * Structure de donnees pour un accomplissement / succes specifique dans le jeu.
 * 
 * Definit les prerequis textuels et donne un statut de debloquage pour 
 * recompenser le joueur (traite par {@link SuccesRepository}).
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Succes {
    private String nom;
    private String description;
    private int recompense;
    private boolean estDebloque;
    private boolean estReclame;

    /**
     * Definit un nouveau succes recuperable.
     * 
     * @param nom le titre du succes
     * @param description la condition pour valider ce succes
     * @param recompense le bonus remunere accordé lors de la recuperation
     */
    public Succes(String nom, String description, int recompense) {
        this.nom = nom;
        this.description = description;
        this.recompense = recompense;
        this.estDebloque = false;
        this.estReclame = false;
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

    public boolean isEstReclame() {
        return estReclame;
    }

    public void setEstReclame(boolean estReclame) {
        this.estReclame = estReclame;
    }
}