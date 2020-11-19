package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JRegistreTest {

    private FabriqueVelo fabrique;
    private IRegistre registre;
    private Abonne abonne;
    private IVelo velo;
    final private String[] options = {
            "CADRE_ALUMINIUM",
            "SUSPENSION_AVANT",
            "SUSPENSION_ARRIERE",
            "FREINS_DISQUE",
            "ASSISTANCE_ELECTRIQUE"
    };

    private JRegistre jr;

    private Velo v;
    private Abonne a;

    public long maintenantMoinsNMin(long min){
        return System.currentTimeMillis() - 1000*60*min;
    }

    @Before
    public void initRegistreVide() throws IncorrectNameException {
        registre = new JRegistre();

        jr = new JRegistre();
        v = new Velo();
        a = new Abonne("François Poguet");
    }

    @Before
    public void initAbonne() throws IncorrectNameException {
        abonne = new Abonne("John");
    }

    @Before
    public void initVelo(){
        fabrique = FabriqueVelo.getInstance();
        velo = fabrique.construire('m',options[(int)(Math.random()*options.length)]);
    }

    @Test
    public void registreVide() {
        assertEquals(0,registre.nbEmpruntsEnCours(abonne));
    }

    @Test
    public void facturationSansEmprunt(){
        assertEquals(0,registre.facturation(abonne,0,maintenantMoinsNMin(0)),1e-3);
    }

    @Test
    public void emprunter(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
    }

    @Test
    public void emprunterDejaEmprunter() throws IncorrectNameException {
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(-2,registre.emprunter(abonne,velo,maintenantMoinsNMin(0)));
        assertEquals(-2,registre.emprunter(new Abonne("Keyser Söze"),velo,maintenantMoinsNMin(2)));
    }

    @Test
    public void emprunterDejaEmprunteDansLePasse(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(0)));
        assertEquals(-2,registre.emprunter(abonne,velo,maintenantMoinsNMin(4)));
    }

    @Test
    public void emprunterVeloNull(){
        assertEquals(-1,registre.emprunter(abonne,null,maintenantMoinsNMin(10)));
    }

    @Test
    public void emprunterAbonneNull(){
            assertEquals(-1,registre.emprunter(null,velo,maintenantMoinsNMin(3)));
    }

    @Test
    public void emprunterAvantAutreEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(5)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(2)));
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(-3,registre.retourner(velo,maintenantMoinsNMin(3)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(0)));
    }

    @Test
    public void nombreEmpruntsApresRetour(){
        assertEquals(0,registre.nbEmpruntsEnCours(abonne));
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(1,registre.nbEmpruntsEnCours(abonne));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(0)));
        assertEquals(0,registre.nbEmpruntsEnCours(abonne));
    }

    @Test
    public void retournerVeloNull(){
        assertEquals(-1,registre.retourner(null,maintenantMoinsNMin(0)));
    }

    @Test
    public void retournerNonEmprunte(){
        assertEquals(-2,registre.retourner(velo,maintenantMoinsNMin(0)));
    }

    @Test
    public void retournerAvantEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(-3,registre.retourner(velo,maintenantMoinsNMin(11)));
    }

    @Test
    public void retournerChevaucheAutreEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(0)));

        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(20)));
        assertEquals(-3,registre.retourner(velo,maintenantMoinsNMin(8)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(15)));
    }

    @Test
    public void nbEmpruntEnCours(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(-2,registre.emprunter(abonne,velo,maintenantMoinsNMin(9)));

        assertEquals(0,registre.emprunter(abonne,new Velo('h'),maintenantMoinsNMin(8)));
        assertEquals(0,registre.emprunter(abonne,new Velo('f'),maintenantMoinsNMin(7)));

        assertEquals(3,registre.nbEmpruntsEnCours(abonne));
    }

    @Test
    public void nbEmpruntPlusieursAbonne() throws IncorrectNameException {
        Abonne abonne1 = new Abonne("Jack");

        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(0,registre.emprunter(abonne1,new Velo('h'),maintenantMoinsNMin(9)));
        assertEquals(0,registre.emprunter(abonne,new Velo('f'),maintenantMoinsNMin(8)));

        assertEquals(2,registre.nbEmpruntsEnCours(abonne));
        assertEquals(1,registre.nbEmpruntsEnCours(abonne1));
    }

    @Test
    public void nbEmpruntFutur(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(-10)));
        assertEquals(1,registre.nbEmpruntsEnCours(abonne));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(-12)));
        assertEquals(0,registre.nbEmpruntsEnCours(abonne));
    }

    @Test
    public void facturationUnEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(60)));

        assertEquals(2 * velo.tarif(),registre.facturation(abonne,maintenantMoinsNMin(200),maintenantMoinsNMin(0)),1e-3);
        assertEquals(0,registre.facturation(abonne,maintenantMoinsNMin(200),maintenantMoinsNMin(70)),1e-3);
    }

    @Test
    public void facturationAvantEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(60)));

        assertEquals(0,registre.facturation(abonne,maintenantMoinsNMin(300),maintenantMoinsNMin(200)),1e-3);
    }

    @Test
    public void facturationPlusieursEmprunt(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(60)));

        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(40)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(10)));

        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(250)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(235)));

        assertEquals(2.75 * velo.tarif(),registre.facturation(abonne,maintenantMoinsNMin(500),maintenantMoinsNMin(0)),1e-3);
        assertEquals(0.25 * velo.tarif(),registre.facturation(abonne,maintenantMoinsNMin(300),maintenantMoinsNMin(200)),1e-3);
        assertEquals(2.5 * velo.tarif(),registre.facturation(abonne,maintenantMoinsNMin(100),maintenantMoinsNMin(0)),1e-3);
        assertEquals(0,registre.facturation(abonne,maintenantMoinsNMin(-10),maintenantMoinsNMin(-100)),1e-3);
    }

    @Test
    public void facturationPrecisMinute(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(114)));

        assertEquals(1.1 * velo.tarif(),registre.facturation(abonne,maintenantMoinsNMin(180),maintenantMoinsNMin(114)),1e-3);
    }

    @Test
    public void facturationEmpruntSimultane(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(60)));

        IVelo deuxieme = new Velo('f');
        assertEquals(0,registre.emprunter(abonne,deuxieme,maintenantMoinsNMin(150)));
        assertEquals(0,registre.retourner(deuxieme,maintenantMoinsNMin(70)));

        IVelo troisieme = new Velo('f');
        assertEquals(0,registre.emprunter(abonne,troisieme,maintenantMoinsNMin(170)));
        assertEquals(0,registre.retourner(troisieme,maintenantMoinsNMin(30)));

        assertEquals(2 * velo.tarif() + 1.333 * deuxieme.tarif() + 2.333 * troisieme.tarif(),registre.facturation(abonne,maintenantMoinsNMin(180),maintenantMoinsNMin(0)),1e-2);
        assertEquals(2 * velo.tarif() + 1.333 * deuxieme.tarif(),registre.facturation(abonne,maintenantMoinsNMin(160),maintenantMoinsNMin(40)),1e-2);

    }

    /* ---------- Emprunter ---------- */

    @Test
    public void testEmprunter_Good()  {
        int ret = this.jr.emprunter(this.a, this.v, System.currentTimeMillis());
        Assert.assertEquals(ret, 0);

        Velo v2 = new Velo();
        ret = this.jr.emprunter(this.a, v2, System.currentTimeMillis());
        Assert.assertEquals(ret, 0);

        Velo v3 = new Velo();
        ret = this.jr.emprunter(this.a, v3, System.currentTimeMillis() - 100);
        Assert.assertEquals(ret, 0);
    }

    @Test
    public void testEmprunter_Null() {
        int ret;

        ret = this.jr.emprunter(null, this.v, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);

        ret = this.jr.emprunter(this.a, null, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);

        ret = this.jr.emprunter(null, null, System.currentTimeMillis());
        Assert.assertEquals(ret, -1);
    }

    @Test
    public void testEmprunter_Already() throws IncorrectNameException {
        int ret;
        long date = System.currentTimeMillis();

        ret = this.jr.emprunter(this.a, this.v, date);
        Assert.assertEquals(ret, 0);

        // In progress
        ret = this.jr.emprunter(this.a, this.v, date);
        Assert.assertEquals(ret, -2);

        // In progress
        Abonne b = new Abonne("Lucas Cosson");
        ret = this.jr.emprunter(b, this.v, date + 10);
        Assert.assertEquals(ret, -2);

        // End
        this.jr.retourner(this.v, date + 100);
        ret = this.jr.emprunter(this.a, this.v, date);
        Assert.assertEquals(ret, -2);

        // End
        ret = this.jr.emprunter(this.a, this.v, date + 200);
        Assert.assertEquals(ret, 0);
    }

    /* ---------- Retourner ---------- */

    @Test
    public void testRetourner_Good() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);

        int ret;
        ret = this.jr.retourner(this.v, date + 5);
        Assert.assertEquals(ret, 0);

        this.jr.emprunter(this.a, this.v, date + 50);
        ret = this.jr.retourner(this.v, date + 100);
        Assert.assertEquals(ret, 0);
    }

    @Test
    public void testRetourner_Null() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);

        int ret;
        ret = this.jr.retourner(null, date + 5);
        Assert.assertEquals(ret, -1);
    }

    @Test
    public void testRetourner_Error() {
        long date = System.currentTimeMillis();

        int ret;
        ret = jr.retourner(this.v, date + 5);
        Assert.assertEquals(ret, -2);

        this.jr.emprunter(this.a, this.v, date + 10);
        ret = jr.retourner(this.v, date + 15);
        Assert.assertEquals(ret, 0);

        ret = jr.retourner(this.v, date + 20);
        Assert.assertEquals(ret, -2);
    }

    @Test
    public void testRetourner_Date() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);

        int ret;

        ret = this.jr.retourner(this.v, date - 20);
        Assert.assertEquals(ret, -3);
        ret = this.jr.retourner(this.v, date + 20);
        Assert.assertEquals(ret, 0);
    }

    /* ---------- nbEmpruntsEnCours ---------- */

    @Test
    public void testNbEmpruntsEnCours() {
        long date = System.currentTimeMillis();

        Assert.assertEquals(this.jr.nbEmpruntsEnCours(this.a), 0);

        this.jr.emprunter(this.a, this.v, date - 20);

        Assert.assertEquals(this.jr.nbEmpruntsEnCours(this.a), 1);

        Velo v2 = new Velo();
        this.jr.emprunter(this.a, v2, date - 10);

        Assert.assertEquals(this.jr.nbEmpruntsEnCours(this.a), 2);

        this.jr.retourner(v2, date - 5);

        Assert.assertEquals(this.jr.nbEmpruntsEnCours(this.a), 1);

        Velo v3 = new Velo();
        this.jr.emprunter(this.a, v3, date + 100000);
    }

    /* ---------- Facturation ---------- */

    @Test
    public void testFacturation_Nothing() {
        long date = System.currentTimeMillis();

        double ret = this.jr.facturation(this.a, date, date + 10);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturation_InProgress() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);

        double ret = this.jr.facturation(this.a, date, date + 10);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturation_One() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);
        this.jr.retourner(this.v, date + 1000*60*60);

        double ret = this.jr.facturation(this.a, date, date + 10000000);
        Assert.assertEquals(this.v.tarif(), ret, 0.001);
    }

    @Test
    public void testFacturation_OneBis() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);
        this.jr.retourner(this.v, date + 1000*60*30);

        double ret = this.jr.facturation(this.a, date, date + 10000000);
        Assert.assertEquals(this.v.tarif() / 2, ret, 0.001);
    }

    @Test
    public void testFacturation_DateOut() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);
        this.jr.retourner(this.v, date + 1000*60*30);

        double ret = this.jr.facturation(this.a, date, date + 300);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturation_Error() {
        long date = System.currentTimeMillis();

        this.jr.emprunter(this.a, this.v, date);
        this.jr.retourner(this.v, date + 1000*60*30);

        double ret = this.jr.facturation(this.a, date + 10000000, date);
        Assert.assertEquals(0, ret, 0.001);
    }

    @Test
    public void testFacturation_Null() {
        long date = System.currentTimeMillis();

        double ret = this.jr.facturation(null, date, date + 200);
        Assert.assertEquals(0, ret, 0.001);
    }

}
