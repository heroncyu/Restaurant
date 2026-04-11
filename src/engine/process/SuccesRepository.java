package engine.process;

import engine.prestige.Succes;

import java.util.ArrayList;

public class SuccesRepository {
    private ArrayList<Succes> succes = new ArrayList<>();
    private static SuccesRepository instance = new SuccesRepository();
    private ArrayList<Succes> aAfficher = new ArrayList<>();

    private SuccesRepository() {
    }

    public static SuccesRepository getInstance() {
        return instance;
    }

    public void setSucces(ArrayList<Succes> succes) {
        this.succes = succes;
    }

    public ArrayList<Succes> getSucces() {
        return succes;
    }

    public boolean aUnSuccesEnAttente() {
        for (Succes s : succes) {
            if (s.isEstDebloque() && !s.isEstReclame()) {
                return true;
            }
        }
        return false;
    }
    public Succes Notification() {
        if (aAfficher.isEmpty()) return null;
        return aAfficher.remove(0);
    }

    public void verifierSucces(DayStatistics dayStatistics) {
        for (Succes s : succes) {
            if (!s.isEstDebloque()) {
                if (s.getNom().equals("Bon debut") && dayStatistics.getNbCommandesTotal() >= 10) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                } else if (s.getNom().equals("Restaurant populaire") && dayStatistics.getNbCommandesTotal() >= 50) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                } else if (s.getNom().equals("Riche marchand") && ArgentRepository.getInstance().getMonnaie() >= 2000) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                } else if (s.getNom().equals("Bonne reputation") && ReputationRepository.getInstance().getReputation() >= 75) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                } else if (s.getNom().equals("Semaine chargee") && dayStatistics.getNbJour() >= 7) {
                    s.setEstDebloque(true);
                    aAfficher.add(s);
                }
            }
        }
    }
}