package engine.process;

import engine.prestige.Proprete;

/**
 * Classe gérant le niveau de propreté du restaurant.
 *
 * Le score de propreté est limité entre 0 et 100.
 *
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class PropreteRepository {
    private Proprete proprete = new Proprete(100);
    private static PropreteRepository instance = new PropreteRepository();

    /**
     * Constructeur privé pour le design pattern Singleton.
     */
    private PropreteRepository() {
    }

    public void reset() {
        proprete.setScoreProprete(100);
    }

    /**
     * Récupère l'instance unique qui gère la propreté.
     *
     * @return l'instance unique
     */
    public static PropreteRepository getInstance() {
        return instance;
    }

    /**
     * Renvoie le score de propreté actuel.
     *
     * @return un entier entre 0 et 100
     */
    public int getProprete() {
        return proprete.getScoreProprete();
    }

    /**
     * Modifie le niveau de propreté (ajoute ou retire).
     *
     * @param ajout nombre de points ajoutés ou retirés
     */
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