package fr.ufc.l3info.oprog;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {

    @Test
    public void NomValide() throws IncorrectNameException{
        new Abonne("JeAN-JÉrôme");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_DoubleTirets() throws IncorrectNameException {
        new Abonne("Jean--Marie");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Nombres() throws IncorrectNameException {
        new Abonne("Jean25");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_DoubleEspace() throws IncorrectNameException {
        new Abonne("Jean  Marie");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Symboles() throws IncorrectNameException {
        new Abonne("F@nny<3/");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_ElonMusk() throws IncorrectNameException {
        new Abonne("X AE A-12 Musk");
    }

    @Test (expected = IncorrectNameException.class)
    public void NomInvalide_Vide() throws IncorrectNameException {
        new Abonne("");
    }

    @Test
    public void RibValide() throws IncorrectNameException{
        String rib = "10278-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        assertFalse(a.estBloque());
    }

    @Test
    public void RibInvalide_MauvaiseCle() throws IncorrectNameException{
        String rib = "10278-08000-00022270603-72";
        Abonne a = new Abonne("John",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_Lettres() throws IncorrectNameException{
        String rib = "102A8-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_Symboles() throws IncorrectNameException{
        String rib = "102%8-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibInvalide_MauvaisFormat() throws IncorrectNameException{
        String rib = "10278 08000 00022270603 02";
        Abonne a = new Abonne("John",rib);
        assertTrue(a.estBloque());
    }

    @Test
    public void RibNull() throws IncorrectNameException {
        Abonne a = new Abonne("Fred");
        assertTrue(a.estBloque());
        a.miseAJourRIB(null);
        assertTrue(a.estBloque());
    }

    @Test
    public void Id() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        assertTrue(fred.getID() >= 0);
        assertEquals(jube.getID(), fred.getID()+1);
    }

    @Test
    public void Equals() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        assertTrue(fred.equals(fred));
        assertFalse(jube.equals(fred));
    }

    @Test
    public void Equals_MauvaisType() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");

        assertFalse(fred.equals(new String()));
        assertTrue(fred.equals((Object)fred));
    }

    @Test
    public void Equals_null() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");

        assertFalse(fred.equals(null));
    }

    @Test
    public void Hash() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        assertEquals(fred.hashCode(),fred.hashCode());
        Assert.assertNotEquals(jube.hashCode(),fred.hashCode());
        Assert.assertNotEquals(jube.hashCode(),new Object().hashCode());
    }

    @Test
    public void Nom() throws IncorrectNameException {
        Abonne a = new Abonne("Fréd");
        assertEquals("Fréd", a.getNom());
    }

    @Test(expected = IncorrectNameException.class)
    public void NomNull() throws IncorrectNameException {
        Abonne a = new Abonne(null);
    }

    @Test
    public void NomTrim() throws IncorrectNameException {
        Abonne a = new Abonne("   Pog    ");
        assertEquals(a.getNom(),"Pog");
    }


    @Test
    public void MiseAJourRib() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs");
        assertTrue(a.estBloque());
        a.miseAJourRIB("10278-08000-00022270603-02");
        assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_invalide() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs","10278-08000-00022270603-02");
        assertFalse(a.estBloque());
        a.miseAJourRIB("XX");
        assertFalse(a.estBloque());
    }

    @Test
    public void MiseAJourRib_invalideBis() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs");
        assertTrue(a.estBloque());
        a.miseAJourRIB("XX");
        assertTrue(a.estBloque());
    }

    @Test
    public void BloquerDebloquer() throws IncorrectNameException{
        Abonne a = new Abonne("Neil Armstrong","10278-08000-00022270603-02");
        assertFalse(a.estBloque());
        a.bloquer();
        assertTrue(a.estBloque());
        a.debloquer();
        assertFalse(a.estBloque());

    }

    @Test
    public void BlockerDebloquerSansRib() throws IncorrectNameException{
        Abonne a = new Abonne("Neil Armstrong");
        assertTrue(a.estBloque());
        a.debloquer();
        assertTrue(a.estBloque());

    }

    public void BlockerMAJRib() throws IncorrectNameException{
        Abonne a = new Abonne("Brendan Eich");
        assertTrue(a.estBloque());
        a.bloquer();
        a.miseAJourRIB("10278-08000-00022270603-02");
        assertTrue(a.estBloque());
    }



















}
