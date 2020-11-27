package fr.ufc.l3info.oprog;

import java.util.*;

public class ClosestStationIterator implements Iterator<Station> {
    private final Set<Station> stations;
    private Station current;

    ClosestStationIterator(Set<Station> stations, Station s){
        this.stations = new HashSet<>(stations);
        this.current = s;
    }

    @Override
    public boolean hasNext() {
        return this.stations.size() != 0;
    }

    @Override
    public Station next() {
        Station res = this.current;

        // Suppression de la station courante des stations restantes
        this.stations.remove(this.current);

        // Calcul de la station la plus proche de la station courante
        Station closest = null;
        double min = 0;
        for(Station s : this.stations) {
            double distance = s.distance(this.current);
            if(closest == null || distance < min){
                min = distance;
                closest = s;
            }
        }

        // La prochaine station devient la plus proche de la station courante
        this.current = closest;

        // On retourne l'ancienne station courante
        return res;
    }

    @Override
    public void remove() {}
}
