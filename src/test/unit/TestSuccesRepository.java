package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import engine.process.SuccesRepository;
import engine.prestige.Succes;
import engine.process.DayStatistics;
import engine.process.ArgentRepository;
import engine.process.ReputationRepository;

public class TestSuccesRepository {

    private SuccesRepository repo;
    private DayStatistics stats;

    @Before
    public void setUp() {
        repo = SuccesRepository.getInstance();
        repo.reset(); // Reset global state
        
        ArgentRepository.getInstance().reset();
        ReputationRepository.getInstance().reset();
        stats = new DayStatistics();
        
        ArrayList<Succes> listeSucces = new ArrayList<>();
        listeSucces.add(new Succes("Bon debut", "Atteindre 10 commandes", 100));
        listeSucces.add(new Succes("Riche marchand", "Avoir 2000 or", 500));
        repo.setSucces(listeSucces);
    }

    @Test
    public void testAUnSuccesEnAttente() {
        assertFalse("Il ne doit pas y avoir de succès en attente au début", repo.aUnSuccesEnAttente());
        
        repo.getSucces().get(0).setEstDebloque(true);
        assertTrue("Il doit y avoir un succès en attente", repo.aUnSuccesEnAttente());
        
        repo.getSucces().get(0).setEstReclame(true);
        assertFalse("Le succès est réclamé, donc plus d'attente", repo.aUnSuccesEnAttente());
    }

    @Test
    public void testVerifierSuccesBonDebut() {
        // Simuler 10 commandes
        for(int i=0; i<10; i++) {
            stats.addCommande();
        }
        
        repo.verifierSucces(stats);
        
        Succes s = repo.getSucces().get(0);
        assertTrue("Le succès Bon debut doit être débloqué", s.isEstDebloque());
        assertNotNull("Une notification de succès doit être générée", repo.Notification());
    }

    @Test
    public void testVerifierSuccesRicheMarchand() {
        ArgentRepository.getInstance().setMonnaie(2500); // Dépasser 2000
        
        repo.verifierSucces(stats);
        
        Succes s = repo.getSucces().get(1);
        assertTrue("Le succès Riche marchand doit être débloqué", s.isEstDebloque());
        assertNotNull("Une notification de succès doit être générée", repo.Notification());
    }
}
