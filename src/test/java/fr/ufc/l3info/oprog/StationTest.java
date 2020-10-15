package fr.ufc.l3info.oprog;



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


    public IRegistre registreToujoursDAccord(){
        IRegistre registre = mock(IRegistre.class);
        when(registre.emprunter(ArgumentMatchers.<Abonne>any(),ArgumentMatchers.<IVelo>any(),anyLong())).thenReturn(0);
        when(registre.nbEmpruntsEnCours(ArgumentMatchers.<Abonne>any())).thenReturn(0);
        when(registre.retourner(ArgumentMatchers.<IVelo>any(),anyLong())).thenReturn(0);
        return registre;
    }




    /****************************************************
                        TESTS AVEC MOCKS
     ****************************************************/

    @Test
    public void arrimerOk(){
        IRegistre registre = registreToujoursDAccord();

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));
    }

    @Test
    public void emprunterOk() {
        IRegistre registre = registreToujoursDAccord();
        Abonne abonne = mock(Abonne.class);
        when(abonne.estBloque()).thenReturn(false);

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));

        assertNotNull(station.emprunterVelo(abonne,3));
        assertNull(station.emprunterVelo(abonne,3));
    }

    @Test
    public void emprunterAbonneBloque() {
        IRegistre registre = registreToujoursDAccord();

        Abonne abonne = mock(Abonne.class);
        when(abonne.estBloque()).thenReturn(true);

        station.setRegistre(registre);
        assertEquals(0,station.arrimerVelo(new Velo(),3));

        assertNull(station.emprunterVelo(abonne,3));
    }

    @Test
    public void emprunterSansRegistre(){

    }

    @Test
    public void emprunterTropEmprunts(){

    }

    @Test
    public void arrimerSansRegistre(){
        Abonne abonne = mock(Abonne.class);
        when(abonne.estBloque()).thenReturn(false);

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
