package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.DayStatistics;
import engine.process.ArgentRepository;
import engine.process.ReputationRepository;

public class TestDayStatistics {

    private DayStatistics stats;

    @Before
    public void setUp() {
        ArgentRepository.getInstance().reset();
        ReputationRepository.getInstance().reset();
        stats = new DayStatistics();
    }

    @Test
    public void testCalculDepenses() {
        stats.addCoutConstruction(100);
        stats.addCoutSalaires(50);
        stats.addCoutLoyer(200);
        stats.addAchat(30);
        
        assertEquals("Les dépenses doivent être la somme des coûts", 380, stats.calculDepenses());
    }

    @Test
    public void testCalculRevenus() {
        stats.addRevenusCommandes(500);
        stats.addRevenusPourboire(25);
        
        assertEquals("Les revenus doivent être la somme des rentrées d'argent", 525, stats.calculRevenus());
    }

    @Test
    public void testCalculBenefices() {
        int argentInitial = ArgentRepository.getInstance().getMonnaie();
        
        // Simuler des gains
        ArgentRepository.getInstance().ajouterMonnaie(150);
        
        // Le bénéfice doit être l'argent actuel - l'argent initial (ici 150)
        assertEquals("Le bénéfice doit être la différence d'argent", 150, stats.calculBenefices());
    }

    @Test
    public void testUpdate() {
        stats.addCoutConstruction(100);
        stats.addCommande();
        
        stats.update();
        
        assertEquals("Après update, les dépenses du jour doivent être réinitialisées", 0, stats.getDepensesDuJour());
        assertEquals("Après update, le nombre de commandes du jour doit être réinitialisé", 0, stats.getNbCommandesDuJour());
        assertEquals("Après update, le jour doit passer à 1", 1, stats.getNbJour());
    }
}
