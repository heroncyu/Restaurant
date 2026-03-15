package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.chrono.Chronometer;
import engine.process.chrono.CyclicCounter;
import gui.PaintStrategy;

public class TempsLabel extends JLabel {
	private PaintStrategy paintStrategy = new PaintStrategy();
	private Chronometer chronometer;

	public TempsLabel(Chronometer chronometer, Font font) {
		this.chronometer = chronometer;
		updateValues();

		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		setFont(font);
		setForeground(Color.WHITE);

	}

	private void updateValues() {

		CyclicCounter heure = chronometer.getHour();
		CyclicCounter minute = chronometer.getMinute();

		String buffer = String.format("%02d:%02d", heure.getValue(), minute.getValue());
		setText(buffer);

	}

	public void paintComponent(Graphics g) {
		updateValues();
		paintStrategy.paint(this, g);
		super.paintComponent(g);
	}

}
