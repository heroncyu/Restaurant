package engine.process;

import engine.prestige.Succes;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import log.LoggerUtility;

/**
 * Classe gérant les défis réalisés par le joueur.
 * Elle sauvegarde les succès complétés pendant la partie.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class SuccesRepository {
    private static Logger logger = LoggerUtility.getLogger(SuccesRepository.class, "html");

    private ArrayList<Succes> succes = new ArrayList<>();
    private static SuccesRepository instance = new SuccesRepository();
    private ArrayList<Succes> aAfficher = new ArrayList<>();

    /**
     * Constructeur privé pour le design pattern Singleton.
     */
    private SuccesRepository() {
    }


    public void reset() {
        for (Succes s : succes) {
            s.setEstDebloque(false);
            s.setEstReclame(false);
        }
        aAfficher.clear();
    }

    /**
     * Récupère l'instance unique gérant les défis de la session.
     *
     * @return l'instance unique
     */
    public static SuccesRepository getInstance() {
        return instance;
    }

    public void setSucces(ArrayList<Succes> succes) {
        this.succes = succes;
    }

    public ArrayList<Succes> getSucces() {
        return succes;
    }

    /**
     * Indique si le joueur a complété un succès et s'il peut récupérer une récompense.
     * 
     * @return vrai s'il y a un succès en attente de récupération
     */
    public boolean aUnSuccesEnAttente() {
        for (Succes s : succes) {
            if (s.isEstDebloque() && !s.isEstReclame()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Fait apparaître à l'écran les succès récemment débloqués.
     * 
     * @return le succès à montrer au joueur
     */
    public Succes Notification() {
        if (aAfficher.isEmpty()) return null;
        return aAfficher.remove(0);
    }

    /**
     * Vérifie pendant la journée si le joueur vient de remplir les conditions d'un défi (ex: 2000 or atteints).
     * 
     * @param dayStatistics statistiques du jour pour contrôler l'évolution
     */
    public void verifierSucces(DayStatistics dayStatistics) {
        for (Succes s : succes) {
            if (!s.isEstDebloque()) {
                boolean unlock = false;
                if (s.getNom().equals("Bon debut") && dayStatistics.getNbCommandesTotal() >= 10) {
                    unlock = true;
                } else if (s.getNom().equals("Restaurant populaire") && dayStatistics.getNbCommandesTotal() >= 50) {
                    unlock = true;
                } else if (s.getNom().equals("Riche marchand") && ArgentRepository.getInstance().getMonnaie() >= 2000) {
                    unlock = true;
                } else if (s.getNom().equals("Bonne reputation") && ReputationRepository.getInstance().getReputation() >= 75) {
                    unlock = true;
                } else if (s.getNom().equals("Semaine chargee") && dayStatistics.getNbJour() >= 7) {
                    unlock = true;
                }
                
                if (unlock) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                    logger.info("Nouveau succès débloqué : " + s.getNom());
                }
            }
        }
    }
}