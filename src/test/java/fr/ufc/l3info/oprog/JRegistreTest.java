package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JRegistreTest {

    private IRegistre registre;
    private Abonne abonne;
    private IVelo velo;

    public long maintenantMoinsNMin(long min){
        return System.currentTimeMillis() - 1000*60*min;
    }

    @Before
    public void initRegistreVide(){
        registre = new JRegistre();
    }

    @Before
    public void initAbonne() throws IncorrectNameException {
        abonne = new Abonne("John");
    }

    @Before
    public void initVelo(){
        velo = new Velo('m');
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

        assertEquals(4.0,registre.facturation(abonne,maintenantMoinsNMin(200),maintenantMoinsNMin(0)),1e-3);
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

        assertEquals(5.5,registre.facturation(abonne,maintenantMoinsNMin(500),maintenantMoinsNMin(0)),1e-3);
        assertEquals(0.5,registre.facturation(abonne,maintenantMoinsNMin(300),maintenantMoinsNMin(200)),1e-3);
        assertEquals(5,registre.facturation(abonne,maintenantMoinsNMin(100),maintenantMoinsNMin(0)),1e-3);
        assertEquals(0,registre.facturation(abonne,maintenantMoinsNMin(-10),maintenantMoinsNMin(-100)),1e-3);
    }

    @Test
    public void facturationPrecisMinute(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(180)));
        assertEquals(0,registre.retourner(velo,maintenantMoinsNMin(114)));

        assertEquals(2.2,registre.facturation(abonne,maintenantMoinsNMin(180),maintenantMoinsNMin(114)),1e-3);
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

        assertEquals(11.33,registre.facturation(abonne,maintenantMoinsNMin(180),maintenantMoinsNMin(0)),1e-2);
        assertEquals(6.67,registre.facturation(abonne,maintenantMoinsNMin(160),maintenantMoinsNMin(40)),1e-2);

    }



}
