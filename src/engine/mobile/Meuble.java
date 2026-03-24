package engine.mobile;

import engine.map.Block;

public class Meuble extends MobileElement{
    private int prix;
    private String Type;

    public Meuble(Block position) {
        super(position);
    }

    public Meuble(Block position, int prix, String type){
        this(position);
        this.prix = prix;
        this.Type = type;
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
