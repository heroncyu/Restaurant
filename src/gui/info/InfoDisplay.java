package gui.info;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import engine.process.ArgentRepository;
import engine.process.PropreteRepository;
import engine.process.Simulation;
import engine.process.SimulationUtility;
import gui.menu.RecettesMenu;
import gui.menu.SuccesMenu;
import gui.util.FondPanel;

public class InfoDisplay extends JPanel {
  private JFrame owner;

  private Simulation simulation;

  private static Font font = new Font("Comis Sans MS", Font.BOLD, 24);

  private MonnaieLabel goldLabel = new MonnaieLabel(font);
  private JourLabel jourLabel = new JourLabel(font);
  private ReputationLabel reputationLabel = new ReputationLabel();
  private PropreteLabel propreteLabel = new PropreteLabel();
  private TempsLabel chrono;

  private InfoBouton pauseButton = new InfoBouton("");
  private InfoBouton accelererButton = new InfoBouton(">> x1");
  private InfoBouton nettoyerButton = new InfoBouton("");
  private SuccesButton succesButton = new SuccesButton();
  private RecettesButton recettesButton = new RecettesButton();

  Image imgProprete = SimulationUtility.lireImage("src/resources/proprete.png");
  Image imgPause = SimulationUtility.lireImage("src/resources/pause.png");
  Image imgFond = SimulationUtility.lireImage("src/resources/fond_bois_moyen.png");

  private FondPanel fondPanel;

  public InfoDisplay(JFrame owner, Simulation simulation) {
    this.owner = owner;
    this.simulation = simulation;
    this.chrono = new TempsLabel(simulation.getChronometre(), font);
    this.fondPanel = new FondPanel(imgFond);

    setLayout(new BorderLayout());
    fondPanel.setLayout(new BorderLayout());

    JPanel panelGauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelGauche.setOpaque(false);
    panelGauche.add(goldLabel);
    panelGauche.add(reputationLabel);
    panelGauche.add(propreteLabel);

    if (imgProprete != null) {
      nettoyerButton.setIcon(new ImageIcon(imgProprete.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    }
    nettoyerButton.addActionListener(new NettoyerAction());
    panelGauche.add(nettoyerButton);

    accelererButton.setFont(font);
    accelererButton.setForeground(Color.white);
    accelererButton.addActionListener(new AccelererAction());

    JPanel panelDroit = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelDroit.setOpaque(false);

    if (imgPause != null) {
      pauseButton.setIcon(new ImageIcon(imgPause.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    }
    pauseButton.addActionListener(new PauseAction());

    succesButton.addActionListener(new SuccesAction());
    recettesButton.addActionListener(new RecettesAction());

    panelDroit.add(recettesButton);
    panelDroit.add(succesButton);
    panelDroit.add(accelererButton);
    panelDroit.add(pauseButton);
    panelDroit.add(chrono);
    panelDroit.add(jourLabel);

    fondPanel.add(panelGauche, BorderLayout.WEST);
    fondPanel.add(panelDroit, BorderLayout.EAST);

    add(fondPanel, BorderLayout.CENTER);
  }

  private class PauseAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      simulation.setStop(!simulation.isStop());
      if (simulation.isStop()) {
        pauseButton.setIcon(new ImageIcon(SimulationUtility.lireImage("src/resources/go.png").getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
      } else {
        pauseButton.setIcon(new ImageIcon(SimulationUtility.lireImage("src/resources/pause.png").getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
      }
    }
  }

  private class AccelererAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (simulation.getSpeedMultiplier() == 1) {
        simulation.setSpeedMultiplier(2);
        accelererButton.setText(">> x2");
      } else {
        simulation.setSpeedMultiplier(1);
        accelererButton.setText(">> x1");
      }
    }
  }

  private class NettoyerAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      PropreteRepository propreteRepository = PropreteRepository.getInstance();
      ArgentRepository argentRepository = ArgentRepository.getInstance();

      if (propreteRepository.getProprete() < 100 && argentRepository.getMonnaie() >= 10) {
        argentRepository.retirerMonnaie(10);
        propreteRepository.ajouterProprete(10);
      }
    }
  }

  private class SuccesAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      new SuccesMenu(owner);
    }
  }

  private class RecettesAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      new RecettesMenu(owner, simulation);
    }
  }
}