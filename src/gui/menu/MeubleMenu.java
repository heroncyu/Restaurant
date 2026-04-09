package gui.menu;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import config.GameConfiguration;
import engine.process.ArgentRepository;
import engine.process.DayStatistics;
import engine.process.RestaurantManager;
import engine.process.Simulation;
import engine.process.SimulationUtility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MeubleMenu extends JDialog {
    private Simulation simulation;
    private ArgentRepository argentRepository = ArgentRepository.getInstance();
    private DayStatistics dayStatistics;
    private RestaurantManager restaurantManager;

    private JPanel tablePanel = new JPanel();
    private JPanel fourPanel = new JPanel();
    private JPanel plantePanel = new JPanel();
    private JPanel porteManteauPanel = new JPanel();

    private JPanel acheterTablePanel = new JPanel();
    private JPanel acheterFourPanel = new JPanel();
    private JPanel acheterPlantePanel = new JPanel();
    private JPanel acheterPorteManteauPanel = new JPanel();

    private JButton acheterTable = new JButton("Acheter");
    private JButton acheterFour = new JButton("Acheter");
    private JButton acheterPlante = new JButton("Acheter");
    private JButton acheterPorteManteau = new JButton("Acheter");

    private JLabel prixTable = new JLabel("Prix : " + GameConfiguration.PRIX_TABLE + " G");
    private JLabel prixFour = new JLabel("Prix : " + GameConfiguration.PRIX_FOUR + " G");
    private JLabel prixPlante = new JLabel("Prix : " + GameConfiguration.PRIX_PLANTE + " G");
    private JLabel prixPorteManteau = new JLabel("Prix : " + GameConfiguration.PRIX_PORTE_MANTEAU + " G");

    private JLabel table = new JLabel();
    private JLabel four = new JLabel();
    private JLabel plante = new JLabel();
    private JLabel porteManteau = new JLabel();

    private JScrollPane scrollPane;



    public MeubleMenu(JFrame owner, Simulation simulation) {
        super(owner, "Menu Meuble", true);
        this.simulation = simulation;
        this.dayStatistics = simulation.getDayStatistics();
        this.restaurantManager = simulation.getRestaurantManager();

        setLayout(new GridLayout(4, 1, 0, 10));
        getContentPane().setBackground(Color.gray);
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        initLabels();
        initButtons();
        initTablePanel();
        initFourPanel();
        initPlantePanel();
        initPorteManteauPanel();

        scrollPane = new JScrollPane(getContentPane());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setContentPane(scrollPane);
        
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

    private void initPorteManteauPanel() {
        porteManteauPanel.setLayout(new GridLayout(1, 3));
        porteManteauPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        acheterPorteManteauPanel.setLayout(new GridBagLayout());
        acheterPorteManteauPanel.add(acheterPorteManteau);

        porteManteauPanel.add(porteManteau);
        porteManteauPanel.add(prixPorteManteau);
        porteManteauPanel.add(acheterPorteManteauPanel);

        add(porteManteauPanel);
    }

    private void initLabels() {
        Font font = new Font("comic sans ms", Font.BOLD, 20);

        table.setFont(font);
        four.setFont(font);
        plante.setFont(font);
        porteManteau.setFont(font);
        prixTable.setFont(font);
        prixFour.setFont(font);
        prixPlante.setFont(font);
        prixPorteManteau.setFont(font);
        acheterTable.setFont(font);
        acheterFour.setFont(font);
        acheterPlante.setFont(font);
        acheterPorteManteau.setFont(font);

        Image imgTable = SimulationUtility.lireImage("src/resources/table.png");
        if (imgTable != null) {
            table.setIcon(new ImageIcon(imgTable.getScaledInstance(175, 175, Image.SCALE_SMOOTH)));
        } else {
            table.setText("Table");
        }

        Image imgFour = SimulationUtility.lireImage("src/resources/four.png");
        if (imgFour != null) {
            four.setIcon(new ImageIcon(imgFour.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        } else {
            four.setText("Four");
        }

        Image imgPlante = SimulationUtility.lireImage("src/resources/plante.png");
        if (imgPlante != null) {
            plante.setIcon(new ImageIcon(imgPlante.getScaledInstance(175, 175, Image.SCALE_SMOOTH)));
        } else {
            plante.setText("Plante");
        }

        Image imgPorteManteau = SimulationUtility.lireImage("src/resources/porte_manteau.png");
        if (imgPorteManteau != null) {
            porteManteau.setIcon(new ImageIcon(imgPorteManteau.getScaledInstance(175, 175, Image.SCALE_SMOOTH)));
        } else {
            porteManteau.setText("Porte Manteau");
        }
        
        table.setHorizontalAlignment(JLabel.CENTER);
        four.setHorizontalAlignment(JLabel.CENTER);
        plante.setHorizontalAlignment(JLabel.CENTER);
        porteManteau.setHorizontalAlignment(JLabel.CENTER);
        prixTable.setHorizontalAlignment(JLabel.CENTER);
        prixFour.setHorizontalAlignment(JLabel.CENTER);
        prixPlante.setHorizontalAlignment(JLabel.CENTER);
        prixPorteManteau.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initButtons() {
        acheterTable.addActionListener(new AcheterTableAction());
        acheterFour.addActionListener(new AcheterFourAction());
        acheterPlante.addActionListener(new AcheterPlanteAction());
        acheterPorteManteau.addActionListener(new AcheterPorteManteauAction());

        acheterTable.setEnabled(argentRepository.getMonnaie() >= GameConfiguration.PRIX_TABLE);
        acheterFour.setEnabled(argentRepository.getMonnaie() >= GameConfiguration.PRIX_FOUR);
        acheterPlante.setEnabled(argentRepository.getMonnaie() >= GameConfiguration.PRIX_PLANTE);
        acheterPorteManteau.setEnabled(argentRepository.getMonnaie() >= GameConfiguration.PRIX_PORTE_MANTEAU);

        acheterTable.setBackground(Color.green);
        acheterFour.setBackground(Color.green);
        acheterPlante.setBackground(Color.green);
        acheterPorteManteau.setBackground(Color.green);

        acheterTable.setFocusPainted(false);
        acheterFour.setFocusPainted(false);
        acheterPlante.setFocusPainted(false);
        acheterPorteManteau.setFocusPainted(false);

        acheterTable.setContentAreaFilled(false);
        acheterFour.setContentAreaFilled(false);
        acheterPlante.setContentAreaFilled(false);
        acheterPorteManteau.setContentAreaFilled(false);

        acheterTable.setOpaque(true);
        acheterFour.setOpaque(true);
        acheterPlante.setOpaque(true);
        acheterPorteManteau.setOpaque(true);
    }

    private class AcheterTableAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            restaurantManager.setMeubleACreer("TABLE");
            restaurantManager.setConstructionMode(2);
            dispose();
            
        }
    }

     private class AcheterFourAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            restaurantManager.setMeubleACreer("FOUR");
            restaurantManager.setConstructionMode(2);
            dispose();
        }
    }

    private class AcheterPlanteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            restaurantManager.setMeubleACreer("PLANTE");
            restaurantManager.setConstructionMode(2);
            dispose();
        }
    }

    private class AcheterPorteManteauAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            restaurantManager.setMeubleACreer("PORTE_MANTEAU");
            restaurantManager.setConstructionMode(2);
            dispose();
        }
    }
}
