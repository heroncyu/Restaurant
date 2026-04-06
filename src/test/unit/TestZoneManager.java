package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import engine.process.ZoneManager;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import config.GameConfiguration;

public class TestZoneManager {

    private HashMap<String, Zone> zones;
    private Map map;
    private Zone salle;
    private Zone cuisine;
    private Zone constructible;

    @Before
    public void setUp() {
        zones = new HashMap<>();
        
        salle = new Zone("SALLE");
        cuisine = new Zone("CUISINE");
        constructible = new Zone("CONSTRUCTIBLE");
        
        zones.put("SALLE", salle);
        zones.put("CUISINE", cuisine);
        zones.put("CONSTRUCTIBLE", constructible);
        
        map = new Map(10, 10); // Carte 10 par 10
    }

    @Test
    public void testGetZone() {
        Block b1 = map.getBlock(5, 5);
        Block b2 = map.getBlock(6, 6);
        
        salle.ajouterBlock(b1);
        
        assertEquals("Le block b1 doit se trouver dans la zone SALLE", salle, ZoneManager.getZone(b1, zones));
        assertNull("Le block b2 ne doit correspondre à aucune zone au départ", ZoneManager.getZone(b2, zones));
    }
    
    @Test
    public void testAjouterBlockDansZone() {
        Block b1 = map.getBlock(1, 1);
        salle.ajouterBlock(b1);
        
        // On teste le transfert de b1
        ZoneManager.ajouterBlockDansZone(b1, salle, cuisine);
        
        assertFalse("b1 ne doit plus appartenir à SALLE", salle.appartientBlock(b1));
        assertTrue("b1 doit maintenant appartenir à CUISINE", cuisine.appartientBlock(b1));
        
        // Ajout d'un nouveau block "libre" vers une cible constructible
        Block b2 = map.getBlock(2, 2);
        ZoneManager.ajouterBlockDansZone(b2, null, constructible);
        
        assertTrue("b2 doit bien y être ajouté", constructible.appartientBlock(b2));
    }
    
    @Test
    public void testAjouterZoneConstructible() {
        Block b1 = map.getBlock(2, 2);
        Block b2 = map.getBlock(3, 3);
        List<Block> voisins = new ArrayList<>();
        voisins.add(b1);
        voisins.add(b2);
        
        ZoneManager.ajouterZoneConstructible(voisins, zones);
        
        assertTrue("b1 a été paramétré comme constructible", constructible.appartientBlock(b1));
        assertTrue("b2 a été paramétré comme constructible", constructible.appartientBlock(b2));
    }
    
    @Test
    public void testGetVoisinsConstructiblesLigneEtColonne() {
        Block center = map.getBlock(5, 5);
        salle.ajouterBlock(center);
        
        List<Block> blocksToTest = new ArrayList<>();
        blocksToTest.add(center);
        
        // Voisins verticaux (ligne)
        List<Block> voisinsL = ZoneManager.getVoisinsConstructiblesLigne(blocksToTest, zones, map);
        assertEquals("Il doit y avoir 2 voisins verticaux sur la map en bordure libre", 2, voisinsL.size());
        assertTrue("Doit contenir le block du haut", voisinsL.contains(map.getBlock(4, 5)));
        assertTrue("Doit contenir le block du bas", voisinsL.contains(map.getBlock(6, 5)));
        
        // On rend un des voisins du haut occupé (donc non validable en constructible)
        cuisine.ajouterBlock(map.getBlock(4, 5));
        voisinsL = ZoneManager.getVoisinsConstructiblesLigne(blocksToTest, zones, map);
        assertEquals("Un seul block doit être disponible à présent verticalement", 1, voisinsL.size());
        
        // Voisins horizontaux (colonne)
        List<Block> voisinsC = ZoneManager.getVoisinsConstructiblesColonne(blocksToTest, zones, map);
        assertEquals("Il doit y avoir 2 voisins horizontaux libres", 2, voisinsC.size());
        assertTrue("Doit contenir le block de gauche", voisinsC.contains(map.getBlock(5, 4)));
        assertTrue("Doit contenir le block de droite", voisinsC.contains(map.getBlock(5, 6)));
    }
    
    @Test
    public void testGetBlockLigneEtColonne() {
        Block b1 = map.getBlock(1, 4);
        Block b2 = map.getBlock(1, 5);
        Block b3 = map.getBlock(2, 5);
        
        salle.ajouterBlock(b1);
        salle.ajouterBlock(b2);
        salle.ajouterBlock(b3);
        
        List<Block> row1 = ZoneManager.getBlockLigne(1, salle);
        assertEquals("Il doit y avoir 2 blocks sur la ligne 1", 2, row1.size());
        
        List<Block> col5 = ZoneManager.getBlockColonne(5, salle);
        assertEquals("Il doit y avoir 2 blocks sur la colonne 5", 2, col5.size());
    }
    
    @Test
    public void testCalculerLoyer() {
        // Ajoutons 3 blocks dans SALLE, 2 dans CUISINE, 5 dans CONSTRUCTIBLE
        salle.ajouterBlock(map.getBlock(1, 1));
        salle.ajouterBlock(map.getBlock(1, 2));
        salle.ajouterBlock(map.getBlock(1, 3));
        
        cuisine.ajouterBlock(map.getBlock(2, 1));
        cuisine.ajouterBlock(map.getBlock(2, 2));
        
        for (int i = 0; i < 5; i++) {
            constructible.ajouterBlock(map.getBlock(9, i));
        }
        
        // Le loyer ne s'applique pas aux zones "CONSTRUCTIBLE"
        // nbCases construites = 3 (Salle) + 2 (Cuisine) = 5
        int loyerAttendu = 5 * GameConfiguration.LOYER_PAR_CASE;
        
        assertEquals("Le calcul brut doit exclure CONSTRUCTIBLE et multiplier l'aire par le loyer/case", loyerAttendu, ZoneManager.calculerLoyer(zones));
    }
}
