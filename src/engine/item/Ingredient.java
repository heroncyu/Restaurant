package engine.item;

/**
 * Représente un ingrédient de base utilisé pour les recettes.
 * 
 * Il possède un nom et un coût associé utile pour le {@link Stockage}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Ingredient {
    private String nom;
    private int prix;

    /**
     * Définit les attributs de base de l'élément comestible.
     * 
     * @param nom intitule du produit
     * @param prix somme necessaire a un rachat potentiel
     */
    public Ingredient(String nom, int prix) {
        this.nom = nom;
        this.prix = prix;
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

    
}
