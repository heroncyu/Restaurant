package gui.menu;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.process.ArgentRepository;
import engine.process.DayStatistics;
import engine.process.Simulation;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MeubleMenu extends JDialog {
    private Simulation simulation;
    private ArgentRepository argentRepository = ArgentRepository.getInstance();
    private DayStatistics dayStatistics;

    private JPanel tablePanel = new JPanel();
    private JPanel fourPanel = new JPanel();
    private JPanel plantePanel = new JPanel();

    private JButton acheterTable = new JButton("Acheter");
    private JButton acheterFour = new JButton("Acheter");
    private JButton acheterPlante = new JButton("Acheter");

    private JLabel prixTable = new JLabel("Prix : 100");
    private JLabel prixFour = new JLabel("Prix : 200");
    private JLabel prixPlante = new JLabel("Prix : 50");

    private JLabel table = new JLabel("Table");
    private JLabel four = new JLabel("Four");
    private JLabel plante = new JLabel("Plante");

    public MeubleMenu(JFrame owner, Simulation simulation) {
        super(owner, "Menu Meuble", true);
        this.simulation = simulation;
        this.dayStatistics = simulation.getDayStatistics();

        setLayout(new GridLayout(3, 1, 0, 10));

        initLabels();
        initTablePanel();
        initFourPanel();
        initPlantePanel();
        
        setSize(820, 520);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initTablePanel() {
        tablePanel.setLayout(new GridLayout(1, 3));
        tablePanel.setBackground(Color.gray);

        

        acheterTable.addActionListener(new AcheterTableAction());

        tablePanel.add(table);
        tablePanel.add(prixTable);
        tablePanel.add(acheterTable);

        table.setAlignmentX(CENTER_ALIGNMENT);
        prixTable.setAlignmentX(CENTER_ALIGNMENT);
        acheterTable.setAlignmentX(CENTER_ALIGNMENT);
        
        add(tablePanel);
    }

    private void initFourPanel() {
        fourPanel.setLayout(new GridLayout(1, 3));
        fourPanel.setBackground(Color.gray);

        acheterFour.addActionListener(new AcheterFourAction());

        fourPanel.add(four);
        fourPanel.add(prixFour);
        fourPanel.add(acheterFour);

        four.setAlignmentX(CENTER_ALIGNMENT);
        prixFour.setAlignmentX(CENTER_ALIGNMENT);
        acheterFour.setAlignmentX(CENTER_ALIGNMENT);

        add(fourPanel);
    }

    private void initPlantePanel() {
        plantePanel.setLayout(new GridLayout(1, 3));
        plantePanel.setBackground(Color.gray);

        acheterPlante.addActionListener(new AcheterPlanteAction());

        plantePanel.add(plante);
        plantePanel.add(prixPlante);
        plantePanel.add(acheterPlante);

        plante.setAlignmentX(CENTER_ALIGNMENT);
        prixPlante.setAlignmentX(CENTER_ALIGNMENT);
        acheterPlante.setAlignmentX(CENTER_ALIGNMENT);

        add(plantePanel);
    }

    private void initLabels() {
        Font font = new Font("comic sans ms", Font.BOLD, 20);

        table.setFont(font);
        four.setFont(font);
        plante.setFont(font);
        prixTable.setFont(font);
        prixFour.setFont(font);
        prixPlante.setFont(font);
        acheterTable.setFont(font);
        acheterFour.setFont(font);
        acheterPlante.setFont(font);

        acheterFour.setBorder(new EmptyBorder(10, 10, 10, 10));
        acheterTable.setBorder(new EmptyBorder(10, 10, 10, 10));
        acheterPlante.setBorder(new EmptyBorder(10, 10, 10, 10));
        
    }

    private class AcheterTableAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            simulation.setMeubleACreer("TABLE");
            simulation.setConstructionMode(2);
            argentRepository.retirerMonnaie(100);
            dayStatistics.addAchat(100);
            dispose();
            
        }
    }

     private class AcheterFourAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            simulation.setMeubleACreer("FOUR");
            simulation.setConstructionMode(2);
            argentRepository.retirerMonnaie(200);
            dayStatistics.addAchat(200);
            dispose();
        }
    }

    private class AcheterPlanteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            simulation.setMeubleACreer("PLANTE");
            simulation.setConstructionMode(2);
            argentRepository.retirerMonnaie(50);
            dayStatistics.addAchat(50);
            dispose();
        }
    }
}
