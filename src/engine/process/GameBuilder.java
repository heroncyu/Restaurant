package engine.process;

import java.util.ArrayList;
import java.util.HashMap;

import config.GameConfiguration;
import engine.item.Recette;
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
    public static ArrayList<Recette> buildRecette() {
        ArrayList<Recette> recettes = new ArrayList<>();

        Recette recette = new Recette("burger",10,30);
        recettes.add(recette);

        return recettes;
    }

    public static Chronometer buildChronometer() {
        Chronometer chronometre = new Chronometer();
        chronometre.init();
        return chronometre;
    }
}