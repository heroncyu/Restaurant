package engine.prestige;

/**
 * Modele stockant le score actuel de propreté globale du bâtiment.
 * 
 * Géré par l'outil de repository correspondant {@link PropreteRepository}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Proprete {
    private int scoreProprete;
    /**
     * Initialise l'état de propreté initial à une valeur définie.
     * 
     * @param scoreProprete montant cible initial
     */
    public Proprete(int scoreProprete){
        this.scoreProprete = scoreProprete;
    }

    public int getScoreProprete() {
        return scoreProprete;
    }

    public void setScoreProprete(int scoreProprete) {
        this.scoreProprete = scoreProprete;
    }
}
