package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import engine.process.ArgentRepository;

public class GameOverWindow extends JDialog {
    private JPanel panelPrincipal = new JPanel();

    private CardLayout cardLayout = new CardLayout();
    private JPanel panelCards = new JPanel(cardLayout);
    
    private ChartPanel panelGraph;
    private JPanel panelStats = new JPanel();
    private JPanel panelRemerciement = new JPanel();
    private JPanel panelBoutons = new JPanel();

    private JLabel labelFaillite = new JLabel();
    private JTextArea textExplication = new JTextArea();
    private JLabel labelRemerciement = new JLabel();

    private JButton btnRecommencer = new JButton("Recommencer");
    private JButton btnSuivant = new JButton("Suivant");
    private JButton btnPrecedent = new JButton("Précédent");

    private HashMap<String, Integer> gameStats;

    public GameOverWindow(JFrame owner, HashMap<String, Integer> gameStats) {
        super(owner, "Fin de partie", true);

        this.gameStats = gameStats;

        setLayout(new BorderLayout());

        init();
        
        setBackground(Color.GRAY);
        setSize(1000, 800);
        setLocationRelativeTo(owner);
        setVisible(true);

    }

    private void init() {
        initBoutons();
        initRemerciement();  
        initGraph();
        initStats();

        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(panelCards, BorderLayout.CENTER);
        panelPrincipal.add(panelBoutons, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void initRemerciement() {
        labelFaillite.setText("Votre restaurant a fait faillite !!!");
        labelFaillite.setFont(new Font("comic sans ms", Font.BOLD, 20));
        labelFaillite.setHorizontalAlignment(JLabel.CENTER);

        textExplication.setText(getExplication());
        textExplication.setFont(new Font("comic sans ms", Font.BOLD, 20));
        textExplication.setEditable(false);
        textExplication.setOpaque(false);
        textExplication.setLineWrap(true);
        textExplication.setWrapStyleWord(true);

        labelRemerciement.setText("Merci d'avoir joué !");
        labelRemerciement.setFont(new Font("comic sans ms", Font.BOLD, 20));
        labelRemerciement.setHorizontalAlignment(JLabel.CENTER);

        panelRemerciement.setLayout(new GridLayout(3, 1));
        panelRemerciement.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panelRemerciement.add(labelFaillite);
        panelRemerciement.add(textExplication);
        panelRemerciement.add(labelRemerciement);

        panelCards.add(panelRemerciement, "Remerciement");
    }

    private void initStats() {
        panelStats.setLayout(new GridLayout(gameStats.size(), 2));
        for (String key : gameStats.keySet()) {
            panelStats.add(new JLabel(key));
            panelStats.add(new JLabel(gameStats.get(key).toString()));
        }

        panelCards.add(panelStats, "Stats");
    }

    private void initGraph() {
        JFreeChart chart = getBilanChart();
        chart.setBackgroundPaint(Color.GRAY);
        
        panelGraph = new ChartPanel(chart);
        panelGraph.setBorder(new EmptyBorder(20, 20, 20, 20));

        panelCards.add(panelGraph, "Graph");
    }

    private void initBoutons() {
        btnRecommencer.addActionListener(new RecommencerAction());
        btnSuivant.addActionListener(new SuivantAction());
        btnPrecedent.addActionListener(new PrecedentAction());

        panelBoutons.setLayout(new BorderLayout());
        panelBoutons.add(btnPrecedent, BorderLayout.WEST);
        panelBoutons.add(btnRecommencer, BorderLayout.CENTER);
        panelBoutons.add(btnSuivant, BorderLayout.EAST);
        
    }

    private String getExplication() {
        String explication = "Malheureusement, votre restaurant a fait faillite. En effet, vous n'avez pas su gerer votre argent et vous êtes tomber en négatif à la fin de la journée, ce qui a entrainer la fermeture de votre restaurant. \n\n";
        explication += "Mais ce n'est pas comme si vous n'avez rien accompli. En effet, vous pouvez cliquer la flèche ci-dessous pour voir les différentes statistiques de votre partie ou cliquer sur le bouton \"Recommencer\" pour commencer une nouvelle partie en retournant a l'écran titre. ";
        return explication;
    }

    private JFreeChart getBilanChart() {
        List<Integer> argentHistory = ArgentRepository.getInstance().getArgentHistory();
		XYSeries serie = new XYSeries("Bilan");
		for (int index = 0; index < argentHistory.size(); index++) {
			serie.add(index, argentHistory.get(index));
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(serie);

		return ChartFactory.createXYLineChart("Bilan Final", "Jour", "Argent possédé", dataset, PlotOrientation.VERTICAL, true, true, false);
	}

    private class RecommencerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new EcranTitreGUI();
        }
    }

    private class SuivantAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.next(panelCards);
        }
    }

    private class PrecedentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.previous(panelCards);
        }
    }
}
