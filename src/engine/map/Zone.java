package engine.map;
import java.util.ArrayList;
import java.util.List;

public class Zone {
    private String nom;
    private List<Block> blocks;

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

    public void ajouterBlock(Block block){
        if (block !=null && !blocks.contains(block)){
                blocks.add(block);
            }

    }
    public void supprimerBlock(Block block){
        blocks.remove(block);
    }
    public boolean appartientBlock(Block block){
        return blocks.contains(block);
    }
}
