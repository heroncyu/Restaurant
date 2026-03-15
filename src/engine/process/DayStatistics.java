package engine.process;

import config.GameConfiguration;

public class DayStatistics {
    private int nbJour;
    private int nbCommandesDuJour;
    private int argentJourPrecedent;
    private int reputationJourPrecedent;

    private int depensesDuJour;
    private int revenusDuJour;
    private int beneficesDuJour;

    private int coutLoyerDuJour;
    private int coutSalairesDuJour;
    private int revenusCommandesDuJour;
    private int coutConstructionDuJour;
    private int revenusPourboireDuJour;
    private int achatDujour;

    public DayStatistics() {
        this.nbJour = 0;
        this.nbCommandesDuJour = 0;
        this.argentJourPrecedent = GameConfiguration.INITIAL_MONEY;
        this.reputationJourPrecedent = 0;
        this.depensesDuJour = 0;
        this.revenusDuJour = 0;
        this.beneficesDuJour = 0;
        this.coutLoyerDuJour = 0;
        this.coutSalairesDuJour = 0;
        this.revenusCommandesDuJour = 0;
        this.coutConstructionDuJour = 0;
        this.revenusPourboireDuJour = 0;
        this.achatDujour = 0;
    }

    public void update() {
        this.nbJour++;
        this.nbCommandesDuJour = 0;
        this.argentJourPrecedent = ArgentRepository.getInstance().getMonnaie();
        //this.reputationJourPrecedent = PrestigeRepository.getInstance().getReputation();
        this.depensesDuJour = 0;
        this.revenusDuJour = 0;
        this.beneficesDuJour = 0;
        this.coutLoyerDuJour = 0;
        this.coutSalairesDuJour = 0;
        this.revenusCommandesDuJour = 0;
        this.coutConstructionDuJour = 0;
        this.revenusPourboireDuJour = 0;
        this.achatDujour = 0;
    }

    public int calculBenefices() {
        this.beneficesDuJour = ArgentRepository.getInstance().getMonnaie() - this.argentJourPrecedent;
        return this.beneficesDuJour;
    }

    public int calculRevenus() {
        this.revenusDuJour = this.revenusCommandesDuJour + this.revenusPourboireDuJour;
        return this.revenusDuJour;
    }
    
    public int calculDepenses() {
        this.depensesDuJour = this.coutLoyerDuJour + this.coutSalairesDuJour + this.coutConstructionDuJour + this.achatDujour;
        return this.depensesDuJour;
    }

    public void addCoutConstruction(int coutConstruction) {
        this.coutConstructionDuJour += coutConstruction;
    }

    public void addRevenusCommandes(int coutCommandes) {
        this.revenusCommandesDuJour += coutCommandes; 
    }

    public void addCoutSalaires(int coutSalaires) {
        this.coutSalairesDuJour += coutSalaires;
    }

    public void addCoutLoyer(int coutLoyer) {
        this.coutLoyerDuJour += coutLoyer;
    }

    public void addCommande() {
        this.nbCommandesDuJour++;
    }

    public void addRevenusPourboire(int pourboire) {
        this.revenusPourboireDuJour += pourboire;
    }

    public void addAchat(int achat) {
        this.achatDujour += achat;
    }


    
    public int getNbJour() {
        return nbJour;
    }
    
    public int getNbCommandesDuJour() {
        return nbCommandesDuJour;
    }
    
    public int getArgentJourPrecedent() {
        return argentJourPrecedent;
    }
    
    public int getReputationJourPrecedent() {
        return reputationJourPrecedent;
    }
    
    public int getDepensesDuJour() {
        return depensesDuJour;
    }
    
    public int getBeneficesDuJour() {
        return beneficesDuJour;
    }
    
    public int getCoutLoyerDuJour() {
        return coutLoyerDuJour;
    }
    
    public int getCoutSalairesDuJour() {
        return coutSalairesDuJour;
    }
    
    public int getRevenusCommandesDuJour() {
        return revenusCommandesDuJour;
    }
    
    public int getCoutConstructionDuJour() {
        return coutConstructionDuJour;
    }

    public int getRevenusPourboireDuJour() {
        return revenusPourboireDuJour;
    }

    public int getRevenusDuJour() {
        return revenusDuJour;
    }

    public int getAchatDujour() {
        return achatDujour;
    }

    public void setNbCommandesDuJour(int nbCommandesDuJour) {
        this.nbCommandesDuJour = nbCommandesDuJour;
    }
    
    public void setArgentJourPrecedent(int argentJourPrecedent) {
        this.argentJourPrecedent = argentJourPrecedent;
    }
    
    public void setReputationJourPrecedent(int reputationJourPrecedent) {
        this.reputationJourPrecedent = reputationJourPrecedent;
    }
    
    public void setDepensesDuJour(int depensesDuJour) {
        this.depensesDuJour = depensesDuJour;
    }
    
    public void setBeneficesDuJour(int beneficesDuJour) {
        this.beneficesDuJour = beneficesDuJour;
    }
    
    public void setCoutLoyerDuJour(int coutLoyerDuJour) {
        this.coutLoyerDuJour = coutLoyerDuJour;
    }
    
    public void setCoutSalairesDuJour(int coutSalairesDuJour) {
        this.coutSalairesDuJour = coutSalairesDuJour;
    }
    
    public void setRevenusCommandesDuJour(int coutCommandesDuJour) {
        this.revenusCommandesDuJour = coutCommandesDuJour;
    }
    
    public void setCoutConstructionDuJour(int coutConstructionDuJour) {
        this.coutConstructionDuJour = coutConstructionDuJour;
    }

    public void setRevenusPourboireDuJour(int pourboire) {
        this.revenusPourboireDuJour = pourboire;
    }

    public void setRevenusDuJour(int revenusDuJour) {
        this.revenusDuJour = revenusDuJour;
    }

    public void setAchatDujour(int achatDujour) {
        this.achatDujour = achatDujour;
    }
}
