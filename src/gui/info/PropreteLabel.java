package gui.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.PropreteRepository;
import gui.PaintStrategy;

public class PropreteLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private PropreteRepository propreteRepository = PropreteRepository.getInstance();

    public PropreteLabel() {
        super();

        setFont(new Font("Segoe UI", Font.PLAIN, 20));
        setForeground(Color.BLACK);

        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        paintStrategy.paint(this, graphics);
        setText("Propreté : " + propreteRepository.getProprete() + "%");
        super.paintComponent(graphics);
    }
}