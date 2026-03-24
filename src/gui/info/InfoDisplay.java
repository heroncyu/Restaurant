package gui.info;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import engine.process.ArgentRepository;
import engine.process.PropreteRepository;
import engine.process.Simulation;
import gui.menu.RecettesMenu;
import gui.menu.SuccesMenu;

public class InfoDisplay extends JPanel {
  private JFrame owner;

  private Simulation simulation;

  private static Font font = new Font("Comis Sans MS", Font.BOLD, 24);

  private MonnaieLabel goldLabel = new MonnaieLabel(font);
  private JourLabel jourLabel = new JourLabel(font);
  private ReputationLabel reputationLabel = new ReputationLabel();
  private PropreteLabel propreteLabel = new PropreteLabel();
  private TempsLabel chrono;

  private InfoBouton pauseButton = new InfoBouton("Pause");
  private InfoBouton accelererButton = new InfoBouton(">> x1");
  private InfoBouton nettoyerButton = new InfoBouton("Nettoyer");
  private SuccesButton succesButton = new SuccesButton();
  private RecettesButton recettesButton = new RecettesButton();

  public InfoDisplay(JFrame owner,Simulation simulation) {
    this.simulation = simulation;
    this.chrono = new TempsLabel(simulation.getChronometre(), font);

    setLayout(new BorderLayout());
    setBackground(Color.gray);

    JPanel panelGauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelGauche.setOpaque(false);
    panelGauche.add(goldLabel);
    panelGauche.add(reputationLabel);
    panelGauche.add(propreteLabel);

    nettoyerButton.setFont(font);
    nettoyerButton.addActionListener(new NettoyerAction());
    panelGauche.add(nettoyerButton);

    JPanel panelDroit = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelDroit.setOpaque(false);

    pauseButton.setFont(font);
    pauseButton.addActionListener(new PauseAction());

    accelererButton.setFont(font);
    accelererButton.addActionListener(new AccelererAction());

    succesButton.setFont(font);
    succesButton.addActionListener(new SuccesAction());

    recettesButton.setFont(font);
    recettesButton.addActionListener(new RecettesAction());

    panelDroit.add(recettesButton);
    panelDroit.add(succesButton);
    panelDroit.add(accelererButton);
    panelDroit.add(pauseButton);
    panelDroit.add(chrono);
    panelDroit.add(jourLabel);

    add(panelGauche, BorderLayout.WEST);
    add(panelDroit, BorderLayout.EAST);
  }

  private class PauseAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      simulation.setStop(!simulation.isStop());
      if (simulation.isStop()) {
        pauseButton.setText("Go");
      } else {
        pauseButton.setText("Pause");
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
      new RecettesMenu(owner,simulation);
    }
  }
}