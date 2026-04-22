package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.PropreteRepository;

public class TestPropreteRepository {

    private PropreteRepository repo;

    @Before
    public void setUp() {
        repo = PropreteRepository.getInstance();
        repo.reset();
    }

    @Test
    public void testAjouterPropreteEtLimites() {
        assertEquals("La propreté initiale doit être 100", 100, repo.getProprete());
        
        repo.ajouterProprete(-20);
        assertEquals("La propreté doit diminuer", 80, repo.getProprete());
        
        repo.ajouterProprete(50);
        assertEquals("La propreté ne doit pas dépasser 100", 100, repo.getProprete());
        
        repo.ajouterProprete(-150);
        assertEquals("La propreté ne doit pas être en dessous de 0", 0, repo.getProprete());
    }
}
