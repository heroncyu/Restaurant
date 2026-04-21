package engine.mobile;

import engine.map.Block;

/**
 * Indique le comportement fondamental de l'entité client.
 * 
 * Hérite de la nature positionnable de {@link MobileElement} en y
 * ajoutant une notion de patience et de satisfaction.
 * 
 * @see engine.mobile.ClientCritique
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Client extends MobileElement{
    private int satisfaction;


    /**
     * Fait entrer le client dans le restaurant sur sa case initiale.
     * 
     * @param position la porte d'entrée par défaut
     */
    public Client(Block position) {
        super(position);
        this.satisfaction = 100;
    }
    /**
     * Invoque spécifiquement la distinction d'un client star via boolean.
     * 
     * @param position la position initiale
     * @param estStar véritable s'il s'agit d'une célébrité
     */
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
