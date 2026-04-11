package engine.item;
import engine.mobile.Client;

public class Commande {
private Plat plat;
private Client client;


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
