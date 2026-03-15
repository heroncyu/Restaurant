package engine.mobile;

import engine.map.Block;

public class Meuble extends MobileElement{
    private int prix;
    private String Type;

    public Meuble(Block position) {
        super(position);
    }
    public Meuble(Block position, int prix,String name){
        this(position);
        this.prix = prix;
        this.Type = name;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
