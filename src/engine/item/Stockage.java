package engine.item;

import java.util.HashMap;

/**
 * Stock central gérant l'inventaire des composants rattachés physiquement à la cuisine.
 * 
 * Supervise et maintient la quantité des {@link Ingredient} et valide la faisabilité via
 * le composant {@link StockRepository}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Stockage {
    private HashMap<Ingredient, Integer> ingredients = new HashMap<Ingredient, Integer>();
    
    /**
     * Initialise le registre contenant les données d'accumulation.
     * 
     * @param ingredients registre préexistant d'ingredients instanciés
     */
    public Stockage(HashMap<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Assigne la valeur d'une quantité ajoutée aux denrées correspondantes.
     * 
     * @param ingredient élément désiré
     * @param quantite le montant à intégrer au frigo
     */
    public void ajouterIngredient(Ingredient ingredient, int quantite){
        ingredients.put(ingredient, quantite);
    }

    /**
     * Soustrait proportionnellement un nombre d'objets ou ingrédients du compteur.
     * 
     * @param ingredient nourriture ciblée dans le stock
     * @param quantite montant supprimé ou rendu à zéro
     */
    public void supprimerIngredient(Ingredient ingredient, int quantite){
        if(ingredients.containsKey(ingredient)){
            int nouveauStock = ingredients.get(ingredient) - quantite;
            ingredients.put(ingredient, nouveauStock);
        }
    }

    /**
     * Pratique un dénombrement de tous les items de l'inventaire pour la somme générale.
     * 
     * @return le cumul mathématique de toutes les denrées présentes dans le stock
     */
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
