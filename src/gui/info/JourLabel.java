package gui.info;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import engine.process.ArgentRepository;
import gui.PaintStrategy;

public class JourLabel extends JLabel {
    private PaintStrategy paintStrategy = new PaintStrategy();
    private ArgentRepository argentRepository = ArgentRepository.getInstance();

    public JourLabel(Font font) {
        super();
        setFont(font);
        setForeground(Color.white);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    protected void paintComponent(Graphics g) {
        paintStrategy.paint(this, g);
        setText("J-" + (argentRepository.getArgentHistory().size()));
        super.paintComponent(g);
    }

}
