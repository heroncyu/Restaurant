package gui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import engine.process.DayStatistics;
import engine.process.RestaurantManager;
import engine.process.Simulation;
import engine.process.SimulationUtility;
import engine.process.SimulationUtility;
import gui.util.FondPanel;
import org.apache.log4j.Logger;
import log.LoggerUtility;

/**
 * Panneau situé à droite de l'écran avec les 4 gros boutons principaux.
 * 
 * Permet d'ouvrir les menus pour construire, gérer le personnel, les meubles ou le stock.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class MenuDisplay extends JPanel {
    private static Logger logger = LoggerUtility.getLogger(MenuDisplay.class, "html");

	private JFrame owner;
	private FondPanel fondPanel;

	private Simulation simulation;
	private DayStatistics dayStatistics;
	private RestaurantManager restaurantManager;

	private MenuButton buildButton = new MenuButton("Construire", 300, 200);
	private MenuButton personnelButton = new MenuButton("Personnel", 300, 200);
	private MenuButton meubleButton = new MenuButton("Meuble", 300, 200);
	private MenuButton ingredientButton = new MenuButton("Stock", 300, 200);

	private Image imgFond = SimulationUtility.lireImage("src/resources/fond_bois_moyen.png");

	/**
	 * Configure la colonne de boutons à droite.
	 * 
	 * @param owner la fenêtre principale
	 * @param simulation le jeu en cours
	 * @param dayStatistics les statistiques (pour les achats)
	 */
	public MenuDisplay(JFrame owner, Simulation simulation, DayStatistics dayStatistics) {
		this.owner = owner;
		this.simulation = simulation;
		this.dayStatistics = dayStatistics;
		this.restaurantManager = simulation.getRestaurantManager();
		this.fondPanel = new FondPanel(imgFond);

		fondPanel.setLayout(new GridLayout(0, 1, 0, 100));

		personnelButton.addActionListener(new PersonnelButtonAction());
		buildButton.addActionListener(new BuildButtonAction());
		ingredientButton.addActionListener(new IngredientButtonAction());
		meubleButton.addActionListener(new MeubleButtonAction());

		fondPanel.add(buildButton);
		fondPanel.add(personnelButton);
		fondPanel.add(meubleButton);
		fondPanel.add(ingredientButton);

		setLayout(new BorderLayout());
		add(fondPanel, BorderLayout.CENTER);
	}

	private class PersonnelButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
            logger.trace("Ouverture du menu Personnel");
			new PersonnelMenu(owner, simulation);
		}
	}

	private class BuildButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (restaurantManager.getConstructionMode() == 0) {
				restaurantManager.setConstructionMode(1);
			} else {
				restaurantManager.setConstructionMode(0);
			}
            logger.trace("Bascule du mode construction : " + restaurantManager.getConstructionMode());
		}
	}
	private class IngredientButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
            logger.trace("Ouverture du menu Stock/Ingrédients");
			new IngredientMenu(owner,dayStatistics);
		}
	}

	private class MeubleButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
            logger.trace("Ouverture du menu Meubles");
			new MeubleMenu(owner,simulation);
		}
	}
}
