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
    public void emprunterDejaEmprunter(){
        assertEquals(0,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));
        assertEquals(-2,registre.emprunter(abonne,velo,maintenantMoinsNMin(0)));
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
    public void retournerNomEmprunter(){
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
        assertEquals(-2,registre.emprunter(abonne,velo,maintenantMoinsNMin(10)));

        assertEquals(0,registre.emprunter(abonne,new Velo('h'),maintenantMoinsNMin(10)));
        assertEquals(0,registre.emprunter(abonne,new Velo('f'),maintenantMoinsNMin(10)));

        assertEquals(3,registre.nbEmpruntsEnCours(abonne));
    }

    @Test
    public void nbEmpruntPlusieursAbonne() throws IncorrectNameException {
        Abonne abonne1 = new Abonne("Jack");
    }

}
