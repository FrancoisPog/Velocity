package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class StationIntegrationTest {

    final String NOM = "317C";
    final int CAPACITE = 5;
    final double LATITUDE = 47.24547025801882;
    final double LONGITUDE = 5.9879826235144495;
    final private String[] options = {
            "CADRE_ALUMINIUM",
            "SUSPENSION_AVANT",
            "SUSPENSION_ARRIERE",
            "FREINS_DISQUE",
            "ASSISTANCE_ELECTRIQUE"
    };
    private FabriqueVelo fabrique;

    private Station station;

    private Station s;
    private final int CAPACITY = 42;

    @Before
    public void initStation() {
        station = new Station(NOM, LATITUDE, LONGITUDE, CAPACITE);
        this.s = new Station("Station Karl Ente-Deus", 42, 42, CAPACITY);
    }

    @Before
    public void initFabrique(){
        fabrique = FabriqueVelo.getInstance();
    }


    public Abonne createAbonne(boolean retEstBloque) throws IncorrectNameException {
        Abonne abonne = new Abonne("Keyser Söze", "10278-08000-00022270603-02");
        if (retEstBloque) {
            abonne.bloquer();
        }
        return abonne;
    }

    public IVelo createIVelo(int retArrimer, boolean retEstAbime, double retProchaineRevision) {
        IVelo velo = fabrique.construire('f',options[(int)(Math.random()*options.length)]);
        if (retArrimer == -1) {
            velo.arrimer();
        }
        if (retEstAbime) {
            velo.abimer();
        }
        velo.parcourir(500 - retProchaineRevision);
        return velo;
    }

    private void assertCompoStation(int abime, int revision, int ok,Station ...stations){
        if(stations.length != 0){
            station = stations[0];
        }
        int nbAbime = 0, nbRevision = 0, nbOk = 0;
        for(int i = 0 ; i < station.capacite() ; ++i){
            IVelo velo = station.veloALaBorne(i+1);
            if(velo == null){
                continue;
            }

            if(velo.estAbime()){
                nbAbime++;
                continue;
            }

            if(velo.prochaineRevision() <= 0){
                nbRevision++;
                continue;
            }

            nbOk++;
        }

        //System.out.println("Station : "+nbOk+" + a("+nbAbime+") + r("+nbRevision+")");
        assertEquals("nbAbime",abime,nbAbime);
        assertEquals("nbRevision",revision,nbRevision);
        assertEquals("nbOk",ok,nbOk);
    }

    private void assertCompoSet(Set<IVelo> set, int abime, int revision, int ok){
        int nbAbime = 0, nbRevision = 0, nbOk = 0;
        for(IVelo velo : set){
            if(velo == null){
                continue;
            }

            if(velo.estAbime()){
                nbAbime++;
                continue;
            }

            if(velo.prochaineRevision() <= 0){
                nbRevision++;
                continue;
            }

            nbOk++;
        }

        //System.out.println("Set : "+nbOk+" + a("+nbAbime+") + r("+nbRevision+")");

        assertEquals("nbAbime",abime,nbAbime);
        assertEquals("nbRevision",revision,nbRevision);
        assertEquals("nbOk",ok,nbOk);
    }


    /****************************************************
     TESTS
     ****************************************************/

    @Test
    public void constructeur() {
        assertEquals(NOM, station.getNom());
        assertEquals(CAPACITE, station.capacite());
        assertEquals(CAPACITE, station.nbBornesLibres());
    }

    @Test
    public void constructeurCapaciteNegative() {
        station = new Station(NOM, LATITUDE, LONGITUDE, -3);
        assertEquals(NOM, station.getNom());
        assertEquals(-3, station.capacite());
        assertEquals(0, station.nbBornesLibres());
    }

    @Test
    public void nbBornesLibres() {
        station.setRegistre(new JRegistre());
        station.arrimerVelo(createIVelo(0, false, 500), 3);
        assertEquals(CAPACITE - 1, station.nbBornesLibres());
    }

    @Test
    public void toutesBornesLibres() {
        for (int i = 0; i < CAPACITE; ++i) {
            assertNull(station.veloALaBorne(i));
        }
    }

    @Test
    public void veloALaBorneMauvaiseBorne() {
        assertNull(station.veloALaBorne(-1));
        assertNull(station.veloALaBorne(CAPACITE + 1));
    }

    @Test
    public void distance() {
        Station other = new Station("Kyoto", 35.011636, 135.768029, 5);
        assertEquals(9589.796,station.distance(other),10);
    }

    @Test
    public void distanceNull(){
        assertEquals(0,station.distance(null),0);
    }

    @Test
    public void emprunterOk() throws IncorrectNameException {
        IRegistre registre = new JRegistre();
        Abonne abonne = createAbonne(false);

        station.setRegistre(registre);
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 3));

        IVelo velo = station.emprunterVelo(abonne, 3);
        assertNotNull(velo);
        assertEquals(-1,velo.decrocher());
        assertNull(station.emprunterVelo(abonne, 3));
    }

    @Test
    public void emprunterAbonneBloque() throws IncorrectNameException {
        IRegistre registre = new JRegistre();
        Abonne abonne = createAbonne(true);

        station.setRegistre(registre);
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 3));

        assertNull(station.emprunterVelo(abonne, 3));
    }

    @Test
    public void emprunterAbonneNull(){
        station.setRegistre(new JRegistre());

        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 3));

        assertNull(station.emprunterVelo(null, 3));

    }

    @Test
    public void emprunterSansRegistre() throws IncorrectNameException {
        Abonne abonne = createAbonne(false);

        assertNull(station.emprunterVelo(abonne, 2));
    }

    @Test
    public void emprunterTropEmprunts() throws IncorrectNameException {
        station.setRegistre(new JRegistre());

        Abonne abonne = createAbonne(false);


        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 2));
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 3));


        assertNotNull(station.emprunterVelo(abonne, 2));
        assertNull(station.emprunterVelo(abonne, 3));

        assertEquals(CAPACITE - 1, station.nbBornesLibres());

    }

    @Test
    public void emprunterBorneVide() throws IncorrectNameException {
        IRegistre registre = new JRegistre();
        station.setRegistre(registre);

        Abonne abonne = createAbonne(false);

        assertNull(station.emprunterVelo(abonne, 2));

    }

    @Test
    public void emprunterErreurVelo() throws IncorrectNameException {
        IRegistre registre = new JRegistre();
        station.setRegistre(registre);

        Abonne abonne = createAbonne(false);

        IVelo velo = createIVelo(-1, false, 500);

        assertEquals(-3, station.arrimerVelo(velo, 2));

        assertNull(station.emprunterVelo(abonne, 2));

    }

    @Test
    public void emprunterBorneTropPetite() throws IncorrectNameException {
        station.setRegistre(new JRegistre());

        assertNull(station.emprunterVelo(createAbonne(false),0));
        assertNull(station.emprunterVelo(createAbonne(false),-4));
    }

    @Test
    public void emprunterBorneTropGrande() throws IncorrectNameException {
        station.setRegistre(new JRegistre());

        assertNull(station.emprunterVelo(createAbonne(false),CAPACITE));
        assertNull(station.emprunterVelo(createAbonne(false),CAPACITE + 10));
    }

    @Test
    public void arrimerOk() throws IncorrectNameException {
        station.setRegistre(new JRegistre());
        IVelo velo = createIVelo(0, false, 500);
        assertEquals(-4, station.arrimerVelo(velo, 3));
        assertEquals(-1,velo.arrimer());
        assertEquals(velo,station.emprunterVelo(createAbonne(false),3));
        assertEquals(-1,velo.decrocher());
        assertEquals(0,station.arrimerVelo(velo,3));
        assertEquals(-1,velo.arrimer());
    }

    @Test
    public void arrimerSansEmprunt() {
        station.setRegistre(new JRegistre());
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 3));
    }

    @Test
    public void arrimerAvantEmprunt() throws IncorrectNameException {
        Station s = Mockito.spy(station);

        s.setRegistre(new JRegistre());
        Abonne a = createAbonne(false);
        IVelo velo = createIVelo(0,false,300);
        assertEquals(-4,s.arrimerVelo(velo,3));
        assertNotNull(s.emprunterVelo(a,3));

        Mockito.when(s.maintenant()).thenReturn(System.currentTimeMillis() - 1000 * 60 * 5);

        assertEquals(-4,s.arrimerVelo(velo,4));
    }

    @Test
    public void arrimerSansRegistre() {
        assertEquals(-2, station.arrimerVelo(createIVelo(0, false, 500), 3));
    }

    @Test
    public void arrimerVeloNull() {
        station.setRegistre(new JRegistre());
        assertEquals(-1, station.arrimerVelo(null, 3));
    }

    @Test
    public void arrimerMauvaiseBorne() {
        station.setRegistre(new JRegistre());
        assertEquals(-1, station.arrimerVelo(createIVelo(0, false, 500), -4));
        assertEquals(-1, station.arrimerVelo(createIVelo(0, false, 500), CAPACITE + 1));
        assertEquals(-1, station.arrimerVelo(createIVelo(0, false, 500), 0));

    }

    @Test
    public void arrimerBorneNonLibre() {
        station.setRegistre(new JRegistre());
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 2));
        assertEquals(-2, station.arrimerVelo(createIVelo(0, false, 500), 2));
    }

    @Test
    public void arrimerVeloErreurVelo() {
        station.setRegistre(new JRegistre());
        assertEquals(-3, station.arrimerVelo(createIVelo(-1, false, 500), 3));

    }

    @Test
    public void arrimerErreurRegistre() {
        station.setRegistre(new JRegistre());
        assertEquals(-4, station.arrimerVelo(createIVelo(0, false, 500), 2));
    }


    @Test
    public void equilibrerRempliDeBonEtat() {
        Set<IVelo> nouveaux = new HashSet<>();
        // set : 5
        // station : 0
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(2,nouveaux.size());
        // station : 3
        // set : 2
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,0,0,2);
    }

    @Test
    public void equilibrerRempliAvecRevisionNecessaire() {
        station.setRegistre(new JRegistre());
        Set<IVelo> nouveaux = new HashSet<>();
        // set :        2 + a(1) + r(2)
        // station :    1 + a(1) + r(1)
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 0));

        station.arrimerVelo(createIVelo(0, true, 0), 3);
        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, false, 500), 4);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(5,nouveaux.size());
        // station : 3
        // set :  a(2) +  r(3)
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,2,3,0);
    }

    @Test
    public void equilibrerPasDeVeloDispo() {
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : a(4)
        // station : 1 + a(2)

        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 0));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 0));

        station.arrimerVelo(createIVelo(0, true, 0), 4);
        station.arrimerVelo(createIVelo(0, true, 0), 3);
        station.arrimerVelo(createIVelo(0, false, 500), 2);


        station.equilibrer(nouveaux);
        assertEquals(4, station.nbBornesLibres());
        assertEquals(6, nouveaux.size());
        // set : a(6)
        // station : 1
        assertCompoStation(0,0,1);
        assertCompoSet(nouveaux,6,0,0);
    }

    @Test
    public void equilibrerTropDeVelos() {
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : a(2)
        // stattion : 5
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 0));

        station.arrimerVelo(createIVelo(0, false, 500), 1);
        station.arrimerVelo(createIVelo(0, false, 500), 2);
        station.arrimerVelo(createIVelo(0, false, 500), 3);
        station.arrimerVelo(createIVelo(0, false, 500), 4);
        station.arrimerVelo(createIVelo(0, false, 500), 5);


        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(4,nouveaux.size());
        // set : 2 + a(2)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,2,0,2);
    }


    @Test
    public void equilibrerJusteEnleverMauvaisEtat() {
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 2
        // station : 3 + a(2)
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));


        station.arrimerVelo(createIVelo(0, false, 500), 1);
        station.arrimerVelo(createIVelo(0, false, 500), 2);
        station.arrimerVelo(createIVelo(0, false, 500), 3);
        station.arrimerVelo(createIVelo(0, true, 500), 4);
        station.arrimerVelo(createIVelo(0, true, 500), 5);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(4, nouveaux.size());
        // set : 2 + a(2)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,2,0,2);
    }

    @Test
    public void equilibrerTropDeVelosBis() {
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 1 + r(1)
        // stattion : 4
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 500));

        station.arrimerVelo(createIVelo(0, false, 500), 4);
        station.arrimerVelo(createIVelo(0, false, 500), 2);
        station.arrimerVelo(createIVelo(0, false, 500), 3);
        station.arrimerVelo(createIVelo(0, false, 500), 5);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(3, nouveaux.size());
        // set : 2 + r(1)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,0,1,2);
    }

    @Test
    public void equilibrerPasAssezVelos(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 1 + r(3)
        // stattion : r(2) + a(3)
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 500));

        station.arrimerVelo(createIVelo(0, false, 0), 4);
        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, true, 500), 3);
        station.arrimerVelo(createIVelo(0, true, 500), 5);
        station.arrimerVelo(createIVelo(0, true, 500), 1);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(6, nouveaux.size());
        // set : a(3) + r(3)
        // station : 1 + r(2)
        assertCompoStation(0,2,1);
        assertCompoSet(nouveaux,3,3,0);
    }

    @Test
    public void equilibrerRemplacerAbimeEtRemplir(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : a(2) + r(2) + 3
        // station : a(1)
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));



        station.arrimerVelo(createIVelo(0, true, 500), 1);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(5, nouveaux.size());
        // set : a(3) + r(2)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,3,2,0);
    }

    @Test
    public void equilibrerAucun(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : a(2) + r(2)
        // station : 0
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));


        station.equilibrer(nouveaux);
        assertEquals(5, station.nbBornesLibres());
        assertEquals(4, nouveaux.size());
        // set : a(2) + r(2)
        // station : 0
        assertCompoStation(0,0,0);
        assertCompoSet(nouveaux,2,2,0);
    }

    @Test
    public void equilibrer(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 3
        // station : a(1) + r(3)
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));


        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, false, 0), 3);
        station.arrimerVelo(createIVelo(0, false, 0), 5);
        station.arrimerVelo(createIVelo(0, true, 500), 1);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(4, nouveaux.size());
        // set : a(1) + r(3)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,1,3,0);
    }

    @Test
    public void equilibrerBis(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 2
        // station : a(1) + r(3)
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));

        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, false, 0), 3);
        station.arrimerVelo(createIVelo(0, false, 0), 5);
        station.arrimerVelo(createIVelo(0, true, 500), 1);

        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(3, nouveaux.size());
        // set : a(1) + r(2)
        // station : 2 + r(1)
        assertCompoStation(0,1,2);
        assertCompoSet(nouveaux,1,2,0);
    }

    @Test
    public void equilibrerTer(){
        station.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 3 + r(3) + a(3)
        // station : 1 + r(1)
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));

        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, false, 40), 3);



        station.equilibrer(nouveaux);
        assertEquals(2, station.nbBornesLibres());
        assertEquals(8, nouveaux.size());
        // set : 1 + r(4) + a(3)
        // station : 3
        assertCompoStation(0,0,3);
        assertCompoSet(nouveaux,3,4,1);
    }

    @Test
    public void equilibrerGrosseStation(){
        Station grosseStation = new Station("Paris",10,10,30);
        grosseStation.setRegistre(new JRegistre());

        Set<IVelo> nouveaux = new HashSet<>();
        // set : 8 + r(5) + a(3)
        // station : 6 + r(4) + a(10)
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, false, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, true, 500));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));
        nouveaux.add(createIVelo(0, false, 0));

        grosseStation.arrimerVelo(createIVelo(0, false, 500), 1);
        grosseStation.arrimerVelo(createIVelo(0, false, 500), 4);
        grosseStation.arrimerVelo(createIVelo(0, false, 500), 5);
        grosseStation.arrimerVelo(createIVelo(0, false, 500), 6);
        grosseStation.arrimerVelo(createIVelo(0, false, 500), 7);
        grosseStation.arrimerVelo(createIVelo(0, false, 500), 8);

        grosseStation.arrimerVelo(createIVelo(0, false, 0), 2);
        grosseStation.arrimerVelo(createIVelo(0, false, 0), 3);
        grosseStation.arrimerVelo(createIVelo(0, false, 0), 9);
        grosseStation.arrimerVelo(createIVelo(0, false, 0), 10);

        grosseStation.arrimerVelo(createIVelo(0, true, 40), 20);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 11);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 12);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 13);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 14);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 15);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 16);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 17);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 18);
        grosseStation.arrimerVelo(createIVelo(0, true, 40), 19);

        assertCompoStation(10,4,6,grosseStation);
        assertCompoSet(nouveaux,3,5,8);

        grosseStation.equilibrer(nouveaux);

        assertEquals(15, grosseStation.nbBornesLibres());
        assertEquals(21, nouveaux.size());
        // set : 0 + r(8) + a(13)
        // station : 14 + r(1)
        assertCompoStation(0,1,14,grosseStation);
        assertCompoSet(nouveaux,13,8,0);

    }

    @Test
    public void equilibrerSetNull(){
        station.setRegistre(new JRegistre());

        station.arrimerVelo(createIVelo(0, false, 0), 2);
        station.arrimerVelo(createIVelo(0, false, 40), 3);

        station.equilibrer(null);

        assertCompoStation(0,1,1);
    }

    /* ---------- Private functions ---------- */

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

    private void addBikes(int nb) {
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Set<IVelo> setVelos = fillSet(nb, true);
        int i = 1;
        for (IVelo v : setVelos) {
            this.s.arrimerVelo(v, i);
            ++i;
        }
    }

    /* ---------- Before ---------- */

    @Before
    public void setUp() {
        this.s = new Station("Station Karl Ente-Deus", 42, 42, CAPACITY);
    }

    /* ---------- Getters ---------- */

    @Test
    public void testGetters_GetNom() {
        Assert.assertEquals("Station Karl Ente-Deus", this.s.getNom());
    }

    @Test
    public void testGetters_Capacite() {
        Assert.assertEquals(this.CAPACITY, this.s.capacite());
    }

    @Test
    public void testGetters_Capacite_Zero() {
        Station st = new Station("Station Knut", 10, 10, 0);
        Assert.assertEquals(0, st.capacite());
    }

    @Test
    public void testGetters_nbBL() {
        Assert.assertEquals(this.CAPACITY, this.s.nbBornesLibres());
    }

    @Test
    public void testGetters_nbBL_Zero() {
        Station st = new Station("Station Knut", 10, 10, 0);
        Assert.assertEquals(0, st.nbBornesLibres());
    }

    @Test
    public void testGetters_nbBL_Minus() {
        Station st = new Station("Station Knut", 10, 10, -42);
        Assert.assertEquals(0, st.nbBornesLibres());
    }

    @Test
    public void testGetters_nbBL_NotEmpty() {
        IVelo v = new Velo();
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);
        this.s.arrimerVelo(v, 1);

        Assert.assertEquals(this.s.capacite() - 1, this.s.nbBornesLibres());
    }

    /* ---------- veloALaBorne ---------- */

    @Test
    public void testVeloALaBorne_Empty() {
        for (int i = 1; i <= this.s.capacite(); i++) {
            Assert.assertNull(this.s.veloALaBorne(i));
        }
    }

    @Test
    public void testVeloALaBorne_NotEmpty() {
        this.addBikes(this.CAPACITY);

        int nbOfNull = 0, nbOfNotNull = 0;
        for (int i = 1; i <= this.s.capacite(); i++) {
            if (this.s.veloALaBorne(i) == null) {
                nbOfNull++;
            } else {
                nbOfNotNull++;
            }
        }

        Assert.assertEquals(0, nbOfNull);
        Assert.assertEquals(this.CAPACITY, nbOfNotNull);
    }

    @Test
    public void testVeloALaBorne_OutOfBounds() {
        Assert.assertNull(this.s.veloALaBorne(-1));
        Assert.assertNull(this.s.veloALaBorne(152));
    }

    /* ---------- emprunterVelo ---------- */

    @Test
    public void testEmprunterVelo_Empty_RegKo_AbNull() {
        Assert.assertNull(this.s.emprunterVelo(null, 1));
    }

    @Test
    public void testEmprunterVelo_Empty_RegOk_AbOk() throws IncorrectNameException {
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);
        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbKo() throws IncorrectNameException {
        this.addBikes(this.CAPACITY);
        Abonne a = new Abonne("Donald", "STOP-THE-COUNT");

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegKO_AbOk() throws IncorrectNameException {
        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbOk() throws IncorrectNameException {
        this.addBikes(this.CAPACITY);
        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");

        Assert.assertNotNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbOk_Out() throws IncorrectNameException {
        this.addBikes(this.CAPACITY);
        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");

        Assert.assertNotNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_MultipleBorrows() throws IncorrectNameException {
        this.addBikes(this.CAPACITY);
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");
        this.s.emprunterVelo(a, 2);

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    /* ---------- arrimerVelo ---------- */

    @Test
    public void testArrimerVelo_Success() throws IncorrectNameException {
        this.addBikes(this.CAPACITY);

        Abonne a = new Abonne("Donald", "12345-98765-12345678912-21");
        IVelo v = this.s.emprunterVelo(a, 1);

        Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_VeloNull_BorneOk() {
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Assert.assertEquals(-1, this.s.arrimerVelo(null, 1));
    }

    @Test
    public void testArrimerVelo_VeloOk_BorneOutOfBounds() {
        IVelo v = new Velo();
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Assert.assertEquals(-1, this.s.arrimerVelo(v, -1));
        Assert.assertEquals(-1, this.s.arrimerVelo(v, 1250));
    }

    @Test
    public void testArrimerVelo_RegistreKO() {
        IVelo v = new Velo();

        Assert.assertEquals(-2, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_BorneNotEmpty() {
        IVelo v = new Velo();
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        this.s.arrimerVelo(v, 1);

        IVelo v2 = new Velo();
        Assert.assertEquals(-2, this.s.arrimerVelo(v2, 1));
    }

    @Test
    public void testArrimerVelo_CantArrimer() {
        IVelo v = new Velo();
        v.arrimer();
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Assert.assertEquals(-3, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_ErrorRetourner() {
        IVelo v = new Velo();
        IRegistre reg = new JRegistre();
        this.s.setRegistre(reg);

        Assert.assertEquals(-4, this.s.arrimerVelo(v, 1));
    }

    /* ---------- equilibrer ---------- */

    @Test
    public void testEquilibrer_Null() {
        this.s.equilibrer(null);

        Assert.assertEquals(this.CAPACITY, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_EmptyStation() {
        Set<IVelo> velos = fillSet(2, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.CAPACITY - 2, this.s.nbBornesLibres());
        Assert.assertEquals(0, velos.size());
    }

    @Test
    public void testEquilibrer_EmptyStation_BadBikes() {
        Set<IVelo> velos = fillSet(2, false);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.CAPACITY, this.s.nbBornesLibres());
        Assert.assertEquals(2, velos.size());
    }

    @Test
    public void testEquilibrer_EmptyStation_Both() {
        Set<IVelo> velos = fillSet(2, true);
        Set<IVelo> v1 = fillSet(2, false);
        Set<IVelo> v2 = fillSet(2, true);
        velos.addAll(v1);
        velos.addAll(v2);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.CAPACITY - 4, this.s.nbBornesLibres());
        Assert.assertEquals(2, velos.size());
    }

    @Test
    public void testEquilibrer_Empty_MoreThanCapacity_Even() {
        Set<IVelo> velos = fillSet(this.CAPACITY, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.CAPACITY / 2, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Enough() {
        this.addBikes(this.CAPACITY / 2);
        Assert.assertEquals(this.CAPACITY / 2, this.s.nbBornesLibres());

        Set<IVelo> velos = fillSet(this.CAPACITY, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.CAPACITY / 2, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Empty_MoreThanCapacity_Odd() {
        Station s1 = new Station("Stop the count", 10, 10, 21);
        Set<IVelo> velos = fillSet(this.CAPACITY, true);
        s1.equilibrer(velos);

        Assert.assertEquals(21 / 2, s1.nbBornesLibres());
    }

    /* ---------- distance ---------- */

    @Test
    public void testDistance_Success() {
        Station s1 = new Station("Station gare Viotte", 47.246501551427329, 6.022715427111734, 10);
        Station s2 = new Station("Station Rivotte", 47.232117826784354, 6.035021926715934, 20);

        Assert.assertEquals(1.850, s1.distance(s2), 0.01);
    }

    @Test
    public void testDistance_Null() {
        Assert.assertEquals(0, this.s.distance(null), 0.001);
    }

    /* ---------- maintenant ---------- */

    @Test
    public void testMaintenant_Spy() {
        Station st = Mockito.spy(s);
        long later10min = System.currentTimeMillis() + 10 * 60 * 1000;
        Mockito.when(st.maintenant()).thenReturn(later10min);

        Assert.assertEquals(later10min, st.maintenant());
    }

}
