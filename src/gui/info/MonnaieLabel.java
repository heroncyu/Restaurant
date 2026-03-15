package gui.info;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ArgentRepository;
import gui.PaintStrategy;

public class MonnaieLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ArgentRepository argentRepository = ArgentRepository.getInstance();

    public MonnaieLabel(Font font) {
        super();

        setFont(font);
        setForeground(Color.YELLOW);

        setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        setOpaque(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        paintStrategy.paint(this, g);
        setText(argentRepository.getMonnaieString());
        super.paintComponent(g);
    }
}
