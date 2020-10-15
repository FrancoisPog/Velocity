package fr.ufc.l3info.oprog;



import jdk.nashorn.internal.parser.TokenKind;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentMatchers;


import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StationTest {

    final String NOM = "Granvelle";
    final int CAPACITE = 5;
    final double LATITUDE = 30;
    final double LONGITUDE = 30;

    private Station station;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initStation(){
        station = new Station(NOM,LATITUDE,LONGITUDE, CAPACITE);
    }

    /****************************************************
                        TESTS SANS MOCKS
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
    public void toutesBornesLibres(){
        for(int i = 0 ; i < CAPACITE ; ++i){
            assertNull(station.veloALaBorne(i));
        }
    }

    @Test
    public void veloALaBorneMauvaiseBorne(){

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

    public IVelo createIVeloMock(int retArrimer){
        IVelo velo = mock(Velo.class);
        when(velo.arrimer()).thenReturn(retArrimer);
        return velo;
    }


    /****************************************************
                        TESTS AVEC MOCKS
     ****************************************************/



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
        IRegistre registre = createIRegistreMock(0,1,0);
        station.setRegistre(registre);

        Abonne abonne = createAbonneMock(false);

        IVelo velo = createIVeloMock(0);

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

        station.arrimerVelo(createIVeloMock(0),2);

        assertNull(station.emprunterVelo(abonne,2));

    }

    @Test
    public void arrimerOk(){
        IRegistre registre = createIRegistreMock(0,0,0);

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));
    }



    @Test
    public void arrimerSansRegistre(){
        assertEquals(-2,station.arrimerVelo(new Velo(),3));
    }

    @Test
    public void arrimerVeloNull(){

    }

    @Test
    public void arrimerMauvaiseBorne(){

    }

    @Test
    public void arrimerBorneNonLibre(){

    }

    @Test
    public void arrimerVeloDejaArrime(){

    }

    @Test
    public void arrimerErreurRegistre(){

    }








}
