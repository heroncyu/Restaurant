package gui.info;

import engine.process.SuccesRepository;

import java.awt.*;

public class SuccesButton extends InfoBouton {

    public SuccesButton() {
        super("Succès");
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (SuccesRepository.getInstance().aUnSuccesEnAttente()) {
            setForeground(Color.ORANGE);
        } else {
            setForeground(Color.WHITE);
        }
        super.paintComponent(g);
    }
}