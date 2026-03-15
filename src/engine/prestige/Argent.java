package engine.prestige;

public class Argent {
    private int monnaie;
    private int revenueJournee;
    private int depenseJournee;

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
