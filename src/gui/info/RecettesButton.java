package gui.info;

import engine.process.SuccesRepository;

import java.awt.*;

public class RecettesButton extends InfoBouton {

    public RecettesButton() {
        super("Recettes");
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