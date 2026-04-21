package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;
import gui.PaintStrategy;

/**
 * L'étiquette (texte) qui affiche l'heure du jeu (ex: 08:30).
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class TempsLabel extends JLabel {
	private PaintStrategy paintStrategy = new PaintStrategy();
	private Chronometer chronometer;

	/**
	 * Prépare le style du texte de l'horloge.
	 * 
	 * @param chronometer l'horloge pour avoir les heures et minutes
	 * @param font le style d'écriture utilisé
	 */
	public TempsLabel(Chronometer chronometer, Font font) {
		this.chronometer = chronometer;
		updateValues();

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setFont(font);
		setForeground(Color.WHITE);

	}

	/**
	 * Met à jour le texte pour afficher comme une vraie montre "HH:MM".
	 */
	private void updateValues() {

		CyclicCounter heure = chronometer.getHour();
		CyclicCounter minute = chronometer.getMinute();

		String buffer = String.format("%02d:%02d", heure.getValue(), minute.getValue());
		setText(buffer);

	}

	/**
	 * Change l'heure toute seule et redessine l'étiquette.
	 * 
	 * @param g l'outil de dessin
	 */
	public void paintComponent(Graphics g) {
		updateValues();
		paintStrategy.paint(this, g);
		super.paintComponent(g);
	}

}
