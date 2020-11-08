package fr.ufc.l3info.oprog;


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

    @Before
    public void initStation() {
        station = new Station(NOM, LATITUDE, LONGITUDE, CAPACITE);
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

    private void assertCompoStation(int abime, int revision, int ok){
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




}
