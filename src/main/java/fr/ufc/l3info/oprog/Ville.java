package fr.ufc.l3info.oprog;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Ville implements Iterable<Station> {
    private Station[] stations;
    private Abonne[] abonnes;
    private IRegistre reg;

    public void initialiser(File f) throws IOException {

    }

    public void setStationPrincipale(Station st) {

    }

    public Station getStation(String nom) {
        return null;
    }

    public Station getStationPlusProche(double lat, double lon) {
        return null;
    }

    public Abonne creerAbonne(String nom, String RIB) {
        return null;
    }

    public Map<Abonne, Double> facturation(int mois, int annee) {
        return null;
    }

    @Override
    public Iterator<Station> iterator() {
        return null;
    }
}
