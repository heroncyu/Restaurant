package engine.process;

import static config.GameConfiguration.INITIAL_MONEY;

import java.util.List;
import java.util.ArrayList;

import engine.prestige.Argent;

/**
 * Classe utilisant le pattern Singleton pour gérer l'argent du joueur.
 * 
 * Elle permet de sauvegarder l'évolution de l'argent jour après jour.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class ArgentRepository {
	private Argent argent = new Argent(INITIAL_MONEY);
    private List<Integer> argentHistory = new ArrayList<>();
    
	private static ArgentRepository instance = new ArgentRepository();

    public void reset() {
        argent.setMonnaie(config.GameConfiguration.INITIAL_MONEY);
        argentHistory.clear();
        argentHistory.add(config.GameConfiguration.INITIAL_MONEY);
    }


	    /**
     * Constructeur privé pour le design pattern Singleton.
     */
    private ArgentRepository() {
        argentHistory.add(INITIAL_MONEY);
	}

	    /**
     * Permet de récupérer l'instance unique d'ArgentRepository.
     * 
     * @return l'instance unique
     */
    public static ArgentRepository getInstance() {
		return instance;
	}

	public Argent getArgent() {
        return argent;
    }

    public int getMonnaie() {
        return argent.getMonnaie();
    }

    /**
     * Renvoie la quantité d'argent convertie en texte pour l'affichage graphique.
     * 
     * @return montant sous forme de String
     */
    public String getMonnaieString() {
        return String.valueOf(getMonnaie());
    }

    public void setMonnaie(int monnaie) {
        argent.setMonnaie(monnaie);
    }

    /**
     * Ajoute de l'argent à la cagnotte du joueur.
     * 
     * @param ajout montant à ajouter
     */
    public void ajouterMonnaie(int ajout) {
        setMonnaie(getMonnaie() + ajout);
    }

    /**
     * Retire de l'argent à la cagnotte pour les dépenses.
     * 
     * @param retrait montant à retirer
     */
    public void retirerMonnaie(int retrait) {
        setMonnaie(getMonnaie() - retrait);
    }

    /**
     * Sauvegarde l'argent actuel dans l'historique quand un nouveau jour commence.
     */
    public void nouveauJour() {
        argentHistory.add(getMonnaie());
    }

    public List<Integer> getArgentHistory() {
        return argentHistory;
    }
}

