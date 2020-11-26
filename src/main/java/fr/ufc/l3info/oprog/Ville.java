package fr.ufc.l3info.oprog;

import fr.ufc.l3info.oprog.parser.ASTNode;
import fr.ufc.l3info.oprog.parser.ASTStationBuilder;
import fr.ufc.l3info.oprog.parser.StationParser;
import fr.ufc.l3info.oprog.parser.StationParserException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Ville implements Iterable<Station> {
    private Map<String, Station> stations;
    private Station stationPrincipale;
    private List<Abonne> abonnes;
    private IRegistre reg;

    public Ville() {
        this.stations = new HashMap<>();
        this.stationPrincipale = null;
        this.abonnes = new LinkedList<>();
        this.reg = new JRegistre();
    }

    public void initialiser(File f) throws IOException {
        StationParser parser = StationParser.getInstance();

        this.stations.clear();
        try {
            ASTNode n = parser.parse(f);
            ASTStationBuilder builder = new ASTStationBuilder();
            n.accept(builder);
            boolean first = true;
            for (Station s : builder.getStations()) {
                s.setRegistre(this.reg);
                stations.put(s.getNom(), s);
                if (first) {
                    this.setStationPrincipale(s);
                    first = false;
                }
            }
        } catch (StationParserException e) {
            // Aucune station ajoutée à la ville
        }
    }

    public void setStationPrincipale(Station st) {
        if (st != null && this.stations.get(st.getNom()) != null) {
            this.stationPrincipale = st;
        }
    }

    public Station getStation(String nom) {
        return this.stations.get(nom);
    }

    public Station getStationPlusProche(double lat, double lon) {
        Station s = new Station("tmp", lat, lon, 0);

        Station bestStation = this.stationPrincipale;
        double bestDistance = s.distance(this.stationPrincipale);

        for (Station current : (LinkedList<Station>)this.stations.values()) {
            double newDistance = s.distance(current);
            if (newDistance < bestDistance) {
                bestDistance = newDistance;
                bestStation = current;
            }
        }

        return bestStation;
    }

    public Abonne creerAbonne(String nom, String RIB) {
        Abonne a;
        try {
            a = new Abonne(nom, RIB);
            this.abonnes.add(a);
        } catch (IncorrectNameException e) {
            return null;
        }

        return a;
    }

    @Override
    public Iterator<Station> iterator() {
        HashSet<Station> stTmp = new HashSet<>(this.stations.values());
        return new ClosestStationIterator(stTmp, this.stationPrincipale);
    }

    public Map<Abonne, Double> facturation(int mois, int annee) {
        long debut = 0, fin = 0;
        if (mois >= 1 && mois <= 12) {
            Date tmp = new Date(annee, mois, 1);
            debut = tmp.getTime();

            if (mois == 12) {
                mois = 1;
                ++annee;
            } else {
                ++mois;
            }
            tmp = new Date(annee, mois, 1);
            fin = tmp.getTime();
        }

        Map<Abonne, Double> factures = new HashMap<>();
        for (Abonne a : this.abonnes) {
            factures.put(a, this.reg.facturation(a, debut, fin));
        }

        return factures;
    }
}
