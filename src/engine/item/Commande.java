package engine.item;
import engine.mobile.Client;

/**
 * Représente la demande d'un client au sein du restaurant.
 * 
 * Accompagnée de son {@link Plat} spécifique rattaché à une {@link Recette}.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Commande {
private Plat plat;
private Client client;


/**
 * Initialise une nouvelle commande passée par le client.
 * 
 * @param client le profil de {@link Client} demandeur
 * @param recette la recette réclamée qui formera le nouveau {@link Plat}
 */
public Commande(Client client,Recette recette) {
    this.client = client;
    this.plat = new Plat(recette);
}

public Plat getPlat() {
    return plat;
}

public void setPlat(Plat plat) {
    this.plat = plat;
}

public Client getClient() {
    return client;
}

public void setClient(Client client) {
    this.client = client;
}

public String getNomRecette(){
    return getPlat().getRecette().getNom();
}
public Recette getRecette(){
        return getPlat().getRecette();
}

public int getPrixRecette(){
        return getPlat().getRecette().getPrix();
    }

}
