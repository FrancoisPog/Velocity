package fr.ufc.l3info.oprog;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Ville implements Iterable<Station> {
    private List<Station> stations;
    private List<Abonne> abonnes;
    private IRegistre reg;

    public Ville() {
        this.stations = new LinkedList<>();
        this.abonnes = new LinkedList<>();
        this.reg = new JRegistre();
    }

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
