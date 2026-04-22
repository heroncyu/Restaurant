package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import engine.map.Map;
import engine.map.Block;
import engine.mobile.Serveur;
import engine.mobile.Cuisinier;
import engine.mobile.Client;
import engine.mobile.Meuble;
import engine.mobile.ClientFactory;
import engine.process.MobileElementManager;
import config.GameConfiguration;

public class TestMobileElementManager {

    private Map map;
    private MobileElementManager manager;
    private Serveur serveur;
    private Cuisinier cuisinier;

    @Before
    public void setUp() {
        map = new Map(10, 10);
        
        ArrayList<Serveur> serveurs = new ArrayList<>();
        serveur = new Serveur(map.getBlock(1, 1), 1, 100, "Serveur 1");
        serveurs.add(serveur);
        
        ArrayList<Cuisinier> cuisiniers = new ArrayList<>();
        cuisinier = new Cuisinier(map.getBlock(2, 2), 1, 100, "Chef 1");
        cuisiniers.add(cuisinier);
        
        manager = new MobileElementManager(map, serveurs, cuisiniers);
    }

    @Test
    public void testInitialisation() {
        assertEquals("Il doit y avoir 1 serveur", 1, manager.getServeurs().size());
        assertEquals("Il doit y avoir 1 cuisinier", 1, manager.getCuisiniers().size());
        
        assertEquals("Le serveur doit être libre", GameConfiguration.ETAT_LIBRE, manager.getEtatServeur(serveur));
        assertEquals("Le cuisinier doit être libre", GameConfiguration.ETAT_LIBRE, manager.getEtatCuisinier(cuisinier));
    }

    @Test
    public void testMoveElementDirection() {
        // Test moveLeft
        manager.moveElementLeft(serveur);
        assertEquals("Le serveur doit s'être déplacé à gauche", map.getBlock(1, 0), serveur.getPosition());
        
        // Test moveRight
        manager.moveElementRight(serveur);
        assertEquals("Le serveur doit s'être déplacé à droite", map.getBlock(1, 1), serveur.getPosition());
        
        // Test moveTop
        manager.moveElementTop(serveur);
        assertEquals("Le serveur doit s'être déplacé en haut", map.getBlock(0, 1), serveur.getPosition());
        
        // Test moveBottom
        manager.moveElementBottom(serveur);
        assertEquals("Le serveur doit s'être déplacé en bas", map.getBlock(1, 1), serveur.getPosition());
    }
    
    @Test
    public void testMoveElementUse() {
        Block destination = map.getBlock(3, 3);
        
        // Le serveur est en (1, 1). On l'emmène en (3, 3).
        boolean arrive = manager.moveElementUse(serveur, destination);
        assertFalse("Le serveur ne doit pas encore être arrivé", arrive);
        assertEquals("Le serveur s'est déplacé d'une case vers le bas", map.getBlock(2, 1), serveur.getPosition());
        
        manager.moveElementUse(serveur, destination); // (3, 1)
        manager.moveElementUse(serveur, destination); // (3, 2)
        manager.moveElementUse(serveur, destination); // Mouvement vers (3, 3), renvoie false car il n'y était pas avant
        
        arrive = manager.moveElementUse(serveur, destination); // Maintenant il est en (3, 3), l'appel renvoie true
        
        assertTrue("Le serveur doit être arrivé à destination (confirmé au tour suivant)", arrive);
        assertEquals("Le serveur est sur la destination", destination, serveur.getPosition());
    }

    @Test
    public void testGestionTablesVides() {
        assertFalse("Au début, aucune table vide", manager.aDesTablesVides());
        
        Meuble table = new Meuble(map.getBlock(5, 5), "TABLE");
        manager.ajouterTableVide(table);
        
        assertTrue("Il y a maintenant une table vide", manager.aDesTablesVides());
        assertEquals("On doit récupérer la table vide ajoutée", table, manager.getProchaineTableVide());
    }

    @Test
    public void testGestionClients() {
        Client client = ClientFactory.createClient("NORMAL", map.getBlock(0, 0));
        Meuble table = new Meuble(map.getBlock(5, 5), "TABLE");
        
        manager.ajouterTableVide(table);
        manager.ajouterClient(client, table);
        
        assertEquals("Il doit y avoir 1 client", 1, manager.getClients().size());
        assertFalse("La table ne doit plus être dans les tables vides", manager.aDesTablesVides());
        assertEquals("Le client doit être assigné à la table", table, manager.getTableClient(client));
        
        // Libération de la table
        manager.libererTable(client);
        assertTrue("La table est de nouveau vide", manager.aDesTablesVides());
        assertNull("Le client n'a plus de table assignée", manager.getTableClient(client));
        
        // Retrait du client
        manager.retirerClient(client);
        assertEquals("Il ne doit plus y avoir de client", 0, manager.getClients().size());
    }

    @Test
    public void testChangementEtatEtServeurLibre() {
        assertNotNull("On doit trouver un serveur libre", manager.trouverServeurLibre());
        
        manager.changerEtatServeur(serveur, GameConfiguration.ETAT_VA_PRENDRE);
        assertEquals("L'état du serveur a changé", GameConfiguration.ETAT_VA_PRENDRE, manager.getEtatServeur(serveur));
        assertNull("Il n'y a plus de serveur libre", manager.trouverServeurLibre());
        
        manager.libererServeur(serveur);
        assertEquals("L'état du serveur est de nouveau libre", GameConfiguration.ETAT_LIBRE, manager.getEtatServeur(serveur));
        assertNotNull("On trouve à nouveau un serveur libre", manager.trouverServeurLibre());
    }
    
    @Test
    public void testAjouterEtLibererFour() {
        Meuble four = new Meuble(map.getBlock(9, 9), "FOUR");
        manager.ajouterFourVide(four);
        
        Meuble fourAssigne = manager.occuperProchainFour(cuisinier);
        assertEquals("Le four assigné doit être celui ajouté", four, fourAssigne);
        assertNull("Il ne doit plus y avoir de four disponible", manager.occuperProchainFour(cuisinier));
        
        manager.libererFour(cuisinier);
        assertNotNull("Le four est de nouveau disponible", manager.occuperProchainFour(cuisinier));
    }
}
