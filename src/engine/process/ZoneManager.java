package engine.process;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.map.Zone;
import org.apache.log4j.Logger;
import log.LoggerUtility;

/**
 * Classe gérant les zones sur le terrain (Salle, Cuisine, Reserve...).
 * 
 * Permet de découper la Map de cases d'espace en groupes distincts.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ZoneManager {
    private static Logger logger = LoggerUtility.getLogger(ZoneManager.class, "html");

    /**
     * Récupère la zone à laquelle appartient une case spécifique.
     * 
     * @param block case cherchée
     * @param zones liste des zones existantes
     * @return la zone liée à cette case, sinon null
     */
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

    /**
     * Enlève une case de son ancienne zone et l'ajoute dans une nouvelle.
     * 
     * @param block sol ciblé
     * @param zoneDuBlock source précédente (ancienne)
     * @param zoneCible destination finale (nouvelle)
     */
    public static void ajouterBlockDansZone(Block block, Zone zoneDuBlock, Zone zoneCible) {
        if (zoneDuBlock != null && zoneCible != null) {
            zoneDuBlock.supprimerBlock(block);
            zoneCible.ajouterBlock(block);
            logger.info("Transfert d'un block vers la zone : " + zoneCible.getNom());
        } else if (zoneDuBlock == null && zoneCible != null) {
            zoneCible.ajouterBlock(block);
            logger.info("Ajout d'un nouveau block dans la zone : " + zoneCible.getNom());
        } else {
            logger.warn("Échec d'ajout de block : Zone cible null");
        }
    }

    /**
     * Ajoute des tuiles dans une zone "CONSTRUCTIBLE" grisée (ce qu'on s’apprête à construire).
     * 
     * @param voisinsConstructibles série de tuiles vides/grises repérées
     * @param zones dictionnaire où il y a le groupe qu'on cherche
     */
    public static void ajouterZoneConstructible(List<Block> voisinsConstructibles, HashMap<String, Zone> zones) {
        for (Block blockVoisin : voisinsConstructibles) {
            ajouterBlockDansZone(blockVoisin, null, zones.get("CONSTRUCTIBLE"));
        }
    }

    /**
     * Regarde les cases au-dessus et en dessous de chaque case dans un mur imaginaire pour trouver des emplacements libres.
     * 
     * @param blocks tuiles en bordure du bâtiment concerné
     * @param zones dict des zones de jeu 
     * @param map plateau de la carte principale
     * @return la liste des cases "vides" prêtes pour une extension au-dessus / en-dessous
     */
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

    /**
     * Observe à gauche et droite pour y trouver des zones hors bâtiment sans autres zones (donc achetables).
     * 
     * @param blocks tuiles limites cibles
     * @param zones dictionnaire global des propriétés de terrain
     * @param map plateau de cases
     * @return zone achetables et adjacentes
     */
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
    

    /**
     * En se fixant une coordonnée Y précise (ligne), récupère toutes les cases d'une certaine zone s'y trouvant.
     * 
     * @param ligne la coordonnée statique
     * @param zone la zone filtre
     * @return tout bloc qui croise cette coordonnée sur cette ligne
     */
    public static List<Block> getBlockLigne(int ligne, Zone zone){
        List<Block> blocks = new ArrayList<Block>();
        for(Block block : zone.getBlocks()){
            if(block.getLine() == ligne){
                blocks.add(block);
            }
        }
        return blocks;
    }

    /**
     * Pareil mais basé sur croisement d'un trait X coordonnée de la matrice.
     * 
     * @param colonne coordonnée index X
     * @param zone zone ciblée
     * @return les cases trouvées de haut en bas
     */
    public static List<Block> getBlockColonne(int colonne, Zone zone){
        List<Block> blocks = new ArrayList<Block>();
        for(Block block : zone.getBlocks()){
            if(block.getColumn() == colonne){
                blocks.add(block);
            }
        }
        return blocks;
    }

    /**
     * Compte toutes les cases actives du terrain et multiplie par leur valeur pour facturer un "loyer".
     * 
     * @param zones toutes les zones du fichier 
     * @return prix total à payer dans les stats journalières
     */
    public static int calculerLoyer(HashMap<String,Zone> zones){
        int nbCases = 0;

        for(String nomZone : zones.keySet()){
            if(!nomZone.equals("CONSTRUCTIBLE")){
                nbCases += zones.get(nomZone).getBlocks().size();
            }
        }
        int loyerFinal = nbCases * GameConfiguration.LOYER_PAR_CASE;
        logger.debug("Loyer calculé pour " + nbCases + " cases : " + loyerFinal);
        return loyerFinal;
    }
}
