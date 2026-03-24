package engine.item;

import java.util.HashMap;

public class Recette {
    private String nom;
    private int prix;
    private int tempsPreparation;
    private int niveauRequis;
    private HashMap<Ingredient, Integer> ingredients = new HashMap<Ingredient, Integer>();

    public Recette(String nom, int prix, int tempsPreparation, int niveauRequis) {
        this.nom = nom;
        this.prix = prix;
        this.tempsPreparation = tempsPreparation;
        this.niveauRequis = niveauRequis;
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

    public boolean estDebloquee(int niveauCuisinier){
        return niveauCuisinier>=niveauRequis;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getTempsPreparation() {
        return tempsPreparation;
    }

    public int getNiveauRequis(){
        return this.niveauRequis;
    }

    public void setTempsPreparation(int tempsPreparation) {
        this.tempsPreparation = tempsPreparation;
    }

    public HashMap<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    

     
}
