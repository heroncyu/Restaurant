package gui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import engine.process.DayStatistics;
import engine.process.Simulation;

public class MenuDisplay extends JPanel {
	private JFrame owner;

	private Simulation simulation;
	private DayStatistics dayStatistics;

	private MenuButton buildButton = new MenuButton("Construire", 300, 200);
	private MenuButton personnelButton = new MenuButton("Personnel", 300, 200);
	private MenuButton meubleButton = new MenuButton("Meuble", 300, 200);
	private MenuButton ingredientButton = new MenuButton("Ingrédients", 300, 200);

	public MenuDisplay(JFrame owner, Simulation simulation, DayStatistics dayStatistics) {
		this.owner = owner;
		this.simulation = simulation;
		this.dayStatistics = dayStatistics;
		setLayout(new GridLayout(0, 1, 0, 100));

		setBackground(Color.GRAY);

		personnelButton.addActionListener(new PersonnelButtonAction());
		buildButton.addActionListener(new BuildButtonAction());
		ingredientButton.addActionListener(new IngredientButtonAction());

		add(buildButton);
		add(personnelButton);
		add(meubleButton);
		add(ingredientButton);
	}

	private class PersonnelButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new PersonnelMenu(owner, simulation);
		}
	}

	private class BuildButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			simulation.setConstructionModeActive(!simulation.isConstructionModeActive());
		}
	}
	private class IngredientButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new IngredientMenu(owner,dayStatistics);
		}
	}
}
