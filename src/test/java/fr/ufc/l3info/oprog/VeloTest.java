package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VeloTest {

    private Velo velo;

    @Before
    public void InitVelo(){
        velo = new Velo();
    }

    @Test
    public void ConstructeurKilometrage(){
        assertEquals(0,velo.kilometrage(),1e-3);
    }

    @Test
    public void ConstructeurTarif(){
        assertEquals(2.0,velo.tarif(),1e-3);
    }

    @Test
    public void ConstructeurEstDecrocher(){
        assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EtatArrimerDecrocher(){
        assertEquals(0,velo.arrimer());
        assertEquals(-1,velo.arrimer());
        assertEquals(0,velo.decrocher());
        assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EtatArrimerAbimerDecrocher(){
        assertEquals(0,velo.arrimer());
        assertFalse(velo.estAbime());
        velo.abimer();
        assertTrue(velo.estAbime());
        assertEquals(0,velo.decrocher());
        assertTrue(velo.estAbime());
    }

    @Test
    public void EtatAbimerReparer(){
        assertFalse(velo.estAbime());
        assertEquals(-2,velo.reparer());
        velo.abimer();
        assertTrue(velo.estAbime());
        assertEquals(0,velo.reparer());
        assertFalse(velo.estAbime());
    }


    @Test
    public void EtatAbimerAccrocherReparer(){
        velo.abimer();
        assertEquals(0,velo.arrimer());
        assertEquals(-1,velo.reparer());
        assertTrue(velo.estAbime());
    }

    @Test
    public void Parcourir(){
        velo.parcourir(101.5);
        assertEquals(101.5, velo.kilometrage(), 1e-3 );
        velo.parcourir(65.04);
        assertEquals(101.5+65.04, velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirAccroche(){
        velo.arrimer();
        velo.parcourir(100);
        assertEquals(0,velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirNegatif(){
        velo.parcourir(-10);
        assertEquals(0,velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirReviser(){
        velo.parcourir(100);
        assertEquals(100,velo.kilometrage(), 1e-3);
        assertEquals(0, velo.reviser());
        assertEquals(100,velo.kilometrage(), 1e-3);
        assertEquals(500,velo.prochaineRevision(),1e-3);
    }


    @Test
    public void AccrocherReviser(){
        velo.parcourir(104);
        assertEquals(0,velo.arrimer());
        assertEquals(-1,velo.reviser());
        assertEquals(500-104,velo.prochaineRevision(), 1e-3);

    }

    @Test
    public void abimerAccrocherReviser(){
        velo.abimer();
        assertEquals(0,velo.arrimer());
        assertEquals(-1,velo.reviser());
        assertTrue(velo.estAbime());
    }

    @Test
    public void ParcourirProchaineRevision(){
        velo.parcourir(200);
        assertEquals(300,velo.prochaineRevision(),1e-3);
        velo.parcourir(400);
        assertEquals(600,velo.kilometrage(),1e-3);
        assertTrue(velo.prochaineRevision() <= 0);
    }

    @Test
    public void ToStringMixte(){
        assertEquals("Vélo cadre mixte - 0.0 km",velo.toString());
        velo.parcourir(104.78);
        assertEquals("Vélo cadre mixte - 104.8 km",velo.toString());
        velo.parcourir(600);
        assertEquals("Vélo cadre mixte - 704.8 km (révision nécessaire)",velo.toString());
    }

    @Test
    public void ToStringHomme(){
        assertEquals("Vélo cadre homme - 0.0 km",new Velo('H').toString());
        assertEquals("Vélo cadre homme - 0.0 km",new Velo('h').toString());
    }

    @Test
    public void ToStringFemme(){
        assertEquals("Vélo cadre femme - 0.0 km",new Velo('F').toString());
        assertEquals("Vélo cadre femme - 0.0 km",new Velo('f').toString());
    }



}
