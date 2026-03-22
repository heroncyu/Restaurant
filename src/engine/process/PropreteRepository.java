package engine.process;

import engine.prestige.Proprete;

public class PropreteRepository {
    private Proprete proprete = new Proprete(100);
    private static PropreteRepository instance = new PropreteRepository();

    private PropreteRepository() {
    }

    public static PropreteRepository getInstance() {
        return instance;
    }

    public int getProprete() {
        return proprete.getScoreProprete();
    }

    public void ajouterProprete(int ajout) {
        int nouvelle = getProprete() + ajout;
        if (nouvelle > 100) {
            nouvelle = 100;
        }
        if (nouvelle < 0) {
            nouvelle = 0;
        }
        proprete.setScoreProprete(nouvelle);
    }
}