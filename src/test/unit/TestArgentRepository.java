package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.ArgentRepository;
import config.GameConfiguration;

public class TestArgentRepository {

    private ArgentRepository repo;

    @Before
    public void setUp() {
        repo = ArgentRepository.getInstance();
        repo.reset();
    }

    @Test
    public void testReset() {
        repo.ajouterMonnaie(1000);
        repo.reset();
        assertEquals("Le reset doit remettre l'argent à la valeur initiale", GameConfiguration.INITIAL_MONEY, repo.getMonnaie());
    }

    @Test
    public void testAjouterEtRetirerMonnaie() {
        int base = repo.getMonnaie();
        repo.ajouterMonnaie(500);
        assertEquals("L'ajout de monnaie doit être correct", base + 500, repo.getMonnaie());
        
        repo.retirerMonnaie(200);
        assertEquals("Le retrait de monnaie doit être correct", base + 300, repo.getMonnaie());
    }
}
