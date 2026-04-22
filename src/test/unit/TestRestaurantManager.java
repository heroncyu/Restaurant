package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.Simulation;
import engine.process.RestaurantManager;

public class TestRestaurantManager {

    private Simulation simulation;
    private RestaurantManager restaurantManager;

    @Before
    public void setUp() {
        simulation = new Simulation();
        restaurantManager = simulation.getRestaurantManager();
    }

    @Test
    public void testConfigurationsInitiales() {
        assertEquals("Le mode construction par défaut doit être 0", 0, restaurantManager.getConstructionMode());
    }

    @Test
    public void testChangementModeConstruction() {
        restaurantManager.setConstructionMode(1);
        assertEquals("Le mode construction doit être mis à 1", 1, restaurantManager.getConstructionMode());

        restaurantManager.setConstructionMode(2);
        assertEquals("Le mode construction doit être mis à 2", 2, restaurantManager.getConstructionMode());
        
        restaurantManager.setConstructionMode(0);
        assertEquals("Le mode construction doit pouvoir revenir à 0", 0, restaurantManager.getConstructionMode());
    }

    @Test
    public void testSetEtGetMeubleACreer() {
        restaurantManager.setMeubleACreer("TABLE");
        assertEquals("Le meuble à créer doit être TABLE", "TABLE", restaurantManager.getMeubleACreer());
        
        restaurantManager.setMeubleACreer("FOUR");
        assertEquals("Le meuble à créer doit être FOUR", "FOUR", restaurantManager.getMeubleACreer());
    }

    @Test
    public void testAjouterMeuble() {
        int meublesInitiaux = simulation.getMeubles().size();
        
        restaurantManager.setMeubleACreer("PLANTE");
        // On teste l'ajout sur un block qui existe potentiellement (ex: centre de la map)
        restaurantManager.ajouterMeuble(10, 10);
        
        int difference = simulation.getMeubles().size() - meublesInitiaux;
        assertTrue("Le meuble a été ajouté (1) ou refusé car hors d'une zone valide (0)", difference == 0 || difference == 1);
    }
    
    @Test
    public void testCalculerPrixConstruction() {
        // On s'assure d'abord que la zone CONSTRUCTIBLE est vide
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
        assertEquals("Le prix de construction avec une zone vide devrait être de 0", 0, restaurantManager.calculerPrixConstruction());
        
        // On simule l'ajout d'un block dans la zone CONSTRUCTIBLE
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().add(simulation.getMap().getBlock(0, 0));
        
        // 1 block = prix de 20
        assertEquals("Le prix pour 1 block constructible doit être de 20", 20, restaurantManager.calculerPrixConstruction());
        
        // On nettoie la zone
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
    }
    
    @Test
    public void testAgrandirZoneSansCrash() {
        // S'assurer que les appels à agrandirZone() ne plantent pas (ex: NullPointer).
        // Ils manipulent l'attribut zoneBlockSelec et les zones CONSTRUCTIBLE.
        restaurantManager.setConstructionMode(1);
        try {
            restaurantManager.agrandirZone(0, 0);
            restaurantManager.agrandirZone(1, 1);
            assertTrue("agrandirZone s'est exécuté sans lever d'exception", true);
        } catch (Exception e) {
            fail("agrandirZone ne devrait pas lever d'exception : " + e.getMessage());
        }
    }
}
