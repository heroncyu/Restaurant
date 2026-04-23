package engine.process;

import config.GameConfiguration;
import engine.item.Ingredient;
import engine.item.Recette;
import engine.item.Stockage;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import log.LoggerUtility;

/**
 * Classe gérant le stock d'ingrédients du restaurant dans des réserves.
 * 
 * Permet de vérifier les quantités, préparer un plat, acheter des ingrédients.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class StockRepository {
    private static Logger logger = LoggerUtility.getLogger(StockRepository.class, "html");

    private Stockage stockage;
    private static StockRepository instance = new StockRepository();
    private int nbCases;

    /**
     * Constructeur privé pour le design pattern Singleton.
     */
    private StockRepository() {}

    /**
     * Récupère l'instance unique qui gère le stock.
     * 
     * @return l'instance unique
     */
    public static StockRepository getInstance() {
        return instance;
    }

    public void setStockage(Stockage stockage) {
        this.stockage = stockage;
    }

    public Stockage getStockage() {
        return stockage;
    }

    /**
     * Vérifie si on a assez d'un certain ingrédient pour cuisiner.
     * 
     * @param ingredient l'ingrédient qu'on veut utiliser
     * @param quantite la quantité dont on a besoin
     * @return vrai s'il y a assez de cet ingrédient
     */
    public boolean stockSuffisant(Ingredient ingredient, int quantite) {
        return stockage.getIngredients().getOrDefault(ingredient, 0) >= quantite;
    }

    /**
     * Retire une certaine quantité d'un ingrédient du stock.
     * 
     * @param ingredient ingrédient à retirer
     * @param quantite quantité enlevée
     */
    public void consommer(Ingredient ingredient, int quantite) {
        stockage.supprimerIngredient(ingredient, quantite);
        logger.debug("Consommation : -" + quantite + " " + ingredient.getNom());
    }

    /**
     * Ajoute des ingrédients au stock après un achat, à condition que ça rentre dans les réserves.
     * 
     * @param ingredient ingrédient acheté
     * @param quantite quantité ajoutée
     */
    public void approvisionner(Ingredient ingredient, int quantite) {
        if (!peutApprovisionner(quantite)) {
            logger.warn("Approvisionnement impossible : Les réserves sont pleines !");
            return;
        }
        int stockActuel = stockage.getIngredients().getOrDefault(ingredient, 0);
        stockage.ajouterIngredient(ingredient, stockActuel + quantite);
        logger.info("Approvisionnement : +" + quantite + " " + ingredient.getNom());
    }
    /**
     * Vérifie si on a tous les ingrédients nécessaires pour préparer cette recette.
     * 
     * @param recette la recette à faire
     * @return vrai si tout est en stock
     */
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

    /**
     * Enlève du garde-manger tout ce qui est nécessaire pour préparer ce plat.
     * 
     * @param recette la recette qui a été cuisinée
     */
    public void recetteUtilisee(Recette recette) {
        for (Ingredient ingredient : recette.getIngredients().keySet()) {
            int quantite = recette.getIngredients().get(ingredient);
            consommer(ingredient, quantite);
        }
    }

    /**
     * Vérifie si on peut cuisiner au moins un repas du menu entier.
     * 
     * @param recettes la liste de tous les plats
     * @return vrai si possible de cuisiner au moins un de ces repas
     */
    public boolean auMoinsUneRecetteDisponible(ArrayList<Recette> recettes){
        return !recettesDisponibles(recettes).isEmpty();
    }

    /**
     * Renvoie la liste de toutes les recettes qui peuvent être faites avec le stock actuel.
     * 
     * @param recettes la liste des plats
     * @return les plats pour lesquels on a tous les ingrédients
     */
    public ArrayList<Recette> recettesDisponibles(ArrayList<Recette> recettes) {
        ArrayList<Recette> disponibles = new ArrayList<>();

        for (Recette recette : recettes) {
            if (recetteDisponible(recette)) {
                disponibles.add(recette);
            }
        }
        return disponibles;
    }

    public void setNbCases(int nbCases) {
        this.nbCases = nbCases;
    }

    public int getNbCases() {
        return nbCases;
    }

    /**
     * Renvoie le nombre maximum d'ingrédients qu'on peut garder toutes réserves confondues.
     * 
     * @return place totale dans les stocks
     */
    public int getCapaciteMax() {
        return nbCases * GameConfiguration.CAPACITE_PAR_CASE;
    }

    /**
     * Vérifie s'il y a assez de place de stockage vide pour y mettre nos futurs achats.
     * 
     * @param quantite quantité qu'on veut acheter
     * @return vrai s'il reste de la place
     */
    public boolean peutApprovisionner(int quantite) {
        return stockage.quantiteTotale() + quantite <= getCapaciteMax();
    }
}
