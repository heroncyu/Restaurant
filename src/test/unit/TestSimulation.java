package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.Simulation;

public class TestSimulation {

    private Simulation simulation;

    @Before
    public void setUp() {
        // Initialisation de la simulation avant chaque test
        simulation = new Simulation();
    }

    @Test
    public void testInitialisation() {
        assertNotNull("La simulation doit avoir une map", simulation.getMap());
        assertNotNull("La simulation doit avoir des zones", simulation.getZones());
        assertNotNull("La simulation doit avoir un manager d'entités mobiles", simulation.getManager());
        assertNotNull("La simulation doit avoir un manager de restaurant", simulation.getRestaurantManager());
        assertNotNull("La simulation doit avoir un chronomètre", simulation.getChronometre());
        assertNotNull("La simulation doit avoir des meubles", simulation.getMeubles());
        assertNotNull("La simulation doit avoir des recettes", simulation.getRecettes());
    }

    @Test
    public void testConfigurationsInitiales() {
        assertFalse("La simulation ne doit pas être en pause (stop) au démarrage", simulation.isStop());
        assertEquals("La vitesse par défaut (speed multiplier) doit être 1", 1, simulation.getSpeedMultiplier());
    }
    
    @Test
    public void testMiseEnPause() {
        simulation.setStop(true);
        assertTrue("La simulation doit être en pause", simulation.isStop());
        
        simulation.setStop(false);
        assertFalse("La simulation ne doit plus être en pause", simulation.isStop());
    }
}
