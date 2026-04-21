package engine.prestige;

/**
 * Modele encapsulant les finances du restaurant.
 * 
 * Stocke la valeur actuelle de la monnaie ainsi que le detail des revenus
 * et depenses realises au cours d'une journee. Géré via {@link ArgentRepository}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Argent {
    private int monnaie;
    private int revenueJournee;
    private int depenseJournee;

    /**
     * Construit une ressource d'argent avec un montant initial.
     * 
     * @param monnaie montant cible de départ
     */
    public Argent(int monnaie) {
        this.monnaie = monnaie;
        revenueJournee = 0;
        depenseJournee = 0;
    }

    public int getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(int monnaie) {
        this.monnaie = monnaie;
    }

    public int getRevenueJournee() {
        return revenueJournee;
    }

    public void setRevenueJournee(int revenueJournee) {
        this.revenueJournee = revenueJournee;
    }

    public int getDepenseJournee() {
        return depenseJournee;
    }

    public void setDepenseJournee(int depenseJournee) {
        this.depenseJournee = depenseJournee;
    }
}
