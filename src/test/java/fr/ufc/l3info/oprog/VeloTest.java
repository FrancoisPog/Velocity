package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VeloTest {

    private Velo velo;

    @Before
    public void InitVelo(){
        velo = new Velo();
    }

    @Test
    public void ConstructeurKilometrage(){
        Assert.assertEquals(0,velo.kilometrage(),1e-3);
    }

    @Test
    public void ConstructeurTarif(){
        Assert.assertEquals(0.2,velo.tarif(),1e-3);
    }

    @Test
    public void ConstructeurEstDecrocher(){
        Assert.assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EtatArrimerDecrocher(){
        Assert.assertEquals(0,velo.arrimer());
        Assert.assertEquals(-1,velo.arrimer());
        Assert.assertEquals(0,velo.decrocher());
        Assert.assertEquals(-1,velo.decrocher());
    }

    @Test
    public void EtatArrimerAbimerDecrocher(){
        Assert.assertEquals(0,velo.arrimer());
        Assert.assertFalse(velo.estAbime());
        velo.abimer();
        Assert.assertTrue(velo.estAbime());
        Assert.assertEquals(0,velo.decrocher());
        Assert.assertTrue(velo.estAbime());
    }

    @Test
    public void EtatAbimerReparer(){
        Assert.assertFalse(velo.estAbime());
        Assert.assertEquals(-2,velo.reparer());
        velo.abimer();
        Assert.assertTrue(velo.estAbime());
        Assert.assertEquals(0,velo.reparer());
        Assert.assertFalse(velo.estAbime());
    }


    @Test
    public void EtatAbimerAccrocherReparer(){
        velo.abimer();
        Assert.assertEquals(0,velo.arrimer());
        Assert.assertEquals(-1,velo.reparer());
        Assert.assertTrue(velo.estAbime());
    }

    @Test
    public void Parcourir(){
        velo.parcourir(101.5);
        Assert.assertEquals(101.5, velo.kilometrage(), 1e-3 );
        velo.parcourir(65.04);
        Assert.assertEquals(101.5+65.04, velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirAccroche(){
        velo.arrimer();
        velo.parcourir(100);
        Assert.assertEquals(0,velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirNegatif(){
        velo.parcourir(-10);
        Assert.assertEquals(0,velo.kilometrage(), 1e-3);
    }

    @Test
    public void ParcourirReviser(){
        velo.parcourir(100);
        Assert.assertEquals(100,velo.kilometrage(), 1e-3);
        Assert.assertEquals(0, velo.reviser());
        Assert.assertEquals(0,velo.kilometrage(), 1e-3);
    }

    @Test
    public void AccrocherReviser(){
        velo.arrimer();
        Assert.assertEquals(-1,velo.reviser());
    }

    @Test
    public void ParcourirProchaineRevision(){
        velo.parcourir(200);
        Assert.assertEquals(300,velo.prochaineRevision(),1e-3);
        velo.parcourir(400);
        Assert.assertEquals(600,velo.kilometrage(),1e-3);
        Assert.assertTrue(velo.prochaineRevision() <= 0);
    }

    @Test
    public void ToStringMixte(){
        Assert.assertEquals("Vélo cadre mixte - 0.0 km",velo.toString());
        velo.parcourir(104.78);
        Assert.assertEquals("Vélo cadre mixte - 104.8 km",velo.toString());
        velo.parcourir(600);
        Assert.assertEquals("Vélo cadre mixte - 704.8 km (révision nécessaire)",velo.toString());
    }

    @Test
    public void ToStringHomme(){
        Assert.assertEquals("Vélo cadre homme - 0.0 km",new Velo('H').toString());
    }

    @Test
    public void ToStringFemme(){
        Assert.assertEquals("Vélo cadre femme - 0.0 km",new Velo('F').toString());
    }



}
