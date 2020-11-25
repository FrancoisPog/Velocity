package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class TestsTP7 {
    /* ---------- Tarif de facturation dégressif ---------- */
    final int CAPACITE = 10;

    // ***** Tests de la méthode emprunteur() *****

    IRegistre registre;
    Abonne valentin;
    IVelo becane;
    Station station;

    private Abonne fdv;
    private Station sMatz;
    private IRegistre jReg;

    private Set<IVelo> fillSet(int size, boolean ok) {
        Set<IVelo> setVelos = new HashSet<IVelo>();
        for (int i = 0; i < size; i++) {
            IVelo v = new Velo();
            if (!ok) {
                v.decrocher();
                v.parcourir(600);
                v.arrimer();
            }

            setVelos.add(v);
        }

        return setVelos;
    }

    private void addBikes(Station st, int nb) {
        Set<IVelo> setVelos = fillSet(nb, true);
        int i = 1;
        for (IVelo v : setVelos) {
            st.arrimerVelo(v, i);
            ++i;
        }
    }

    @Before
    public void setUp() throws IncorrectNameException {
        this.registre = new JRegistre();
        this.valentin = new Abonne("Valentin","12345-98765-12345678912-21");
        assertFalse(this.valentin.estBloque());
        this.becane = new Velo();
        this.station = new Station("Centre du monde",0,0,CAPACITE);
        this.station.setRegistre(registre);

        for(int i = 1 ; i <= CAPACITE ; ++i){
            if( i % 2 == 0){
                assertEquals(-4,station.arrimerVelo(new Velo(),i));
                assertNotNull(station.veloALaBorne(i));
                continue;
            }
            assertNull(station.veloALaBorne(i));
        }

        this.fdv = new Abonne("Fabien De Vel", "12345-98765-12345678912-21");

        Station stationTMP = new Station("Station Matz Ruby", 6, 7, 42);
        this.jReg = new JRegistre();
        stationTMP.setRegistre(jReg);
        this.sMatz = Mockito.spy(stationTMP);
    }

    @Test
    public void emprunteur_default(){

        registre.emprunter(valentin,becane,10);
        assertEquals(valentin,registre.emprunteur(becane));
        registre.retourner(becane,20);

    }

    @Test
    public void emprunteur_nonEmprunte(){
        assertNull(registre.emprunteur(becane));
    }

    @Test
    public void emprunteur_retourne(){
        registre.emprunter(valentin,becane,10);
        registre.retourner(becane,20);
        assertNull(registre.emprunteur(becane));
    }

    @Test
    public void emprunter_plusieursEmprunts(){
        IVelo trottinette = new Velo();

        registre.emprunter(valentin,trottinette,10);
        registre.emprunter(valentin,becane,15);

        assertEquals(valentin,registre.emprunteur(becane));
        assertEquals(valentin,registre.emprunteur(trottinette));

        registre.retourner(becane,20);

        assertNull(registre.emprunteur(becane));
        assertEquals(valentin,registre.emprunteur(trottinette));

        registre.retourner(trottinette,25);
        assertNull(registre.emprunteur(becane));
        assertNull(registre.emprunteur(trottinette));
    }

    @Test
    public void emprunteur_veloNull(){
        assertNull(registre.emprunteur(null));
    }

    // ***** Tests sur le retour de vélo abimé *****

    @Test
    public void retourner_abime(){
        IVelo velo = station.emprunterVelo(valentin,2);
        assertNotNull(velo);
        assertFalse(velo.estAbime());

        velo.abimer();

        assertEquals(0,station.arrimerVelo(velo,7));

        assertTrue(velo.estAbime());
        assertTrue(valentin.estBloque());
    }

    @Test
    public void retourner_nonAbime(){
        IVelo velo = station.emprunterVelo(valentin,2);
        assertNotNull(velo);
        assertFalse(velo.estAbime());

        assertEquals(0,station.arrimerVelo(velo,7));

        assertFalse(velo.estAbime());
        assertFalse(valentin.estBloque());
    }

    @Test
    public void emprunter_abime(){
        station.veloALaBorne(4).abimer();

        assertFalse(valentin.estBloque());
        assertNull(station.emprunterVelo(valentin,4));
        assertFalse(valentin.estBloque());
    }

    @Test
    public void testTarif_Simple() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;

        addBikes(this.sMatz, this.sMatz.capacite());

        IVelo v;
        v = this.sMatz.emprunterVelo(this.fdv, 1);
        Assert.assertNotNull(v);
        newTime += (60 * 60 * 1000 + 10); // + 1h
        Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
        Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));

        Assert.assertEquals(1 * v.tarif(), this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_PlusieursVelos() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.sMatz, this.sMatz.capacite());

        int nbVelos = 5;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        Assert.assertEquals(nbVelos * v.tarif(), this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.sMatz, this.sMatz.capacite());

        IVelo v = null;
        for (int i = 0; i < 12; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        double tarif = 10 * v.tarif() + (2 * v.tarif() * 0.9);
        Assert.assertEquals(tarif, this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);

        for (int i = 0; i < 10; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        tarif = 10 * v.tarif() + (10 * v.tarif() * 0.9) + (2 * v.tarif() * 0.8);
        Assert.assertEquals(tarif, this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_PlusDe50() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.sMatz, this.sMatz.capacite());

        int nbVelos = 65;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        double tarif = 10 * v.tarif(); // 1 à 10
        tarif += 10 * v.tarif() * 0.9; // 11 à 20
        tarif += 10 * v.tarif() * 0.8; // 21 à 30
        tarif += 10 * v.tarif() * 0.7; // 31 à 40
        tarif += 10 * v.tarif() * 0.6; // 41 à 50
        tarif += 15 * v.tarif() * 0.5; // 51 à 65
        Assert.assertEquals(tarif, this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Moins5Min() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;

        addBikes(this.sMatz, this.sMatz.capacite());

        IVelo v;
        v = this.sMatz.emprunterVelo(this.fdv, 1);
        Assert.assertNotNull(v);
        newTime += (3 * 60 * 1000 + 10); // + 3min
        Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
        Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));

        Assert.assertEquals(v.tarif() / 20, this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Moins5Min_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long threeMin = 3 * 60 * 1000 + 10;

        addBikes(this.sMatz, this.sMatz.capacite());

        int nbVelos = 20;
        IVelo v = null;
        for (int i = 0; i < nbVelos; i++) {
            newTime += threeMin; // + 3min
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += threeMin; // + 3min
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        Assert.assertEquals(nbVelos * (v.tarif() / 20), this.jReg.facturation(this.fdv, timeNow, newTime), 0.001);
    }

    @Test
    public void testTarif_Mix_Niveaux() {
        long timeNow = System.currentTimeMillis(), newTime = timeNow;
        long threeMin = 3 * 60 * 1000 + 10, oneHour = 60 * 60 * 1000 + 10;

        addBikes(this.sMatz, this.sMatz.capacite());

        IVelo v = null;
        for (int i = 0; i < 15; i++) {
            newTime += threeMin; // + 3min
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += threeMin; // + 3min
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        for (int i = 0; i < 12; i++) {
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            v = this.sMatz.emprunterVelo(this.fdv, 1);
            Assert.assertNotNull(v);
            newTime += oneHour; // + 1h
            Mockito.when(this.sMatz.maintenant()).thenReturn(newTime);
            Assert.assertEquals(0, this.sMatz.arrimerVelo(v, 1));
        }

        double tarif = 15 * (v.tarif() / 20);
        tarif += 10 * v.tarif();
        tarif += 2 * v.tarif() * 0.9;
        Assert.assertEquals(tarif, this.jReg.facturation(this.fdv, timeNow, newTime), 0.01);
    }
}