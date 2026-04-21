package engine.prestige;

/**
 * Objet de donnee qui encapsule le niveau de reputation du restaurant.
 * 
 * Utilisé par {@link ReputationRepository} pour évaluer le prestige 
 * et influencer l'apparition proportionnelle des clients.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Reputation {
    private int scoreReputation;
    /**
     * Initialise un objet Reputation avec sa métrique fixe de depart.
     * 
     * @param scoreReputation niveau de depart
     */
    public Reputation(int scoreReputation) {
        this.scoreReputation = scoreReputation;
    }

    public int getScoreReputation() {
        return scoreReputation;
    }

    public void setScoreReputation(int scoreReputation) {
        this.scoreReputation = scoreReputation;
    }
}
