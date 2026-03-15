package engine.process;

import engine.item.Ingredient;
import engine.item.Recette;
import engine.item.Stockage;

import java.util.ArrayList;

public class StockRepository {
    private Stockage stockage;
    private static StockRepository instance = new StockRepository();

    private StockRepository() {}

    public static StockRepository getInstance() {
        return instance;
    }

    public void setStockage(Stockage stockage) {
        this.stockage = stockage;
    }

    public Stockage getStockage() {
        return stockage;
    }

    public boolean stockSuffisant(Ingredient ingredient, int quantite) {
        return stockage.getIngredients().getOrDefault(ingredient, 0) >= quantite;
    }

    public void consommer(Ingredient ingredient, int quantite) {
        stockage.supprimerIngredient(ingredient, quantite);
    }

    public void approvisionner(Ingredient ingredient, int quantite) {
        int stockActuel = stockage.getIngredients().getOrDefault(ingredient, 0);
        stockage.ajouterIngredient(ingredient, stockActuel + quantite);
    }
    public boolean recetteDisponible(Recette recette) {
        boolean disponible = true;
        for (Ingredient ingredient : recette.getIngredients().keySet()) {
            int quantiteRequise = recette.getIngredients().get(ingredient);
            if (!stockSuffisant(ingredient, quantiteRequise)) {
                disponible = false;
            }
        }
        return disponible;
    }

    public void recetteUtilisee(Recette recette) {
        for (Ingredient ingredient : recette.getIngredients().keySet()) {
            int quantite = recette.getIngredients().get(ingredient);
            consommer(ingredient, quantite);
        }
    }

    public ArrayList<Recette> recettesDisponibles(ArrayList<Recette> recettes) {
        ArrayList<Recette> disponibles = new ArrayList<>();

        for (Recette recette : recettes) {
            if (recetteDisponible(recette)) {
                disponibles.add(recette);
            }
        }
        return disponibles;
    }
}
