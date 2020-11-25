package fr.ufc.l3info.oprog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestsTP7 {
    final int CAPACITE = 10;

    // ***** Tests de la méthode emprunteur() *****

    IRegistre registre;
    Abonne valentin;
    IVelo becane;
    Station station;
    @Before
    public void init() throws IncorrectNameException {
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


}
