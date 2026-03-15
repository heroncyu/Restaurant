package engine.item;

import java.util.HashMap;

public class Stockage {
    private HashMap<Ingredient, Integer> ingredients = new HashMap<Ingredient, Integer>();
    
    public Stockage(HashMap<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public void ajouterIngredient(Ingredient ingredient, int quantite){
        ingredients.put(ingredient, quantite);
    }

    public void supprimerIngredient(Ingredient ingredient, int quantite){
        if(ingredients.containsKey(ingredient)){
            int nouveauStock = ingredients.get(ingredient) - quantite;
            ingredients.put(ingredient, nouveauStock);
        }
    }

    public int quantiteTotale(){
        int quantiteTotale = 0;
        for (int val : ingredients.values()){
            quantiteTotale += val;
        }
        return quantiteTotale; 
    }

    public HashMap<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    
}
