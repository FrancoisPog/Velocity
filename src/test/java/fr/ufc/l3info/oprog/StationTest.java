package fr.ufc.l3info.oprog;



import jdk.nashorn.internal.parser.TokenKind;
import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentMatchers;


import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StationTest {

    final String NOM = "Granvelle";
    final int CAPACITE = 5;
    final double LATITUDE = 47.24547025801882;
    final double LONGITUDE = 5.9879826235144495;

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







}
