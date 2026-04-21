package engine.mobile;

import engine.map.Block;

/**
 * Catégorie spéciale de client ayant des standards de restauration très hauts.
 * 
 * Sa jauge d'impatience influence sa review qui affecte le {@link Reputation}.
 * 
 * @see engine.mobile.Client
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ClientCritique extends Client {
    private int impactReputation;

    /**
     * Génère un critique avec un impact de base nul qui se cumulera par la suite.
     * 
     * @param position lieu d'apparition
     */
    public ClientCritique(Block position) {
        super(position);
        this.impactReputation = 0;
    }

    public int getImpactReputation() {
        return impactReputation;
    }

    public void setImpactReputation(int impactReputation) {
        this.impactReputation = impactReputation;
    }
}