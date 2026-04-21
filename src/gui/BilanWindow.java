package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
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
import engine.process.DayStatistics;

public class BilanWindow extends JDialog {
    private ChartPanel panelGraph;
    private JPanel panelStats;

    private DayStatistics dayStatistics;

    private JLabel labelBenefices = new JLabel("Bénéfices");
    private JLabel labelDepenses = new JLabel("Dépenses");
    private JLabel labelRevenus = new JLabel("Revenus");

    
    public BilanWindow(JFrame owner, DayStatistics dayStatistics) {
        super(owner, "Bilan du jour", true);

        this.dayStatistics = dayStatistics;

        setLayout(new BorderLayout());

        initStats();  
        initGraph();
        
        add(panelGraph, BorderLayout.CENTER);
        add(panelStats, BorderLayout.SOUTH);

        setBackground(Color.GRAY);
        setSize(1000, 800);
        setLocationRelativeTo(owner);
        setVisible(true);

    }


    public void initStats() {
        panelStats = new JPanel();
        panelStats.setLayout(new GridLayout(1, 3, 20, 0));
        panelStats.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelStats.setBackground(Color.GRAY);

        JPanel panelDepenses = new JPanel();
        JPanel panelRevenus = new JPanel();
        JPanel panelBenefices = new JPanel();

        panelDepenses.setLayout(new BoxLayout(panelDepenses, BoxLayout.Y_AXIS));
        panelRevenus.setLayout(new BoxLayout(panelRevenus, BoxLayout.Y_AXIS));
        panelBenefices.setLayout(new BoxLayout(panelBenefices, BoxLayout.Y_AXIS));

        labelDepenses.setAlignmentX(CENTER_ALIGNMENT);
        labelRevenus.setAlignmentX(CENTER_ALIGNMENT);
        labelBenefices.setAlignmentX(CENTER_ALIGNMENT);

        panelDepenses.add(labelDepenses);
        panelRevenus.add(labelRevenus);
        panelBenefices.add(labelBenefices);

        labelDepenses.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelRevenus.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelBenefices.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JTextArea textDepenses = new JTextArea(builderDepenses());
        JTextArea textRevenus = new JTextArea(builderRevenus());
        JTextArea textBenefices = new JTextArea(builderBenefices());

        textDepenses.setEditable(false);
        textRevenus.setEditable(false);
        textBenefices.setEditable(false);

        textDepenses.setFocusable(false);
        textRevenus.setFocusable(false);
        textBenefices.setFocusable(false);

        textDepenses.setBackground(Color.LIGHT_GRAY);
        textRevenus.setBackground(Color.LIGHT_GRAY);
        textBenefices.setBackground(Color.LIGHT_GRAY);

        textDepenses.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textRevenus.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textBenefices.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        panelDepenses.add(textDepenses);
        panelRevenus.add(textRevenus);
        panelBenefices.add(textBenefices);

        panelStats.add(panelDepenses);
        panelStats.add(panelRevenus);
        panelStats.add(panelBenefices);
    }

    public void initGraph() {
        JFreeChart chart = getBilanChart();
        chart.setBackgroundPaint(Color.GRAY);
        
        panelGraph = new ChartPanel(chart);
        panelGraph.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private String builderDepenses() {
        StringBuilder sb = new StringBuilder();
        sb.append("Construction: " + dayStatistics.getCoutConstructionDuJour() + "\n");
        sb.append("Salaires: " + dayStatistics.getCoutSalairesDuJour() + "\n");
        sb.append("Loyer: " + dayStatistics.getCoutLoyerDuJour  () + "\n");
        sb.append("Achats: " + dayStatistics.getAchatDujour() + "\n");
        sb.append("Dépenses totales: " + dayStatistics.calculDepenses() + "\n");
        return sb.toString();
    }

    private String builderRevenus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre de commandes: " + dayStatistics.getNbCommandesDuJour() + "\n");
        sb.append("Revenus commandes: " + dayStatistics.getRevenusCommandesDuJour() + "\n");
        sb.append("Pourboire: " + dayStatistics.getRevenusPourboireDuJour() + "\n");
        sb.append("Revenus totaux: " + dayStatistics.calculRevenus() + "\n");
        return sb.toString();
    }

    private String builderBenefices() {
        StringBuilder sb = new StringBuilder();
        sb.append("Argent possédé hier: " + dayStatistics.getArgentJourPrecedent() + "\n");
        sb.append("Argent actuel: " + ArgentRepository.getInstance().getMonnaie() + "\n");
        sb.append("Bénéfices de la journée: " + dayStatistics.calculBenefices() + "\n");
        return sb.toString();
    }

    public JFreeChart getBilanChart() {
        List<Integer> argentHistory = ArgentRepository.getInstance().getArgentHistory();
		XYSeries serie = new XYSeries("Bilan");
		for (int index = 0; index < argentHistory.size(); index++) {
			serie.add(index, argentHistory.get(index));
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(serie);

		return ChartFactory.createXYLineChart("Bilan", "Jour", "Argent possédé", dataset, PlotOrientation.VERTICAL, true, true, false);
	}

}
