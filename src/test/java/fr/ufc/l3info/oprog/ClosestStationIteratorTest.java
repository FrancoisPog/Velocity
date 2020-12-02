package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class ClosestStationIteratorTest {
    private Set<Station> listeStations;
    private Station stationPrincipale;
    private Iterator<Station> iterator;

    private final int NB_STATIONS = 6;

    @Before
    public void setUp() {
        this.listeStations = new HashSet<>();
        for (int i = 0; i < NB_STATIONS; i++) {
            this.listeStations.add(new Station(Integer.toString(i), i, i, 10));
        }
        this.stationPrincipale = this.listeStations.iterator().next();

        iterator = new ClosestStationIterator(this.listeStations, this.stationPrincipale);
    }

    @Test
    public void testIterate_Simple() {
        int nbStations = 0;
        while (this.iterator.hasNext()) {
            Assert.assertNotNull(this.iterator.next());
            ++nbStations;
        }

        Assert.assertEquals(this.NB_STATIONS, nbStations);
    }

    @Test
    public void testIterate_AucunVelo() {
        Set<Station> stations = new HashSet<>();
        Iterator<Station> iter = new ClosestStationIterator(stations, null);

        int nbStations = 0;
        while (iter.hasNext()) {
            iter.next();
            ++nbStations;
        }

        Assert.assertEquals(0, nbStations);
    }

    @Test
    public void testAucuneStationPrincipale() {
        Iterator<Station> iter = new ClosestStationIterator(this.listeStations, null);

        int nbStations = 0;
        while (iter.hasNext()) {
            iter.next();
            ++nbStations;
        }

        Assert.assertEquals(0, nbStations);
    }

    @Test
    public void testListeStationsNull() {
        Iterator<Station> iter = new ClosestStationIterator(null, null);

        int nbStations = 0;
        while (iter.hasNext()) {
            iter.next();
            ++nbStations;
        }

        Assert.assertEquals(0, nbStations);
    }

    @Test
    public void testStationPrincipaleNonIncluse() {
        Iterator<Station> iter = new ClosestStationIterator(
                this.listeStations,
                new Station("Station Spatiale Internationale", 10, 10, 10)
        );

        int nbStations = 0;
        while (iter.hasNext()) {
            iter.next();
            ++nbStations;
        }

        Assert.assertEquals(0, nbStations);
    }

    @Test
    public void testStationPlusProche() {
        Station oldStation = null;
        while (this.iterator.hasNext()) {

            Station s = this.iterator.next();

            if (oldStation != null) {

                Station meilleureStation = null;
                double meilleureDistance = 0;
                Iterator<Station> rechercheIter = new ClosestStationIterator(this.listeStations, this.stationPrincipale);
                while (rechercheIter.hasNext()) {
                    Station actuelle = rechercheIter.next();
                    if (actuelle == oldStation) continue;
                    double distance = oldStation.distance(actuelle);
                    if (meilleureStation == null || distance < meilleureDistance) {
                        meilleureDistance = distance;
                        meilleureStation = s;
                    }
                }

                Assert.assertEquals(s, meilleureStation);

            }

            oldStation = s;

        }
    }

    @Test
    public void testNePasSupprimer() {
        while (this.iterator.hasNext()) {
            Assert.assertNotNull(this.iterator.next());
            this.iterator.remove();
        }

        Assert.assertEquals(this.NB_STATIONS, this.listeStations.size());
    }
}
