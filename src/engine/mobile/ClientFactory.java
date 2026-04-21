package engine.mobile;

import engine.map.Block;

/**
 * Usine d'élaboration architecturant la venue de la clientèle.
 * 
 * Basée sur le design pattern Factory, elle permet la fabrication simplifiée
 * selon le type de {@link Client} imposé en argument.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ClientFactory {
    /**
     * Applique la probabilité ou requête logicielle à l'outil de production.
     * 
     * @param type le type String (STAR, CRITIQUE ou default)
     * @param position la source physique depuis la porte d'entrée
     * @return un sous-module ou base directe du {@link Client}
     */
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