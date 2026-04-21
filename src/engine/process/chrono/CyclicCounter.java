package engine.process.chrono;

/**
 * Compteur qui boucle quand il dépasse son max ou son min.
 * 
 * Par exemple pour les minutes du chrono : après 59, ça ne fait pas 60, ça repart à 0.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class CyclicCounter extends BoundedCounter {

    /**
     * Crée le compteur avec ses valeurs.
     * 
     * @param value valeur de départ
     * @param max maximum (ex: 59 min)
     * @param min minimum (ex: 0 min)
     */
	public CyclicCounter(int value, int max, int min) {
		super(value, max, min); 
	}

    /**
     * Décrémente le compteur de 1. S'il est à 0, il repart à son max (ex: 59).
     */
	@Override
	public void decrement() {
		if (getValue() > getMin()) {
			super.decrement();
		} else {
			setValue(getMax());
		}
	}

    /**
     * Incrémente le compteur de 1. S'il dépasse le max, il revient à son min (ex: 0).
     */
	@Override
	public void increment() {
		if (getValue() < getMax()) {
			super.increment();
		} else {
			setValue(getMin());
		}
	}

    /**
     * Formate en texte le chiffre trouvé (avec le 0 devant si < 10).
     * 
     * @return texte au format chrono
     */
	@Override
	public String toString() {
		return Chronometer.transform(getValue());
	}

}
