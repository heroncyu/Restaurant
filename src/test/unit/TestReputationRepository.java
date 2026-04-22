package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.ReputationRepository;
import config.GameConfiguration;

public class TestReputationRepository {

    private ReputationRepository repo;

    @Before
    public void setUp() {
        repo = ReputationRepository.getInstance();
        repo.reset();
    }

    @Test
    public void testAjouterReputationEtLimites() {
        int initial = GameConfiguration.INITIAL_REPUTATION;
        assertEquals("La réputation initiale doit être correcte", initial, repo.getReputation());
        
        repo.ajouterReputation(20);
        assertEquals("La réputation doit augmenter", initial + 20, repo.getReputation());
        
        repo.ajouterReputation(100);
        assertEquals("La réputation ne doit pas dépasser 100", 100, repo.getReputation());
        
        repo.ajouterReputation(-150);
        assertEquals("La réputation ne doit pas être en dessous de 0", 0, repo.getReputation());
    }
}
