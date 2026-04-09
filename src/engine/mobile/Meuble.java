package engine.mobile;

import engine.map.Block;

public class Meuble extends MobileElement{
    private int prix;
    private String type;

    public Meuble(Block position) {
        super(position);
    }

    public Meuble(Block position, String type){
        this(position);
        this.type = type;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
