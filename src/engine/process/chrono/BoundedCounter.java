package engine.process.chrono;

/**
 * Un compteur qui a une limite minimum et maximum.
 * 
 * Il ne peut pas aller plus haut que son max ni plus bas que son min.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class BoundedCounter extends Counter {
	private int max;
	private int min;

    /**
     * Crée le compteur avec ses limites.
     * 
     * @param value chiffre ou nombre de départ
     * @param max la valeur maximum autorisée
     * @param min la valeur minimum autorisée
     */
    public BoundedCounter(int value, int max, int min) {
		super(value);
		this.max = max;
		this.min = min;
	}

    /**
     * Baisse le compteur de 1, mais s'arrête s'il touche le min.
     */
    @Override
    public void decrement() {
		if (getValue() > min) {
			super.decrement();
		}
	}

    /**
     * Monte le compteur de 1, mais s'arrête s'il touche le max.
     */
    @Override
    public void increment() {
		if (getValue() < max) {
			super.increment();
		}
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

}
