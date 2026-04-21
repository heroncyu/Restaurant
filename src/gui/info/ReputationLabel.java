package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ReputationRepository;
import gui.PaintStrategy;

public class ReputationLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ReputationRepository reputationRepository = ReputationRepository.getInstance();

    public ReputationLabel() {
        super();

        setFont(new Font("Segoe UI", Font.PLAIN, 20));
        setForeground(Color.BLACK);

        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        paintStrategy.paint(this, graphics);
        setText("Réputation : " + reputationRepository.getReputation());
        super.paintComponent(graphics);
    }
}
