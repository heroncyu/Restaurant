package engine.mobile;

import engine.map.Block;

/**
 * Catégorie privilégiée caractérisant l'apparition de célébrités.
 * 
 * Rapportent souvent de gros bénéfices via la caisse {@link Argent}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ClientStar extends Client {

    /**
     * Appelle le constructeur parent pour placer et formater la vedette.
     * 
     * @param position case d'accès unique
     */
    public ClientStar(Block position) {
        super(position);
    }
}