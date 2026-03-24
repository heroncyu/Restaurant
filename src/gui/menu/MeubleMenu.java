package gui.menu;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import engine.process.ArgentRepository;
import engine.process.DayStatistics;
import engine.process.Simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
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

    private JPanel acheterTablePanel = new JPanel();
    private JPanel acheterFourPanel = new JPanel();
    private JPanel acheterPlantePanel = new JPanel();

    private JButton acheterTable = new JButton("Acheter");
    private JButton acheterFour = new JButton("Acheter");
    private JButton acheterPlante = new JButton("Acheter");

    private JLabel prixTable = new JLabel("Prix : 100 G");
    private JLabel prixFour = new JLabel("Prix : 200 G");
    private JLabel prixPlante = new JLabel("Prix : 50 G");

    private JLabel table = new JLabel("Table");
    private JLabel four = new JLabel("Four");
    private JLabel plante = new JLabel("Plante");

    public MeubleMenu(JFrame owner, Simulation simulation) {
        super(owner, "Menu Meuble", true);
        this.simulation = simulation;
        this.dayStatistics = simulation.getDayStatistics();

        setLayout(new GridLayout(3, 1, 0, 10));
        getContentPane().setBackground(Color.gray);
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        initLabels();
        initButtons();
        initTablePanel();
        initFourPanel();
        initPlantePanel();
        
        setSize(820, 520);
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initTablePanel() {
        tablePanel.setLayout(new GridLayout(1, 3));
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        acheterTablePanel.setLayout(new GridBagLayout());
        acheterTablePanel.add(acheterTable);
        
        tablePanel.add(table);
        tablePanel.add(prixTable);
        tablePanel.add(acheterTablePanel);
        
        add(tablePanel);
    }

    private void initFourPanel() {
        fourPanel.setLayout(new GridLayout(1, 3));
        fourPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        acheterFourPanel.setLayout(new GridBagLayout());
        acheterFourPanel.add(acheterFour);

        fourPanel.add(four);
        fourPanel.add(prixFour);
        fourPanel.add(acheterFourPanel);

        add(fourPanel);
    }

    private void initPlantePanel() {
        plantePanel.setLayout(new GridLayout(1, 3));
        plantePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        acheterPlantePanel.setLayout(new GridBagLayout());
        acheterPlantePanel.add(acheterPlante);

        plantePanel.add(plante);
        plantePanel.add(prixPlante);
        plantePanel.add(acheterPlantePanel);

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
        
        table.setHorizontalAlignment(JLabel.CENTER);
        four.setHorizontalAlignment(JLabel.CENTER);
        plante.setHorizontalAlignment(JLabel.CENTER);
        prixTable.setHorizontalAlignment(JLabel.CENTER);
        prixFour.setHorizontalAlignment(JLabel.CENTER);
        prixPlante.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initButtons() {
        acheterTable.addActionListener(new AcheterTableAction());
        acheterFour.addActionListener(new AcheterFourAction());
        acheterPlante.addActionListener(new AcheterPlanteAction());

        acheterTable.setEnabled(argentRepository.getMonnaie() >= 100);
        acheterFour.setEnabled(argentRepository.getMonnaie() >= 200);
        acheterPlante.setEnabled(argentRepository.getMonnaie() >= 50);

        acheterTable.setBackground(Color.green);
        acheterFour.setBackground(Color.green);
        acheterPlante.setBackground(Color.green);

        acheterTable.setFocusPainted(false);
        acheterFour.setFocusPainted(false);
        acheterPlante.setFocusPainted(false);

        acheterTable.setContentAreaFilled(false);
        acheterFour.setContentAreaFilled(false);
        acheterPlante.setContentAreaFilled(false);

        acheterTable.setOpaque(true);
        acheterFour.setOpaque(true);
        acheterPlante.setOpaque(true);
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
