package engine.mobile;

import engine.map.Block;

public class ClientCritique extends Client {
    private int impactReputation;

    public ClientCritique(Block position) {
        super(position);
        this.impactReputation = 0;
    }

    public int getImpactReputation() {
        return impactReputation;
    }

    public void setImpactReputation(int impactReputation) {
        this.impactReputation = impactReputation;
    }
}