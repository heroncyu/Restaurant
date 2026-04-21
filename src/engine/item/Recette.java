package engine.item;

import java.util.HashMap;

/**
 * Constitue un menu ou une recette d'assemblage disponible dans le restaurant.
 * 
 * C'est le nœud central définissant ce qu'un client réclame. Chaque recette possède
 * une liste d'{@link Ingredient} requis qui sera vérifiée lors de l'appel.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Recette {
    private String nom;
    private int prix;
    private int tempsPreparation;
    private int niveauRequis;
    private HashMap<Ingredient, Integer> ingredients = new HashMap<Ingredient, Integer>();

    /**
     * Construit une nouvelle recette avec ses attributs temporels et financiers.
     * 
     * @param nom appellation du plat sur la carte
     * @param prix montant facturé au client
     * @param tempsPreparation temps de confection de la recette, en tick de jeu
     * @param niveauRequis niveau d'amélioration du cuisinier exigé
     */
    public Recette(String nom, int prix, int tempsPreparation, int niveauRequis) {
        this.nom = nom;
        this.prix = prix;
        this.tempsPreparation = tempsPreparation;
        this.niveauRequis = niveauRequis;
    }

    /**
     * Incorpore un aliment dans la configuration d'ingredients du plat.
     * 
     * @param ingredient la matière première concernée
     * @param quantite le nombre d'unités de l'ingrédient quémandé
     */
    public void ajouterIngredient(Ingredient ingredient, int quantite){
        ingredients.put(ingredient, quantite);
    }

    /**
     * Exclut un nombre d'ingredients de la configuration de ce plat.
     * 
     * @param ingredient la ressource de la recette à baisser
     * @param quantite le nombre ciblé à extraire de la recette
     */
    public void supprimerIngredient(Ingredient ingredient, int quantite){
        if(ingredients.containsKey(ingredient)){
            int nouveauStock = ingredients.get(ingredient) - quantite;
            ingredients.put(ingredient, nouveauStock);
        }
    }

    /**
     * Permet de valider si la recette est prête à être exécutée compte tenu de l'expérience actuelle.
     * 
     * @param niveauCuisinier compétence de l'employé
     * @return vrai si le niveau requis pour réaliser la recette est accompli, sinon faux
     */
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
