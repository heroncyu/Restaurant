package engine.process;

import static config.GameConfiguration.INITIAL_REPUTATION;
import engine.prestige.Reputation;

/**
 * Singleton de centralisation du niveau de renommée du jeu.
 * 
 * Affecté par les retours des {@link ClientCritique} et limite le score.
 * 
 * @see engine.prestige.Reputation
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ReputationRepository {
    private Reputation reputation = new Reputation(INITIAL_REPUTATION);
    private static ReputationRepository instance = new ReputationRepository();

    /**
     * Constructeur privé pour le design pattern Singleton.
     */
    private ReputationRepository() {
    }

    /**
     * Récupère l'instance unique de ReputationRepository.
     * 
     * @return l'instance unique
     */
    public static ReputationRepository getInstance() {
        return instance;
    }

    /**
     * Donne le score de réputation actuel.
     * 
     * @return entier compris entre 0 et 100
     */
    public int getReputation() {
        return reputation.getScoreReputation();
    }

    /**
     * Modifie la réputation tout en s'assurant de rester entre 0 et 100.
     * 
     * @param ajout positif pour gagner en réputation, négatif pour en perdre
     */
    public void ajouterReputation(int ajout) {
        int nouvelle = getReputation() + ajout;
        if (nouvelle > 100) {
            nouvelle = 100;
        }
        if (nouvelle < 0) {
            nouvelle = 0;
        }
        reputation.setScoreReputation(nouvelle);
    }
}