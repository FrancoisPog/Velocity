package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ClosestStationIteratorTest {
    private Ville besancon;
    private File stationsFile;
    private final int NB_STATIONS = 6;

    @Before
    public void setUpBesancon() {
        this.besancon = new Ville();
        this.stationsFile = new File("./target/classes/data/stationsVille/stationsBesancon.txt");
    }

    @Test
    public void testIterateBesancon() throws IOException {
        besancon.initialiser(this.stationsFile);

        int nbStations = 0;
        Iterator<Station> iter = besancon.iterator();
        while (iter.hasNext()) {
            Assert.assertNotNull(iter.next());
            ++nbStations;
        }

        Assert.assertEquals(this.NB_STATIONS, nbStations);
    }

}
