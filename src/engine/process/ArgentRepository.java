package engine.process;

import static config.GameConfiguration.INITIAL_MONEY;

import java.util.List;
import java.util.ArrayList;

import engine.prestige.Argent;

public class ArgentRepository {
	private Argent argent = new Argent(INITIAL_MONEY);
    private List<Integer> argentHistory = new ArrayList<>();
    
	private static ArgentRepository instance = new ArgentRepository();


	private ArgentRepository() {
        argentHistory.add(INITIAL_MONEY);
	}

	public static ArgentRepository getInstance() {
		return instance;
	}

	public Argent getArgent() {
        return argent;
    }

    public int getMonnaie() {
        return argent.getMonnaie();
    }

    public String getMonnaieString() {
        return String.valueOf(getMonnaie());
    }

    public void setMonnaie(int monnaie) {
        argent.setMonnaie(monnaie);
    }

    public void ajouterMonnaie(int ajout) {
        setMonnaie(getMonnaie() + ajout);
    }

    public void retirerMonnaie(int retrait) {
        setMonnaie(getMonnaie() - retrait);
    }

    public void nouveauJour() {
        argentHistory.add(getMonnaie());
    }

    public List<Integer> getArgentHistory() {
        return argentHistory;
    }
}

