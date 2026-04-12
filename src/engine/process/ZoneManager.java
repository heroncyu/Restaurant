package engine.process;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;

public class ZoneManager {

    public static Zone getZone(Block block, HashMap<String, Zone> zones) {
        Iterator<Zone> it = zones.values().iterator();
        while (it.hasNext()) {
            Zone zone = it.next();
            if (zone.appartientBlock(block)) {
                return zone;
            }
        }
        return null;
    }

    public static void ajouterBlockDansZone(Block block, Zone zoneDuBlock, Zone zoneCible) {
        if (zoneDuBlock != null && zoneCible != null) {
            zoneDuBlock.supprimerBlock(block);
            zoneCible.ajouterBlock(block);
        } else if (zoneDuBlock == null && zoneCible != null) {
            zoneCible.ajouterBlock(block);
        }
    }

    public static void ajouterZoneConstructible(List<Block> voisinsConstructibles, HashMap<String, Zone> zones) {
        for (Block blockVoisin : voisinsConstructibles) {
            ajouterBlockDansZone(blockVoisin, null, zones.get("CONSTRUCTIBLE"));
        }
    }

    public static List<Block> getVoisinsConstructiblesLigne(List<Block> blocks, HashMap<String, Zone> zones, Map map) {
        List<Block> voisinsConstructibles = new ArrayList<Block>();
        Zone zoneDuBlockVoisin;
        int ligne;

        for(Block block : blocks){
            ligne = block.getLine();

            if (ligne - 1 >= 0) {
                Block blockVoisin = map.getBlock(block.getLine() - 1, block.getColumn()); // Block du Haut
                zoneDuBlockVoisin = getZone(blockVoisin, zones);

                if (zoneDuBlockVoisin == null) {
                    voisinsConstructibles.add(blockVoisin);
                }
            }

            if (ligne + 1 < map.getLineCount() - 1) {
                Block blockVoisin = map.getBlock(block.getLine() + 1, block.getColumn()); // Block du Bas
                zoneDuBlockVoisin = getZone(blockVoisin, zones);
                if (zoneDuBlockVoisin == null) {
                    voisinsConstructibles.add(blockVoisin);
                }
            }
        }
        return voisinsConstructibles;
    }

    public static List<Block> getVoisinsConstructiblesColonne(List<Block> blocks, HashMap<String, Zone> zones, Map map) {
        List<Block> voisinsConstructibles = new ArrayList<Block>();
        Zone zoneDuBlockVoisin;
        int colonne;

        for(Block block : blocks){
            colonne = block.getColumn();

            if (colonne - 1 >= 0) {
                Block blockVoisin = map.getBlock(block.getLine(), block.getColumn() - 1); // Block de Gauche
                zoneDuBlockVoisin = getZone(blockVoisin, zones);
                if (zoneDuBlockVoisin == null) {
                    voisinsConstructibles.add(blockVoisin);
                }
            }
            if (colonne + 1 < map.getColumnCount()) {
                Block blockVoisin = map.getBlock(block.getLine(), block.getColumn() + 1); // Block de Droite
                zoneDuBlockVoisin = getZone(blockVoisin, zones);
                if (zoneDuBlockVoisin == null) {
                    voisinsConstructibles.add(blockVoisin);
                }

            }
        }
        return voisinsConstructibles;
    }
    

    public static List<Block> getBlockLigne(int ligne, Zone zone){
        List<Block> blocks = new ArrayList<Block>();
        for(Block block : zone.getBlocks()){
            if(block.getLine() == ligne){
                blocks.add(block);
            }
        }
        return blocks;
    }

    public static List<Block> getBlockColonne(int colonne, Zone zone){
        List<Block> blocks = new ArrayList<Block>();
        for(Block block : zone.getBlocks()){
            if(block.getColumn() == colonne){
                blocks.add(block);
            }
        }
        return blocks;
    }

    public static int calculerLoyer(HashMap<String,Zone> zones){
        int nbCases = 0;

        for(String nomZone : zones.keySet()){
            if(!nomZone.equals("CONSTRUCTIBLE")){
                nbCases += zones.get(nomZone).getBlocks().size();
            }
        }
        return  nbCases * GameConfiguration.LOYER_PAR_CASE;
    }
}
