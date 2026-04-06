package test.unit;

import org.junit.Test;
import static org.junit.Assert.*;

import engine.process.SimulationUtility;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import java.util.ArrayList;

public class TestSimulationUtility {

    @Test
    public void testCalculerQualite() {
        Cuisinier cuisinier = new Cuisinier(null, 1, 100, "Chef1");
        // Niveau 1 => 100
        assertEquals("Un cuisinier de niveau 1 doit produire une qualité de 100", 100, SimulationUtility.calculerQualite(cuisinier));
        
        cuisinier.setNiveau(2); 
        assertEquals("Un cuisinier de niveau 2 doit produire une qualité de 55", 55, SimulationUtility.calculerQualite(cuisinier));
        
        cuisinier.setNiveau(3); 
        assertEquals("Un cuisinier de niveau 3 doit produire une qualité de 70", 70, SimulationUtility.calculerQualite(cuisinier));
        
        cuisinier.setNiveau(4); 
        assertEquals("Un cuisinier de niveau 4 doit produire une qualité de 85", 85, SimulationUtility.calculerQualite(cuisinier));
        
        cuisinier.setNiveau(5); 
        assertEquals("Un cuisinier de niveau 5 (ou plus) doit produire une qualité de 100", 100, SimulationUtility.calculerQualite(cuisinier));
    }

    @Test
    public void testGetNombreFoursEtTables() {
        ArrayList<Meuble> meubles = new ArrayList<>();
        assertEquals("Il doit y avoir 0 FOUR au départ", 0, SimulationUtility.getNombreFours(meubles));
        assertEquals("Il doit y avoir 0 TABLE au départ", 0, SimulationUtility.getNombreTables(meubles));
        
        Meuble table = new Meuble(null, "TABLE");
        Meuble four1 = new Meuble(null, "FOUR");
        Meuble four2 = new Meuble(null, "FOUR");
        
        meubles.add(table);
        meubles.add(four1);
        meubles.add(four2);
        
        assertEquals("Il doit y avoir exactement 2 FOURs", 2, SimulationUtility.getNombreFours(meubles));
        assertEquals("Il doit y avoir exactement 1 TABLE", 1, SimulationUtility.getNombreTables(meubles));
    }
    
    @Test
    public void testPeutAcheterCuisinier() {
        ArrayList<Meuble> meubles = new ArrayList<>();
        
        // 0 fours, 0 cuisiniers
        assertFalse("Impossible d'acheter un cuisinier sans four", SimulationUtility.peutAcheterCuisinier(meubles, 0));
        
        meubles.add(new Meuble(null, "FOUR"));
        
        // 1 four, 0 cuisinier => OK
        assertTrue("On peut acheter un cuisinier s'il y a plus de fours que de cuisiniers", SimulationUtility.peutAcheterCuisinier(meubles, 0));
        
        // 1 four, 1 cuisinier => KO
        assertFalse("Impossible d'acheter si le nb de cuisiniers est >= au nb de fours", SimulationUtility.peutAcheterCuisinier(meubles, 1));
    }
    
    @Test
    public void testPeutAcheterServeur() {
        ArrayList<Meuble> meubles = new ArrayList<>();
        
        // 0 tables, 0 serveurs -> maxServeurs = 0/2 = 0
        assertFalse("Impossible d'acheter un serveur sans tables suffisantes", SimulationUtility.peutAcheterServeur(meubles, 0));
        
        meubles.add(new Meuble(null, "TABLE")); 
        
        // 1 table = maxServeurs = 1/2 = 0
        assertFalse("Il faut 2 tables pour pouvoir avoir 1 serveur", SimulationUtility.peutAcheterServeur(meubles, 0));
        
        meubles.add(new Meuble(null, "TABLE")); 
        
        // 2 tables = maxServeurs = 2/2 = 1
        assertTrue("Avec 2 tables, on peut acheter 1 serveur", SimulationUtility.peutAcheterServeur(meubles, 0));
        assertFalse("Avec 2 tables, on ne peut pas acheter un 2ème serveur", SimulationUtility.peutAcheterServeur(meubles, 1));
    }

    @Test
    public void testGetRandomNumber() {
        int min = 5;
        int max = 10;
        for (int i = 0; i < 50; i++) {
            int random = SimulationUtility.getRandomNumber(min, max);
            assertTrue("Le nombre aléatoire doit être >= au minimum", random >= min);
            assertTrue("Le nombre aléatoire doit être <= au maximum", random <= max);
        }
    }
}
