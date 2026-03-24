package engine.mobile;

import engine.map.Block;

public class Meuble extends MobileElement{
    private int prix;
    private String Type;

    public Meuble(Block position) {
        super(position);
    }

    public Meuble(Block position, String type){
        this(position);
        this.type = type;
        if (type.equals("TABLE")) {
            this.prix = 100;
        } else if (type.equals("FOUR")) {
            this.prix = 200;
        } else if (type.equals("PLANTE")) {
            this.prix = 50;
        }
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
