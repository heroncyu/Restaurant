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
import engine.process.chrono.Chronometer;

public class GameBuilder {

    public static Map buildMap() {
        return new Map(
                GameConfiguration.LINE_COUNT,
                GameConfiguration.COLUMN_COUNT
        );
    }

    public static HashMap<String,Zone> buildZones(Map map) {
        HashMap<String,Zone> zones = new HashMap<>();

        Zone cuisine = new Zone("CUISINE");
        cuisine.ajouterBlock(map.getBlock(5, 3));
        cuisine.ajouterBlock(map.getBlock(5, 4));
        cuisine.ajouterBlock(map.getBlock(5, 5));
        cuisine.ajouterBlock(map.getBlock(6, 3));
        cuisine.ajouterBlock(map.getBlock(6, 4));
        cuisine.ajouterBlock(map.getBlock(6, 5));
        cuisine.ajouterBlock(map.getBlock(7, 3));
        cuisine.ajouterBlock(map.getBlock(7, 4));
        cuisine.ajouterBlock(map.getBlock(7, 5));
        zones.put("CUISINE",cuisine);

        Zone salle = new Zone("SALLE");
        salle.ajouterBlock(map.getBlock(5, 6));
        salle.ajouterBlock(map.getBlock(5, 7));
        salle.ajouterBlock(map.getBlock(5, 8));
        salle.ajouterBlock(map.getBlock(5, 9));
        salle.ajouterBlock(map.getBlock(6, 6));
        salle.ajouterBlock(map.getBlock(6, 7));
        salle.ajouterBlock(map.getBlock(6, 8));
        salle.ajouterBlock(map.getBlock(6, 9));
        salle.ajouterBlock(map.getBlock(7, 6));
        salle.ajouterBlock(map.getBlock(7, 7));
        salle.ajouterBlock(map.getBlock(7, 8));
        salle.ajouterBlock(map.getBlock(7, 9));
        zones.put("SALLE",salle);

        Zone reserve = new Zone("RESERVE");
        reserve.ajouterBlock(map.getBlock(5, 1));
        reserve.ajouterBlock(map.getBlock(5, 2));
        reserve.ajouterBlock(map.getBlock(6, 1));
        reserve.ajouterBlock(map.getBlock(6, 2));
        reserve.ajouterBlock(map.getBlock(7, 1));
        reserve.ajouterBlock(map.getBlock(7, 2));
        zones.put("RESERVE",reserve);

        Zone constructible = new Zone("CONSTRUCTIBLE");
        zones.put("CONSTRUCTIBLE", constructible);

        return zones;
    }

    public static ArrayList<Meuble> buildMeubles(Map map) {
        ArrayList<Meuble> meubles = new ArrayList<>();


        meubles.add(new Meuble(map.getBlock(5, 3), 200,"FOUR"));
        meubles.add(new Meuble(map.getBlock(6, 8), 200,"TABLE"));
        meubles.add(new Meuble(map.getBlock(7, 9), 200,"PLANTE"));
        meubles.add(new Meuble(map.getBlock(5, 9), 200,"PLANTE"));


        return meubles;
    }

    public static ArrayList<Cuisinier> buildCuisiniers(Map map){
        ArrayList<Cuisinier> cuisiniers = new ArrayList<>();

        cuisiniers.add(new Cuisinier(map.getBlock(6,3),1,150,"Jean"));

        return cuisiniers;
    }

    public static ArrayList<Serveur> buildServeurs(Map map) {
        ArrayList<Serveur> serveurs = new ArrayList<>();

        serveurs.add(new Serveur(map.getBlock(7,6),1,100,"Marie"));

        return serveurs;
    }
    public static ArrayList<Recette> buildRecette(ArrayList<Ingredient> ingredients) {
        ArrayList<Recette> recettes = new ArrayList<>();

        Recette burger = new Recette("Burger", 10, 15);
        burger.ajouterIngredient(ingredients.get(0), 1);
        burger.ajouterIngredient(ingredients.get(1), 1);
        burger.ajouterIngredient(ingredients.get(2), 1);
        burger.ajouterIngredient(ingredients.get(6), 1);
        recettes.add(burger);

        Recette pizza = new Recette("Pizza", 12, 20);
        pizza.ajouterIngredient(ingredients.get(3), 1);
        pizza.ajouterIngredient(ingredients.get(4), 1);
        pizza.ajouterIngredient(ingredients.get(2), 1);
        recettes.add(pizza);

        Recette pasta = new Recette("Pasta", 8, 10);
        pasta.ajouterIngredient(ingredients.get(5), 1);
        pasta.ajouterIngredient(ingredients.get(4), 1);
        pasta.ajouterIngredient(ingredients.get(6), 1);
        recettes.add(pasta);

        Recette jiJia = new Recette("Ji Jia", 13, 15);
        jiJia.ajouterIngredient(ingredients.get(7), 1);
        jiJia.ajouterIngredient(ingredients.get(8), 1);
        jiJia.ajouterIngredient(ingredients.get(4), 1);
        recettes.add(jiJia);

        return recettes;
    }

    public static Stockage buildStockage(ArrayList<Ingredient> ingredients) {
        HashMap<Ingredient, Integer> stock = new HashMap<Ingredient, Integer>();
        for (Ingredient ingredient : ingredients) {
            stock.put(ingredient, 10);
        }
        return new Stockage(stock);
    }

    public static ArrayList<Ingredient> buildIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Tomate", 5));
        ingredients.add(new Ingredient("Pain", 3));
        ingredients.add(new Ingredient("Fromage", 10));
        ingredients.add(new Ingredient("Pate", 4));
        ingredients.add(new Ingredient("Sauce tomate", 3));
        ingredients.add(new Ingredient("Spaghetti", 4));
        ingredients.add(new Ingredient("Viande hachee", 8));
        ingredients.add(new Ingredient("Poulet", 8));
        ingredients.add(new Ingredient("Epices", 3));
        return ingredients;
    }

    public static Chronometer buildChronometer() {
        Chronometer chronometre = new Chronometer();
        chronometre.init();
        return chronometre;
    }
}