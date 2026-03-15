package engine.mobile;

import engine.map.Block;

public class Client extends MobileElement{
    private int satisfaction;
    private Boolean isStar;


    public Client(Block position) {
        super(position);
        this.satisfaction = 100;
        this.isStar = false;
    }
    public Client(Block position,Boolean estStar){
        this(position);
        this.isStar = estStar;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Boolean getStar() {
        return isStar;
    }

    public void setStar(Boolean star) {
        isStar = star;
    }
}
