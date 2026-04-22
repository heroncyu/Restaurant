package engine.process;

import java.util.ArrayList;
import java.util.HashMap;

import config.GameConfiguration;
import engine.item.Ingredient;
import engine.item.Recette;
import engine.item.Stockage;
import engine.map.Map;
import engine.map.Zone;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import engine.process.chrono.Chronometer;

/**
 * Classe qui crée tous les éléments du jeu (map, meubles, cuisiniers) au démarrage.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class GameBuilder {

    /**
     * Crée la grille principale du jeu.
     * 
     * @return la carte de jeu vide
     */
    public static Map buildMap() {
        return new Map(
                GameConfiguration.LINE_COUNT,
                GameConfiguration.COLUMN_COUNT
        );
    }

    /**
     * Divise la carte en différentes zones géographiques (CUISINE, SALLE, etc).
     * 
     * @param map la matrice de la carte
     * @return le dictionnaire des différentes zones créées
     */
    public static HashMap<String, Zone> buildZones(Map map) {
        HashMap<String, Zone> zones = new HashMap<>();
        int h = map.getLineCount();
        int bas = h - 1;

        Zone cuisine = new Zone("CUISINE");
        for (int line = 14; line <= bas-1; line++) {
            for (int col = 4; col <= 9; col++) {
                cuisine.ajouterBlock(map.getBlock(line, col));
            }
        }
        zones.put("CUISINE", cuisine);

        Zone salle = new Zone("SALLE");
        for (int line = 14; line <= bas-1; line++) {
            for (int col = 10; col <= 19; col++) {
                salle.ajouterBlock(map.getBlock(line, col));
            }
        }
        zones.put("SALLE", salle);

        Zone reserve = new Zone("RESERVE");
        for (int line = 14; line <= bas-1; line++) {
            for (int col = 0; col <= 3; col++) {
                reserve.ajouterBlock(map.getBlock(line, col));
            }
        }
        zones.put("RESERVE", reserve);

        Zone constructible = new Zone("CONSTRUCTIBLE");
        zones.put("CONSTRUCTIBLE", constructible);

        return zones;
    }

    /**
     * Pose les meubles de départ du restaurant.
     * 
     * @param map carte de base
     * @return la liste des meubles placés
     */
    public static ArrayList<Meuble> buildMeubles(Map map) {
        ArrayList<Meuble> meubles = new ArrayList<>();
        int h = map.getLineCount();

        meubles.add(new Meuble(map.getBlock(h-7, 6), "FOUR"));

        meubles.add(new Meuble(map.getBlock(h-9, 12), "TABLE"));
        meubles.add(new Meuble(map.getBlock(h-9, 15), "TABLE"));

        meubles.add(new Meuble(map.getBlock(h-2, 10), "PLANTE"));
        meubles.add(new Meuble(map.getBlock(h-2, 19), "PLANTE"));
        meubles.add(new Meuble(map.getBlock(h-10, 19), "PLANTE"));

        return meubles;
    }

    /**
     * Crée le cuisinier de départ qui travaille en cuisine.
     * 
     * @param map carte
     * @return liste contentant le premier cuisinier
     */
    public static ArrayList<Cuisinier> buildCuisiniers(Map map) {
        ArrayList<Cuisinier> cuisiniers = new ArrayList<>();
        int h = map.getLineCount();

        cuisiniers.add(new Cuisinier(map.getBlock(h-3, 5), 1, 100, "Jean"));
        return cuisiniers;
    }

    /**
     * Crée le serveur de départ pour déposer les plats.
     * 
     * @param map carte
     * @return liste contenant le premier serveur
     */
    public static ArrayList<Serveur> buildServeurs(Map map) {
        ArrayList<Serveur> serveurs = new ArrayList<>();
        int h = map.getLineCount();

        serveurs.add(new Serveur(map.getBlock(h-3, 10), 1, 50, "Marie"));
        return serveurs;
    }

    /**
     * Définit toutes les recettes du jeu.
     * 
     * @param ingredients base de données des ingrédients existants
     * @return la liste des recettes
     */
    public static ArrayList<Recette> buildRecette(ArrayList<Ingredient> ingredients) {
        ArrayList<Recette> recettes = new ArrayList<>();

        Recette burger = new Recette("Burger", 16, 15,1);
        burger.ajouterIngredient(ingredients.get(0), 1);
        burger.ajouterIngredient(ingredients.get(1), 1);
        burger.ajouterIngredient(ingredients.get(6), 1);
        recettes.add(burger);

        Recette pizza = new Recette("Pizza", 18, 20,1);
        pizza.ajouterIngredient(ingredients.get(3), 1);
        pizza.ajouterIngredient(ingredients.get(4), 1);
        pizza.ajouterIngredient(ingredients.get(2), 1);
        recettes.add(pizza);

        Recette pasta = new Recette("Pasta", 25, 10,2);
        pasta.ajouterIngredient(ingredients.get(5), 1);
        pasta.ajouterIngredient(ingredients.get(4), 1);
        pasta.ajouterIngredient(ingredients.get(6), 1);
        recettes.add(pasta);

        Recette jiJia = new Recette("Poulet frit", 30, 15,3);
        jiJia.ajouterIngredient(ingredients.get(7), 1);
        jiJia.ajouterIngredient(ingredients.get(8), 1);
        jiJia.ajouterIngredient(ingredients.get(4), 1);
        recettes.add(jiJia);

        Recette omelette = new Recette("Omelette", 20, 10, 2);
        omelette.ajouterIngredient(ingredients.get(9), 2);
        omelette.ajouterIngredient(ingredients.get(2), 1);
        omelette.ajouterIngredient(ingredients.get(11), 1);
        recettes.add(omelette);

        Recette tajine = new Recette("Tajine", 45, 20, 4);
        tajine.ajouterIngredient(ingredients.get(7), 1);
        tajine.ajouterIngredient(ingredients.get(8), 1);
        tajine.ajouterIngredient(ingredients.get(13), 1);
        tajine.ajouterIngredient(ingredients.get(0), 1);
        recettes.add(tajine);

        Recette saumon = new Recette("Saumon grille", 60, 25, 5);
        saumon.ajouterIngredient(ingredients.get(12), 1);
        saumon.ajouterIngredient(ingredients.get(13), 1);
        saumon.ajouterIngredient(ingredients.get(10), 1);
        saumon.ajouterIngredient(ingredients.get(8), 1);
        recettes.add(saumon);

        return recettes;
    }

    /**
     * Initialise le garde-manger avec 10 ingrédients de chaque type pour commencer.
     * 
     * @param ingredients types d'ingrédients possibles
     * @return l'inventaire prêt pour la partie
     */
    public static Stockage buildStockage(ArrayList<Ingredient> ingredients) {
        HashMap<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>();
        for (Ingredient ingredient : ingredients) {
            stock.put(ingredient, 10);
        }
        return new Stockage(stock);
    }

    /**
     * Crée tous les ingrédients dont les prix sont définis en dur.
     * 
     * @return liste des ingrédients disponibles
     */
    public static ArrayList<Ingredient> buildIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Tomate", 2));
        ingredients.add(new Ingredient("Pain", 3));
        ingredients.add(new Ingredient("Fromage", 3));
        ingredients.add(new Ingredient("Pate", 3));
        ingredients.add(new Ingredient("Sauce tomate", 1));
        ingredients.add(new Ingredient("Spaghetti", 3));
        ingredients.add(new Ingredient("Viande hachee", 5));
        ingredients.add(new Ingredient("Poulet", 4));
        ingredients.add(new Ingredient("Epices", 1));
        ingredients.add(new Ingredient("Oeuf", 2));
        ingredients.add(new Ingredient("Creme", 3));
        ingredients.add(new Ingredient("Champignon", 2));
        ingredients.add(new Ingredient("Saumon", 6));
        ingredients.add(new Ingredient("Citron", 2));
        return ingredients;
    }

    /**
     * Prépare le système de gestion du temps (chronomètre).
     * 
     * @return le chrono du jeu
     */
    public static Chronometer buildChronometer() {
        Chronometer chronometre = new Chronometer();
        chronometre.init();
        return chronometre;
    }

    /**
     * Définit les succès déblocables et leurs récompenses pour le joueur.
     * 
     * @return liste des succès
     */
    public static ArrayList<Succes> buildSucces() {
        ArrayList<Succes> succes = new ArrayList<>();
        succes.add(new Succes("Bon debut", "Servir 10 commandes", 150));
        succes.add(new Succes("Restaurant populaire", "Servir 50 commandes", 500));
        succes.add(new Succes("Riche marchand", "Avoir 2000 gold", 200));
        succes.add(new Succes("Bonne reputation", "Atteindre 75 de reputation", 300));
        succes.add(new Succes("Semaine chargee", "Jouer 7 jours", 400));
        return succes;
    }
}