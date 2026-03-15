package engine.process;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    public static void afficherZoneConstructible(List<Block> voisinsConstructibles, HashMap<String, Zone> zones) {
        for (Block blockVoisin : voisinsConstructibles) {
            ajouterBlockDansZone(blockVoisin, null, zones.get("CONSTRUCTIBLE"));
        }
    }

    public static List<Block> getVoisinsConstructibles(Block block, HashMap<String, Zone> zones, Map map) {
        List<Block> voisinsConstructibles = new ArrayList<Block>();
        Zone zoneDuBlockVoisin;
        Block blockVoisin = map.getBlock(block.getLine() - 1, block.getColumn()); // Block du Haut
        zoneDuBlockVoisin = getZone(blockVoisin, zones);
        if (zoneDuBlockVoisin == null) {
            voisinsConstructibles.add(blockVoisin);
        }
        blockVoisin = map.getBlock(block.getLine() + 1, block.getColumn()); // Block du Bas
        zoneDuBlockVoisin = getZone(blockVoisin, zones);
        if (zoneDuBlockVoisin == null) {
            voisinsConstructibles.add(blockVoisin);
        }
        blockVoisin = map.getBlock(block.getLine(), block.getColumn() - 1); // Block de Gauche
        zoneDuBlockVoisin = getZone(blockVoisin, zones);
        if (zoneDuBlockVoisin == null) {
            voisinsConstructibles.add(blockVoisin);
        }
        blockVoisin = map.getBlock(block.getLine(), block.getColumn() + 1); // Block de Droite
        zoneDuBlockVoisin = getZone(blockVoisin, zones);
        if (zoneDuBlockVoisin == null) {
            voisinsConstructibles.add(blockVoisin);
        }
        return voisinsConstructibles;
    }
}
