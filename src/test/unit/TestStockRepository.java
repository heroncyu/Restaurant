package test.unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

import engine.process.StockRepository;
import engine.item.Stockage;
import engine.item.Ingredient;
import engine.item.Recette;

public class TestStockRepository {

    private StockRepository repo;
    private Stockage stockage;
    private Ingredient tomate;
    private Ingredient pate;

    @Before
    public void setUp() {
        repo = StockRepository.getInstance();
        stockage = new Stockage(new HashMap<Ingredient, Integer>());
        repo.setStockage(stockage);
        repo.setNbCases(2); // 2 cases de stockage
        
        tomate = new Ingredient("Tomate", 2);
        pate = new Ingredient("Pâte", 3);
        
        stockage.ajouterIngredient(tomate, 10);
        stockage.ajouterIngredient(pate, 5);
    }

    @Test
    public void testStockSuffisant() {
        assertTrue("Le stock doit être suffisant pour 5 tomates", repo.stockSuffisant(tomate, 5));
        assertFalse("Le stock ne doit pas être suffisant pour 15 tomates", repo.stockSuffisant(tomate, 15));
    }

    @Test
    public void testConsommer() {
        repo.consommer(tomate, 3);
        assertTrue("Il doit rester 7 tomates", repo.stockSuffisant(tomate, 7));
        assertFalse("Il ne doit pas rester 8 tomates", repo.stockSuffisant(tomate, 8));
    }

    @Test
    public void testApprovisionnerLimites() {
        int capaciteMax = repo.getCapaciteMax(); // nbCases * CAPACITE_PAR_CASE
        
        repo.approvisionner(tomate, 1);
        assertTrue("La tomate doit être ajoutée", repo.stockSuffisant(tomate, 11));
        
        int quantiteDeBord = capaciteMax - 16;
        repo.approvisionner(pate, quantiteDeBord + 10); // Ne doit pas s'ajouter (trop grand)
        assertTrue("Le stock de pâte ne doit pas avoir augmenté au delà de la limite", repo.stockSuffisant(pate, 5));
    }
    
    @Test
    public void testRecetteDisponibleEtUtilisee() {
        Recette pizza = new Recette("Pizza", 10, 5, 1);
        pizza.ajouterIngredient(tomate, 2);
        pizza.ajouterIngredient(pate, 1);
        
        assertTrue("La recette Pizza doit être disponible", repo.recetteDisponible(pizza));
        
        repo.recetteUtilisee(pizza);
        
        assertTrue("Il doit rester 8 tomates", repo.stockSuffisant(tomate, 8));
        assertTrue("Il doit rester 4 pâtes", repo.stockSuffisant(pate, 4));
    }
}
