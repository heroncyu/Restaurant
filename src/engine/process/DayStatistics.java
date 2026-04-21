package engine.process;

import config.GameConfiguration;

import java.util.HashMap;

/**
 * Classe qui calcule le résumé comptable de la journée.
 * 
 * Stocke et fournit des fonctions pour calculer les recettes, les depenses, etc.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class DayStatistics {
    private int nbJour;
    private int nbCommandesDuJour;
    private int nbCommandesTotal;
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

    private HashMap<String,Integer> ventesParRecette = new HashMap<>();

    /**
     * Initialise toutes les statistiques à zéro lors du lancement de la partie.
     */
    public DayStatistics() {
        this.nbJour = 0;
        this.nbCommandesDuJour = 0;
        this.nbCommandesTotal = 0;
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

    /**
     * Remet à zéro toutes les variables pour commencer une nouvelle journée.
     */
    public void update() {
        this.nbJour++;
        this.nbCommandesDuJour = 0;
        this.argentJourPrecedent = ArgentRepository.getInstance().getMonnaie();
        this.reputationJourPrecedent = ReputationRepository.getInstance().getReputation();
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

    /**
     * Calcule le bénéfice en faisant la différence avec l'argent de la veille.
     * 
     * @return l'argent gagné ou perdu
     */
    public int calculBenefices() {
        this.beneficesDuJour = ArgentRepository.getInstance().getMonnaie() - this.argentJourPrecedent;
        return this.beneficesDuJour;
    }

    /**
     * Calcule le total de rentrée d'argent de la journée.
     * 
     * @return l'argent total reçu
     */
    public int calculRevenus() {
        this.revenusDuJour = this.revenusCommandesDuJour + this.revenusPourboireDuJour;
        return this.revenusDuJour;
    }

    /**
     * Calcule le total des dépenses de la journée.
     * 
     * @return l'argent total dépensé
     */
    public int calculDepenses() {
        this.depensesDuJour = this.coutLoyerDuJour + this.coutSalairesDuJour + this.coutConstructionDuJour + this.achatDujour;
        return this.depensesDuJour;
    }

    /**
     * Ajoute une dépense de construction (ex: achat d'une table).
     * 
     * @param coutConstruction montant dépensé
     */
    public void addCoutConstruction(int coutConstruction) {
        this.coutConstructionDuJour += coutConstruction;
    }

    /**
     * Ajoute l'argent gagné suite à une commande servie.
     * 
     * @param coutCommandes montant perçu
     */
    public void addRevenusCommandes(int coutCommandes) {
        this.revenusCommandesDuJour += coutCommandes;
    }

    /**
     * Ajoute le salaire à payer pour les employés.
     * 
     * @param coutSalaires montant des salaires versés
     */
    public void addCoutSalaires(int coutSalaires) {
        this.coutSalairesDuJour += coutSalaires;
    }

    /**
     * Ajoute le coût du loyer de la salle du restaurant.
     * 
     * @param coutLoyer loyer payé pour les tables
     */
    public void addCoutLoyer(int coutLoyer) {
        this.coutLoyerDuJour += coutLoyer;
    }

    /**
     * Incrémente le nombre de plats servis.
     */
    public void addCommande() {
        this.nbCommandesDuJour++;
        this.nbCommandesTotal++;
    }

    /**
     * Ajoute les pourboires laissés par les clients.
     * 
     * @param pourboire montant du pourboire
     */
    public void addRevenusPourboire(int pourboire) {
        this.revenusPourboireDuJour += pourboire;
    }

    /**
     * Ajoute le coût d'achat pour des ingrédients.
     * 
     * @param achat coût en gold enlevé à la caisse
     */
    public void addAchat(int achat) {
        this.achatDujour += achat;
    }

    /**
     * Compte le nombre de fois qu'une recette a été vendue.
     * 
     * @param nomRecette nom de la recette
     */
    public void addVenteRecette(String nomRecette){
        int actuel = ventesParRecette.getOrDefault(nomRecette,0);
        ventesParRecette.put(nomRecette,actuel+1);
    }

    public int getNbJour() {
        return nbJour;
    }

    public int getNbCommandesDuJour() {
        return nbCommandesDuJour;
    }

    public int getNbCommandesTotal() {
        return nbCommandesTotal;
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

    public int getVentesRecette(String nomRecette){
        return ventesParRecette.getOrDefault(nomRecette,0);
    }

    public HashMap<String,Integer> getVentesParRecette(){
        return ventesParRecette;
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

    public void setNbJour(int nbJour) {
        this.nbJour = nbJour;
    }
}