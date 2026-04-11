package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
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
import engine.process.SimulationUtility;
import gui.util.FondPanel;

public class GameOverWindow extends JDialog {
    private Image fondImage = SimulationUtility.lireImage("src/resources/fond_bois.png");
    private FondPanel panelPrincipal = new FondPanel(fondImage);

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
        
        setSize(1000, 800);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setVisible(true);

    }

    private void init() {
        panelCards.setOpaque(false);
        panelBoutons.setOpaque(false);

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
        labelFaillite.setFont(new Font("comic sans ms", Font.BOLD, 30));
        labelFaillite.setForeground(Color.WHITE);
        labelFaillite.setHorizontalAlignment(JLabel.CENTER);

        textExplication.setText(getExplication());
        textExplication.setFont(new Font("comic sans ms", Font.BOLD, 22));
        textExplication.setForeground(Color.WHITE);
        textExplication.setEditable(false);
        textExplication.setOpaque(false);
        textExplication.setLineWrap(true);
        textExplication.setWrapStyleWord(true);
        textExplication.setBorder(new EmptyBorder(20, 20, 20, 20));

        labelRemerciement.setText("Merci d'avoir joué !");
        labelRemerciement.setFont(new Font("comic sans ms", Font.BOLD, 30));
        labelRemerciement.setForeground(Color.WHITE);
        labelRemerciement.setHorizontalAlignment(JLabel.CENTER);

        panelRemerciement.setOpaque(false);
        panelRemerciement.setLayout(new GridLayout(3, 1));
        panelRemerciement.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panelRemerciement.add(labelFaillite);
        panelRemerciement.add(textExplication);
        panelRemerciement.add(labelRemerciement);

        panelCards.add(panelRemerciement, "Remerciement");
    }

    private void initStats() {
        panelStats.setOpaque(false);
        panelStats.setLayout(new GridLayout(gameStats.size(), 2, 20, 10));
        panelStats.setBorder(new EmptyBorder(50, 50, 50, 50));

        Iterator<String> iterator = gameStats.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String labelText = key;
            
            if ("nbMeubles".equals(key)) {
                labelText = "Nombre de meuble acheter : ";
            } else if ("depenses".equals(key)) {
                labelText = "Dépenses totales : ";
            } else if ("revenus".equals(key)) {
                labelText = "Revenus totaux : ";
            } else if ("reputation".equals(key)) {
                labelText = "Réputation : ";
            } else if ("nbServeurs".equals(key)) {
                labelText = "Nombre de serveurs : ";
            } else if ("nbCuisiniers".equals(key)) {
                labelText = "Nombre de cuisiniers : ";
            } else if ("nbCommandes".equals(key)) {
                labelText = "Nombre de commandes : ";
            } else if ("achats".equals(key)) {
                labelText = "Frais d'achats : ";
            } else if ("loyers".equals(key)) {
                labelText = "Frais de loyer : ";
            } else if ("salaires".equals(key)) {
                labelText = "Salaires versés : ";
            } else if ("construction".equals(key)) {
                labelText = "Frais de construction : ";
            } else if ("pourboires".equals(key)) {
                labelText = "Pourboires gagnés : ";
            } else if ("jour".equals(key)) {
                labelText = "Jours survécus : ";
            } else {
                labelText = key + " : ";
            }

            JLabel keyLabel = new JLabel(labelText);
            keyLabel.setFont(new Font("comic sans ms", Font.BOLD, 20));
            keyLabel.setForeground(Color.WHITE);
            keyLabel.setHorizontalAlignment(JLabel.RIGHT);

            JLabel valueLabel = new JLabel(gameStats.get(key).toString());
            valueLabel.setFont(new Font("comic sans ms", Font.BOLD, 22));
            valueLabel.setForeground(Color.WHITE);
            valueLabel.setHorizontalAlignment(JLabel.LEFT);

            panelStats.add(keyLabel);
            panelStats.add(valueLabel);
        }

        panelCards.add(panelStats, "Stats");
    }

    private void initGraph() {
        JFreeChart chart = getBilanChart();
        chart.setBackgroundPaint(Color.GRAY);
        
        panelGraph = new ChartPanel(chart);
        panelGraph.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelGraph.setOpaque(false);

        panelCards.add(panelGraph, "Graph");
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("comic sans ms", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.GRAY);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
    }

    private void initBoutons() {
        btnRecommencer.addActionListener(new RecommencerAction());
        btnSuivant.addActionListener(new SuivantAction());
        btnPrecedent.addActionListener(new PrecedentAction());

        styleButton(btnRecommencer);
        styleButton(btnSuivant);
        styleButton(btnPrecedent);

        panelBoutons.setLayout(new BorderLayout(50, 0));
        panelBoutons.setBorder(new EmptyBorder(20, 50, 20, 50));

        panelBoutons.add(btnPrecedent, BorderLayout.WEST);
        panelBoutons.add(btnRecommencer, BorderLayout.CENTER);
        panelBoutons.add(btnSuivant, BorderLayout.EAST);
        
    }

    private String getExplication() {
        String explication = "Malheureusement, votre restaurant a fait faillite. En effet, vous n'avez pas su gerer votre argent et vous êtes tombé en négatif à la fin de la journée, ce qui a entrainé la fermeture de votre restaurant. \n\n";
        explication += "Mais ce n'est pas comme si vous n'aviez rien accompli. En effet, vous pouvez cliquer sur le bouton \"Suivant\" ci-dessous pour voir les différentes statistiques de votre partie ou cliquer sur le bouton \"Recommencer\" pour commencer une nouvelle partie en retournant à l'écran titre. ";
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
