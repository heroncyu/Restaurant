package gui;

import engine.map.Map;
import engine.mobile.Client;
import engine.mobile.Cuisinier;
import engine.mobile.Meuble;
import engine.mobile.Serveur;
import engine.prestige.Succes;
import engine.process.RestaurantManager;
import engine.process.Simulation;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau qui s'occupe de dessiner tout le jeu (les murs, les personnages, les meubles).
 * 
 * Il appelle la classe PaintStrategy pour faire le vrai dessin des éléments de la Simulation.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class GameDisplay extends JPanel {
    private Map map;
    private Simulation simulation;
    private PaintStrategy paintStrategy = new PaintStrategy();
    private int animationTick;
    private Succes succesEnCours = null;
    private int tempsRestants = 0;

    /**
     * Prépare la zone de dessin.
     * 
     * @param map la carte du restaurant (les cases)
     * @param simulation la simulation qui contient tous les éléments (serveurs, tables...)
     */
    public GameDisplay(Map map, Simulation simulation) {
        this.map = map;
        this.simulation = simulation;
        this.animationTick = 0;
    }
    /**
     * Affiche une alerte à l'écran quand on débloque un succès.
     * 
     * @param s le succès gagné
     */
    public void afficherSucces(Succes s) {
        succesEnCours = s;
        tempsRestants = 30;
    }

    /**
     * Méthode appelée tout le temps par Java pour redessiner le jeu à l'écran.
     * 
     * @param g l'outil de Java pour dessiner des images et des formes
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!simulation.isStop()){
            animationTick = (animationTick + 1) % 2;
        }

        paintStrategy.paint(map, simulation.getZones(), g);

        RestaurantManager restaurantManager = simulation.getRestaurantManager();

        for (Meuble meuble : simulation.getMeubles()) {
            paintStrategy.paint(meuble, g);
        }

        for (Client client : simulation.getManager().getClients()) {
            boolean enMouvement = simulation.getManager().isClientMoving(client);
            paintStrategy.paint(client, g, animationTick, enMouvement);
        }

        for (Cuisinier cuisinier : simulation.getManager().getCuisiniers()) {
            boolean enMouvement = simulation.getManager().isCuisinierMoving(cuisinier);
            paintStrategy.paint(cuisinier, g, animationTick, enMouvement);
        }

        for (Serveur serveur : simulation.getManager().getServeurs()) {
            boolean enMouvement = simulation.getManager().isServeurMoving(serveur);
            paintStrategy.paint(serveur, g, animationTick, enMouvement);
        }

        if (restaurantManager.getConstructionMode() == 1) {
            paintStrategy.paint(g, "Mode construction : Agrandissement du terrain", restaurantManager.calculerPrixConstruction());
        }
        if (succesEnCours != null) {
            paintStrategy.paint(succesEnCours, g, getWidth(), getHeight());
            tempsRestants--;
            if (tempsRestants <= 0) {
                succesEnCours = null;
            }
        }

        if (restaurantManager.getConstructionMode() == 2) {
            paintStrategy.paint(g, "Mode construction : Placez votre nouveau meuble", 0);
            paintStrategy.paint(simulation.getBlocksOccupees(), g);
        }

        if(simulation.isAlerteStock()){
            paintStrategy.paint(g,getWidth());
        }

    }

}
