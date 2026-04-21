package engine.process.chrono;

import config.GameConfiguration;

/**
 * Classe gérant le temps et l'horloge du jeu.
 * 
 * Elle compte les heures et minutes et dit quand la journée est finie.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class Chronometer {
	private CyclicCounter hour = new CyclicCounter(0, GameConfiguration.END_OF_DAY_HOUR, 10);
	private CyclicCounter minute = new CyclicCounter(0, 59, 0);

    /**
     * Avance l'horloge d'une minute (et change l'heure si on passe 59).
     */
    public void increment() {
		minute.increment();
		if (minute.getValue() == 0) {
			hour.increment();
		}
	}

    /**
     * Recule l'horloge d'une minute.
     */
    public void decrement() {
		minute.decrement();
		if (minute.getValue() == 59) {
			hour.decrement();
		}
	}

	public CyclicCounter getHour() {
		return hour;
	}

	public CyclicCounter getMinute() {
		return minute;
	}

    /**
     * Affiche l'heure au format lisible.
     * 
     * @return texte au format "10 : 05"
     */
    public String toString() {
		return hour.toString() + " : " + minute.toString();
	}

    /**
     * Rajoute un '0' pour que "9" minutes devienne "09" minutes (pour faire joli).
     * 
     * @param value le nombre à vérifier
     * @return texte avec ou sans 0 devant
     */
    public static String transform(int value) {
		String result = "";
		if (value < 10) {
			result = "0" + value;
		} else {
			result = String.valueOf(value);
		}
		return result;
	}

    /**
     * Remet l'horloge à zéro (10h00) pour un nouveau jour.
     */
    public void init() {
		hour.setValue(10);
		minute.setValue(0);
	}

}
