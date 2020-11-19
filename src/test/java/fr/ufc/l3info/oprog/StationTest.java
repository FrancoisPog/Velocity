package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StationTest {

    final String NOM = "Granvelle";
    final int CAPACITE = 5;
    final double LATITUDE = 47.24547025801882;
    final double LONGITUDE = 5.9879826235144495;

    private Station station;

    private Station s;
    private final int BORNES_NB = 42;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initStation(){
        station = new Station(NOM,LATITUDE,LONGITUDE, CAPACITE);
        this.s = new Station("Station Karl Ente-Deus", 42, 42, BORNES_NB);
    }



    /****************************************************
                            MOCKS
     ****************************************************/

    public IRegistre createIRegistreMock(int retEmprunter, int retNbEmprunt, int retRetourner){
        IRegistre registre = mock(IRegistre.class);
        when(registre.emprunter(ArgumentMatchers.<Abonne>any(),ArgumentMatchers.<IVelo>any(),anyLong())).thenReturn(retEmprunter);
        when(registre.nbEmpruntsEnCours(ArgumentMatchers.<Abonne>any())).thenReturn(retNbEmprunt);
        when(registre.retourner(ArgumentMatchers.<IVelo>any(),anyLong())).thenReturn(retRetourner);
        return registre;
    }

    public Abonne createAbonneMock(boolean retEstBloque){
        Abonne abonne = mock(Abonne.class);
        when(abonne.estBloque()).thenReturn(retEstBloque);
        return abonne;
    }

    public IVelo createIVeloMock(int retArrimer, boolean retEstAbime, double retProchaineRevision){
        IVelo velo = mock(Velo.class);
        when(velo.arrimer()).thenReturn(retArrimer);
        when(velo.estAbime()).thenReturn(retEstAbime);
        when(velo.prochaineRevision()).thenReturn(retProchaineRevision);
        return velo;
    }



    /****************************************************
                            TESTS
     ****************************************************/

    @Test
    public void constructeur(){
        assertEquals(NOM,station.getNom());
        assertEquals(CAPACITE,station.capacite());
        assertEquals(CAPACITE,station.nbBornesLibres());
    }

    @Test
    public void constructeurCapaciteNegative(){
        station = new Station(NOM,LATITUDE,LONGITUDE, -3);
        assertEquals(NOM,station.getNom());
        assertEquals(-3,station.capacite());
        assertEquals(0,station.nbBornesLibres());
    }

    @Test
    public void nbBornesLibres(){
        station.setRegistre(createIRegistreMock(0,0,0));
        station.arrimerVelo(createIVeloMock(0,false,500),3);
        assertEquals(CAPACITE-1,station.nbBornesLibres());
    }

    @Test
    public void toutesBornesLibres(){
        for(int i = 0 ; i < CAPACITE ; ++i){
            assertNull(station.veloALaBorne(i));
        }
    }

    @Test
    public void veloALaBorneMauvaiseBorne(){
        assertNull(station.veloALaBorne(-1));
        assertNull(station.veloALaBorne(CAPACITE+1));
    }

    @Test
    public void distance(){
        Station other = new Station("Kyoto",35.011636,135.768029,5);
        System.out.println(station.distance(other));
    }


    @Test
    public void emprunterOk() {
        IRegistre registre = createIRegistreMock(0,0,0);
        Abonne abonne = createAbonneMock(false);

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));

        assertNotNull(station.emprunterVelo(abonne,3));
        assertNull(station.emprunterVelo(abonne,3));
    }

    @Test
    public void emprunterAbonneBloque() {
        IRegistre registre = createIRegistreMock(0,0,0);
        Abonne abonne = createAbonneMock(true);

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));

        assertNull(station.emprunterVelo(abonne,3));
    }

    @Test
    public void emprunterSansRegistre(){
        Abonne abonne = createAbonneMock(false);

        assertNull(station.emprunterVelo(abonne,2));
    }

    @Test
    public void emprunterTropEmprunts(){
        station.setRegistre(createIRegistreMock(0,1,0));

        Abonne abonne = createAbonneMock(false);
        IVelo velo = createIVeloMock(0,false,500);

        assertEquals(0,station.arrimerVelo(velo,2));

        assertNull(station.emprunterVelo(abonne,2));

    }

    @Test
    public void emprunterBorneVide(){
        IRegistre registre = createIRegistreMock(0,0,0);
        station.setRegistre(registre);

        Abonne abonne = createAbonneMock(false);

        assertNull(station.emprunterVelo(abonne,2));
        
    }

    @Test
    public void emprunterErreurRegistre(){
        IRegistre registre  = createIRegistreMock(-1,0,0);
        station.setRegistre(registre);

        Abonne abonne = createAbonneMock(false);

        station.arrimerVelo(createIVeloMock(0,false,500),2);

        assertNull(station.emprunterVelo(abonne,2));

    }

    @Test
    public void arrimerOk(){
        station.setRegistre(createIRegistreMock(0,0,0));
        assertEquals(0,station.arrimerVelo(createIVeloMock(0,false,500),3));
    }

    @Test
    public void arrimerSansRegistre(){
        assertEquals(-2,station.arrimerVelo(createIVeloMock(0,false,500),3));
    }

    @Test
    public void arrimerVeloNull(){
        station.setRegistre(createIRegistreMock(0,0,0));
        assertEquals(-1,station.arrimerVelo(null,3));
    }

    @Test
    public void arrimerMauvaiseBorne(){
        station.setRegistre(createIRegistreMock(0,0,0));
        assertEquals(-1,station.arrimerVelo(createIVeloMock(0,false,500),-4));
        assertEquals(-1,station.arrimerVelo(createIVeloMock(0,false,500),CAPACITE+1));

    }

    @Test
    public void arrimerBorneNonLibre(){
        station.setRegistre(createIRegistreMock(0,0,0));
        assertEquals(0,station.arrimerVelo(createIVeloMock(0,false,500),2));
        assertEquals(-2,station.arrimerVelo(createIVeloMock(0,false,500),2));
    }

    @Test
    public void arrimerVeloErreurVelo(){
        station.setRegistre(createIRegistreMock(0,0,0));
        assertEquals(-3,station.arrimerVelo(createIVeloMock(-1,false,500),3));

    }

    @Test
    public void arrimerErreurRegistre(){
        station.setRegistre(createIRegistreMock(0,0,-1));
        assertEquals(-4,station.arrimerVelo(createIVeloMock(0,false,500),2));
    }


    @Test
    public void equilibrerRempliDeBonEtat(){
        Set<IVelo> nouveaux = new HashSet<>();
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,500));

        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());
    }

    @Test
    public void equilibrerRempliAvecRevisionNecessaire(){
        station.setRegistre(createIRegistreMock(0,0,0));
        Set<IVelo> nouveaux = new HashSet<>();

        nouveaux.add(createIVeloMock(0,true,500));
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,0));
        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,0));

        station.arrimerVelo(createIVeloMock(0,true,0),3);
        station.arrimerVelo(createIVeloMock(0,false,0),2);
        station.arrimerVelo(createIVeloMock(0,false,500),4);

        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());
    }

    @Test
    public void equilibrerPasDeVeloDispo(){
        station.setRegistre(createIRegistreMock(0,0,0));

        Set<IVelo> nouveaux = new HashSet<>();

        nouveaux.add(createIVeloMock(0,true,500));
        nouveaux.add(createIVeloMock(0,true,0));
        nouveaux.add(createIVeloMock(0,true,500));
        nouveaux.add(createIVeloMock(0,true,0));

        station.arrimerVelo(createIVeloMock(0,true,0),4);
        station.arrimerVelo(createIVeloMock(0,true,0),3);
        station.arrimerVelo(createIVeloMock(0,false,500),2);


        station.equilibrer(nouveaux);
        assertEquals(4,station.nbBornesLibres());

    }
    @Test
    public void equilibrerTropDeVelo(){
        station.setRegistre(createIRegistreMock(0,0,0));

        Set<IVelo> nouveaux = new HashSet<>();
        nouveaux.add(createIVeloMock(0,true,500));
        nouveaux.add(createIVeloMock(0,true,0));

        station.arrimerVelo(createIVeloMock(0,false,500),1);
        station.arrimerVelo(createIVeloMock(0,false,500),2);
        station.arrimerVelo(createIVeloMock(0,false,500),3);
        station.arrimerVelo(createIVeloMock(0,false,500),4);
        station.arrimerVelo(createIVeloMock(0,false,500),5);


        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());

    }
    @Test
    public void equilibrerTropDeVelos(){
        station.setRegistre(createIRegistreMock(0,0,0));

        Set<IVelo> nouveaux = new HashSet<>();
        nouveaux.add(createIVeloMock(0,true,500));
        nouveaux.add(createIVeloMock(0,true,0));


        station.arrimerVelo(createIVeloMock(0,false,500),1);
        station.arrimerVelo(createIVeloMock(0,false,500),2);
        station.arrimerVelo(createIVeloMock(0,false,500),3);
        station.arrimerVelo(createIVeloMock(0,false,500),4);
        station.arrimerVelo(createIVeloMock(0,false,500),5);


        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());
    }

    @Test
    public void equilibrerJusteEnleverMauvaisEtat(){
        station.setRegistre(createIRegistreMock(0,0,0));

        Set<IVelo> nouveaux = new HashSet<>();

        nouveaux.add(createIVeloMock(0,false,500));
        nouveaux.add(createIVeloMock(0,false,500));


        station.arrimerVelo(createIVeloMock(0,false,500),1);
        station.arrimerVelo(createIVeloMock(0,false,500),2);
        station.arrimerVelo(createIVeloMock(0,false,500),3);
        station.arrimerVelo(createIVeloMock(0,true,500),4);
        station.arrimerVelo(createIVeloMock(0,true,500),5);

        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());
    }

    @Test
    public void equilibrerTropDeVelosBis(){
        station.setRegistre(createIRegistreMock(0,0,0));

        Set<IVelo> nouveaux = new HashSet<>();
        nouveaux.add(createIVeloMock(0,false,0));
        nouveaux.add(createIVeloMock(0,false,500));

        station.arrimerVelo(createIVeloMock(0,false,500),4);
        station.arrimerVelo(createIVeloMock(0,false,500),2);
        station.arrimerVelo(createIVeloMock(0,false,500),3);
        station.arrimerVelo(createIVeloMock(0,false,500),5);

        station.equilibrer(nouveaux);
        assertEquals(2,station.nbBornesLibres());
    }

    /* ---------- Private functions ---------- */

    private Set<IVelo> fillSet(int size, boolean ok) {
        Set<IVelo> setVelos = new HashSet<IVelo>();
        if (setVelos == null) {
            return null;
        }

        for (int i = 0; i < size; i++) {
            IVelo v = Mockito.mock(IVelo.class);
            Mockito.when(v.estAbime()).thenReturn(!ok);
            Mockito.when(v.prochaineRevision()).thenReturn(ok ? 500.0 : -10.0);

            setVelos.add(v);
        }

        return setVelos;
    }

    private void addBikes(int nb) {
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        Set<IVelo> setVelos = fillSet(nb, true);
        int i = 1;
        for (IVelo v : setVelos) {
            this.s.arrimerVelo(v, i);
            ++i;
        }
    }

    /* ---------- Getters ---------- */

    @Test
    public void testGetters_GetNom() {
        Assert.assertEquals("Station Karl Ente-Deus", this.s.getNom());
    }

    @Test
    public void testGetters_Capacite() {
        Assert.assertEquals(this.BORNES_NB, this.s.capacite());
    }

    @Test
    public void testGetters_Capacite_Zero() {
        Station st = new Station("Station Knut", 10, 10, 0);
        Assert.assertEquals(0, st.capacite());
    }

    @Test
    public void testGetters_nbBL() {
        Assert.assertEquals(this.BORNES_NB, this.s.nbBornesLibres());
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
        IVelo v = Mockito.mock(IVelo.class);
        IRegistre reg = Mockito.mock(IRegistre.class);
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
        this.addBikes(this.BORNES_NB);

        int nbOfNull = 0, nbOfNotNull = 0;
        for (int i = 1; i <= this.s.capacite(); i++) {
            if (this.s.veloALaBorne(i) == null) {
                nbOfNull++;
            } else {
                nbOfNotNull++;
            }
        }

        Assert.assertEquals(0, nbOfNull);
        Assert.assertEquals(this.BORNES_NB, nbOfNotNull);
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
    public void testEmprunterVelo_Empty_RegOk_AbOk() {
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.nbEmpruntsEnCours(ArgumentMatchers.<Abonne>any())).thenReturn(0);
        this.s.setRegistre(reg);

        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(false);

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbKo() {
        this.addBikes(this.BORNES_NB);

        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(true);

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegKO_AbOk() {
        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(false);

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbOk() {
        this.addBikes(this.BORNES_NB);

        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(false);

        Assert.assertNotNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_NotEmpty_RegOk_AbOk_Out() {
        this.addBikes(this.BORNES_NB);

        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(false);

        Assert.assertNotNull(this.s.emprunterVelo(a, 1));
    }

    @Test
    public void testEmprunterVelo_MultipleBorrows() {
        this.addBikes(this.BORNES_NB);

        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.nbEmpruntsEnCours(ArgumentMatchers.<Abonne>any())).thenReturn(42);
        this.s.setRegistre(reg);

        Abonne a = Mockito.mock(Abonne.class);
        Mockito.when(a.estBloque()).thenReturn(false);

        Assert.assertNull(this.s.emprunterVelo(a, 1));
    }

    /* ---------- arrimerVelo ---------- */

    @Test
    public void testArrimerVelo_Success() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        Assert.assertEquals(0, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_VeloNull_BorneOk() {
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        Assert.assertEquals(-1, this.s.arrimerVelo(null, 1));
    }

    @Test
    public void testArrimerVelo_VeloOk_BorneOutOfBounds() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        Assert.assertEquals(-1, this.s.arrimerVelo(v, -1));
        Assert.assertEquals(-1, this.s.arrimerVelo(v, 1250));
    }

    @Test
    public void testArrimerVelo_RegistreKO() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);

        Assert.assertEquals(-2, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_BorneNotEmpty() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        this.s.arrimerVelo(v, 1);

        IVelo v2 = Mockito.mock(IVelo.class);
        Assert.assertEquals(-2, this.s.arrimerVelo(v2, 1));
    }

    @Test
    public void testArrimerVelo_CantArrimer() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(-1);
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(0);
        this.s.setRegistre(reg);

        Assert.assertEquals(-3, this.s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVelo_ErrorRetourner() {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);
        IRegistre reg = Mockito.mock(IRegistre.class);
        Mockito.when(reg.retourner(ArgumentMatchers.<IVelo>any(), anyLong())).thenReturn(-1);
        this.s.setRegistre(reg);

        Assert.assertEquals(-4, this.s.arrimerVelo(v, 1));
    }

    /* ---------- equilibrer ---------- */

    @Test
    public void testEquilibrer_Null() {
        this.s.equilibrer(null);

        Assert.assertEquals(this.BORNES_NB, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_EmptyStation() {
        Set<IVelo> velos = fillSet(2, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.BORNES_NB - 2, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_EmptyStation_BadBikes() {
        Set<IVelo> velos = fillSet(2, false);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.BORNES_NB, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_EmptyStation_Both() {
        Set<IVelo> velos = fillSet(2, true);
        Set<IVelo> v1 = fillSet(2, false);
        Set<IVelo> v2 = fillSet(2, true);
        velos.addAll(v1);
        velos.addAll(v2);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.BORNES_NB - 4, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Empty_MoreThanCapacity_Even() {
        Set<IVelo> velos = fillSet(this.BORNES_NB, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.BORNES_NB / 2, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Enough() {
        this.addBikes(this.BORNES_NB / 2);
        Assert.assertEquals(this.BORNES_NB / 2, this.s.nbBornesLibres());

        Set<IVelo> velos = fillSet(this.BORNES_NB, true);
        this.s.equilibrer(velos);

        Assert.assertEquals(this.BORNES_NB / 2, this.s.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Empty_MoreThanCapacity_Odd() {
        Station s1 = new Station("Stop the count", 10, 10, 21);
        Set<IVelo> velos = fillSet(this.BORNES_NB, true);
        s1.equilibrer(velos);

        Assert.assertEquals(21 / 2, s1.nbBornesLibres());
    }

    @Test
    public void testEquilibrer_Abime() {

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
    public void testMaintenant() {
        Assert.assertEquals(this.s.maintenant(), System.currentTimeMillis());
        long t1 = this.s.maintenant();
        Assert.assertTrue(t1 <= this.s.maintenant());
    }

}
