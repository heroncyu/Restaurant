package gui.info;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import engine.process.Simulation;

public class InfoDisplay extends JPanel {
  private Simulation simulation;

  private static Font font = new Font("Comis Sans MS", Font.BOLD, 24);

  private MonnaieLabel goldLabel = new MonnaieLabel(font);
  private JourLabel jourLabel = new JourLabel(font);
  private TempsLabel chrono;

  private InfoBouton pauseButton = new InfoBouton("⏸");
  private InfoBouton accelererButton = new InfoBouton("⏩ x1");

  public InfoDisplay(Simulation simulation) {
    this.simulation = simulation;
    this.chrono = new TempsLabel(simulation.getChronometre(), font);

    setLayout(new BorderLayout());
    setBackground(Color.gray);

    JPanel panelGauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelGauche.setOpaque(false);
    panelGauche.add(goldLabel);

    JPanel panelDroit = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelDroit.setOpaque(false);

    pauseButton.setFont(font);
    pauseButton.addActionListener(new PauseAction());

    accelererButton.setFont(font);
    accelererButton.addActionListener(new AccelererAction());

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
        pauseButton.setText("▶");
      } else {
        pauseButton.setText("⏸");
      }
    }
  }

  private class AccelererAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (simulation.getSpeedMultiplier() == 1) {
        simulation.setSpeedMultiplier(2);
        accelererButton.setText("⏩ x2");
      } else {
        simulation.setSpeedMultiplier(1);
        accelererButton.setText("⏩ x1");
      }
    }
  }
}
