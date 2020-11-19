package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VeloTest {

    private Velo velo;
    Velo vM;

    @Before
    public void InitVelo(){
        velo = new Velo();
        this.vM = new Velo();
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

    /* ---------- Création d'un nouveau vélo ---------- */

    public void checkInitialValues(Velo v) {
        Assert.assertEquals(v.kilometrage(), 0.0, 0);
        Assert.assertFalse(v.estAbime());
        Assert.assertEquals(v.decrocher(), -1);
        Assert.assertEquals(v.tarif(), 2.0, 0);
    }

    @Test
    public void testNouveauVelo_Defaut() {
        Velo vCreated = new Velo();

        Assert.assertEquals(vCreated.toString(), "Vélo cadre mixte - 0.0 km");
        checkInitialValues(vCreated);
    }

    @Test
    public void testNouveauVelo_Femme_LowerCase() {
        Velo vCreated = new Velo('f');

        Assert.assertEquals(vCreated.toString(), "Vélo cadre femme - 0.0 km");
        checkInitialValues(vCreated);
    }

    @Test
    public void testNouveauVelo_Femme_UpperCase() {
        Velo vCreated = new Velo('F');

        Assert.assertEquals(vCreated.toString(), "Vélo cadre femme - 0.0 km");
        checkInitialValues(vCreated);
    }

    @Test
    public void testNouveauVelo_Homme_LowerCase() {
        Velo vCreated = new Velo('h');

        Assert.assertEquals(vCreated.toString(), "Vélo cadre homme - 0.0 km");
        checkInitialValues(vCreated);
    }

    @Test
    public void testNouveauVelo_Homme_UpperCase() {
        Velo vCreated = new Velo('h');

        Assert.assertEquals(vCreated.toString(), "Vélo cadre homme - 0.0 km");
        checkInitialValues(vCreated);
    }

    @Test
    public void testNouveauVelo_Diff() {
        Velo vCreated = new Velo('j');

        Assert.assertEquals(vCreated.toString(), "Vélo cadre mixte - 0.0 km");
        checkInitialValues(vCreated);
    }

    /* ---------- Évolution de l'état du vélo ---------- */

    @Test
    public void testLifeCycle_First() {
        Assert.assertEquals(this.vM.decrocher(), -1);
        Assert.assertEquals(this.vM.arrimer(), 0);
    }

    @Test
    public void testLifeCycle_Second() {
        Assert.assertEquals(this.vM.arrimer(), 0);
        this.vM.abimer();
        Assert.assertTrue(this.vM.estAbime());
    }

    @Test
    public void testLifeCycle_Third() {
        this.vM.abimer();
        Assert.assertTrue(this.vM.estAbime());
        Assert.assertEquals(this.vM.arrimer(), 0);
        Assert.assertTrue(this.vM.estAbime());
        Assert.assertEquals(this.vM.decrocher(), 0);
    }

    @Test
    public void testLifeCycle_Fourth() {
        Assert.assertEquals(this.vM.arrimer(), 0);
        Assert.assertEquals(this.vM.arrimer(), -1);
        Assert.assertEquals(this.vM.decrocher(), 0);
        Assert.assertEquals(this.vM.decrocher(), -1);
    }

    @Test
    public void testLifeCycle_Fith() {
        Assert.assertEquals(this.vM.reparer(), -2);
        this.vM.abimer();
        Assert.assertEquals(this.vM.reparer(), 0);
        Assert.assertFalse(this.vM.estAbime());
    }

    @Test
    public void testLifeCycle_Sixth() {
        this.vM.abimer();
        Assert.assertTrue(this.vM.estAbime());
        this.vM.abimer();
        Assert.assertTrue(this.vM.estAbime());
        Assert.assertEquals(this.vM.arrimer(), 0);
        Assert.assertEquals(this.vM.reparer(), -1);
        Assert.assertEquals(this.vM.decrocher(), 0);
        Assert.assertEquals(this.vM.reparer(), 0);
        Assert.assertFalse(this.vM.estAbime());
    }

    /* ---------- Évolution des kms ---------- */

    @Test
    public void testKilometers_First() {
        Assert.assertEquals(this.vM.prochaineRevision(), 500, 0);
        this.vM.parcourir(300);
        Assert.assertEquals(this.vM.prochaineRevision(), 200, 0.001);
        Assert.assertEquals(this.vM.kilometrage(), 300, 0.001);
        this.vM.parcourir(300);
        Assert.assertEquals(this.vM.prochaineRevision(), -100, 0.001);
        Assert.assertEquals(this.vM.kilometrage(), 600, 0.001);
    }

    @Test
    public void testKilometers_Second() {
        this.vM.parcourir(100);
        Assert.assertEquals(this.vM.kilometrage(), 100, 0.001);
        this.vM.arrimer();
        this.vM.parcourir(300);
        Assert.assertEquals(this.vM.kilometrage(), 100, 0.001);
        this.vM.decrocher();
        this.vM.parcourir(300);
        Assert.assertEquals(this.vM.kilometrage(), 400, 0.001);
    }

    @Test
    public void testKilometers_Third() {
        this.vM.parcourir(490);
        Assert.assertEquals(this.vM.kilometrage(), 490, 0.001);
        Assert.assertEquals(this.vM.prochaineRevision(), 10, 0.001);
        Assert.assertEquals(this.vM.reviser(), 0, 0);
        Assert.assertEquals(this.vM.prochaineRevision(), 500, 0.001);
    }

    @Test
    public void testKilometers_Fourth() {
        this.vM.parcourir(300);
        this.vM.arrimer();
        Assert.assertEquals(this.vM.reviser(), -1, 0);
        Assert.assertEquals(this.vM.prochaineRevision(), 200, 0.001);
    }

    @Test
    public void testKilometers_Fith() {
        this.vM.parcourir(400);
        this.vM.abimer();
        Assert.assertTrue(this.vM.estAbime());
        Assert.assertEquals(this.vM.reviser(), 0, 0);
        Assert.assertFalse(this.vM.estAbime());
        Assert.assertEquals(this.vM.prochaineRevision(), 500, 0.001);
        this.vM.parcourir(600);
        Assert.assertEquals(this.vM.prochaineRevision(), -100, 0.001);
    }

    @Test
    public void testKilometers_Sixth() {
        this.vM.parcourir(42);
        Assert.assertEquals(this.vM.kilometrage(), 42, 0.001);
        this.vM.parcourir(-24);
        Assert.assertEquals(this.vM.kilometrage(), 42, 0.001);
        this.vM.parcourir(10);
        Assert.assertEquals(this.vM.kilometrage(), 52, 0.001);
    }

    @Test
    public void testKilometers_Seventh() {
        this.vM.parcourir(400);
        Assert.assertEquals(this.vM.toString(), "Vélo cadre mixte - 400.0 km");
        this.vM.parcourir(100.5168);
        Assert.assertEquals(this.vM.toString(), "Vélo cadre mixte - 500.5 km (révision nécessaire)");
        this.vM.reviser();
        Assert.assertEquals(this.vM.toString(), "Vélo cadre mixte - 500.5 km");
    }

}
