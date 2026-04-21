package engine.process.chrono;

/**
 * Base d'un système mathématique d'itération par pas.
 * 
 * Utilisé principalement par la surcouche de chronomètre et de données chronologiques.
 * 
 * @see engine.process.chrono.BoundedCounter
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Counter {
	private int value;

    /**
     * Démarre un compteur.
     * 
     * @param value la valeur de départ
     */
    public Counter(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

    /**
     * Ajoute +1 au compteur.
     */
    public void increment() {
		value++;
	}

    /**
     * Retire -1 au compteur.
     */
    public void decrement() {
		value--;
	}

	protected void setValue(int value) {
		this.value = value;
	}

}
