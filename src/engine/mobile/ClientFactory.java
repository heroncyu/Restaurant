package engine.mobile;

import engine.map.Block;

public class ClientFactory {
    public static Client createClient(String type, Block position) {
        switch (type) {
            case "STAR":
                return new ClientStar(position);
            case "CRITIQUE":
                return new ClientCritique(position);
            default:
                return new Client(position);
        }
    }
}