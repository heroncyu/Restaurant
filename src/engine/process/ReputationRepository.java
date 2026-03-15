package engine.process;

import static config.GameConfiguration.INITIAL_REPUTATION;
import engine.prestige.Reputation;

public class ReputationRepository {
    private Reputation reputation = new Reputation(INITIAL_REPUTATION);
    private static ReputationRepository instance = new ReputationRepository();

    private ReputationRepository() {
    }

    public static ReputationRepository getInstance() {
        return instance;
    }

    public int getReputation() {
        return reputation.getScoreReputation();
    }

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