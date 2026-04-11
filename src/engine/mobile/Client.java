package engine.mobile;

import engine.map.Block;

public class Client extends MobileElement{
    private int satisfaction;


    public Client(Block position) {
        super(position);
        this.satisfaction = 100;
    }
    public Client(Block position,Boolean estStar){
        this(position);
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }


}
