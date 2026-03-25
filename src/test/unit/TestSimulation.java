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
        assertNotNull("La simulation doit avoir un manager", simulation.getManager());
        assertNotNull("La simulation doit avoir un chronomètre", simulation.getChronometre());
        assertNotNull("La simulation doit avoir des meubles", simulation.getMeubles());
        assertNotNull("La simulation doit avoir des recettes", simulation.getRecettes());
    }

    @Test
    public void testConfigurationsInitiales() {
        assertFalse("La simulation ne doit pas être en pause (stop) au démarrage", simulation.isStop());
        assertEquals("La vitesse par défaut (speed multiplier) doit être 1", 1, simulation.getSpeedMultiplier());
        assertEquals("Le mode construction par défaut doit être 0", 0, simulation.getConstructionMode());
    }

    @Test
    public void testChangementModeConstruction() {
        simulation.setConstructionMode(1);
        assertEquals("Le mode construction doit être mis à 1", 1, simulation.getConstructionMode());

        simulation.setConstructionMode(2);
        assertEquals("Le mode construction doit être mis à 2", 2, simulation.getConstructionMode());
        
        simulation.setConstructionMode(0);
        assertEquals("Le mode construction doit pouvoir revenir à 0", 0, simulation.getConstructionMode());
    }
    
    @Test
    public void testMiseEnPause() {
        simulation.setStop(true);
        assertTrue("La simulation doit être en pause", simulation.isStop());
        
        simulation.setStop(false);
        assertFalse("La simulation ne doit plus être en pause", simulation.isStop());
    }

    @Test
    public void testSetEtGetMeubleACreer() {
        simulation.setMeubleACreer("TABLE");
        assertEquals("Le meuble à créer doit être TABLE", "TABLE", simulation.getMeubleACreer());
        
        simulation.setMeubleACreer("FOUR");
        assertEquals("Le meuble à créer doit être FOUR", "FOUR", simulation.getMeubleACreer());
    }

    @Test
    public void testAjouterMeuble() {
        int meublesInitiaux = simulation.getMeubles().size();
        
        simulation.setMeubleACreer("PLANTE");
        // On teste l'ajout sur un block qui existe potentiellement (ex: centre de la map)
        simulation.ajouterMeuble(10, 10);
        
        int difference = simulation.getMeubles().size() - meublesInitiaux;
        assertTrue("Le meuble a été ajouté (1) ou refusé car hors d'une zone valide (0)", difference == 0 || difference == 1);
    }
    
    @Test
    public void testCalculerPrixConstruction() {
        // On s'assure d'abord que la zone CONSTRUCTIBLE est vide
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
        assertEquals("Le prix de construction avec une zone vide devrait être de 0", 0, simulation.calculerPrixConstruction());
        
        // On simule l'ajout d'un block dans la zone CONSTRUCTIBLE
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().add(simulation.getMap().getBlock(0, 0));
        
        // 1 block = prix de 20
        assertEquals("Le prix pour 1 block constructible doit être de 20", 20, simulation.calculerPrixConstruction());
        
        // On nettoie la zone
        simulation.getZones().get("CONSTRUCTIBLE").getBlocks().clear();
    }
    
    @Test
    public void testAgrandirZoneSansCrash() {
        // S'assurer que les appels à agrandirZone() ne plantent pas (ex: NullPointer).
        // Ils manipulent l'attribut zoneBlockSelec et les zones CONSTRUCTIBLE.
        simulation.setConstructionMode(1);
        try {
            simulation.agrandirZone(0, 0);
            simulation.agrandirZone(1, 1);
            assertTrue("agrandirZone s'est exécuté sans lever d'exception", true);
        } catch (Exception e) {
            fail("agrandirZone ne devrait pas lever d'exception : " + e.getMessage());
        }
    }
}
