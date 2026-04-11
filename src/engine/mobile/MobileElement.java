package engine.mobile;

import config.GameConfiguration;
import engine.map.Block;

public abstract class MobileElement {
    private Block position;
    private String direction;

    public MobileElement(Block position) {
        this.position = position;
        this.direction = GameConfiguration.BAS;
    }

    public Block getPosition() {
        return position;
    }

    public void setPosition(Block position) {
        this.position = position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
