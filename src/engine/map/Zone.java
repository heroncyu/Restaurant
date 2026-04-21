package engine.map;
import java.util.ArrayList;
import java.util.List;

/**
 * Section logique définissant un ensemble de fonctionnalités sur la carte.
 * 
 * Regroupe un ensemble de dépendances de {@link Block}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Zone {
    private String nom;
    private List<Block> blocks;

    /**
     * Etablit une zone par son appellation.
     * 
     * @param nom étiquette de la zone
     */
    public Zone(String nom){
        this.nom=nom;
        this.blocks=new ArrayList<>();
    }

    public String getNom(){
        return this.nom;
    }
    public List<Block> getBlocks(){
        return this.blocks;
    }

    /**
     * Enregistre un bloc à la zone s'il n'y existait pas encore.
     * 
     * @param block composant ajouté à la zone
     */
    public void ajouterBlock(Block block){
        if (block !=null && !blocks.contains(block)){
                blocks.add(block);
            }

    }
    /**
     * Supprime le paramètre physique de la liste de son registre.
     * 
     * @param block le block à omettre de la zone
     */
    public void supprimerBlock(Block block){
        blocks.remove(block);
    }
    /**
     * Evalue si un point d'intérêt appartient à cette structure de zone.
     * 
     * @param block point ciblé
     * @return vrai s'il est contenu, faux dans le cas contraire
     */
    public boolean appartientBlock(Block block){
        return blocks.contains(block);
    }
}
