package fr.ufc.l3info.oprog;


import org.junit.Assert;
import org.junit.Test;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {

    @Test
    public void ValidName() throws IncorrectNameException{
        new Abonne("JeAN-JÉrôme");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_doubleHyphens() throws IncorrectNameException {
        new Abonne("Jean--Marie");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_numbers() throws IncorrectNameException {
        new Abonne("Jean25");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_doubleSpaces() throws IncorrectNameException {
        new Abonne("Jean  Marie");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_symbol() throws IncorrectNameException {
        new Abonne("F@nny<3/");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_elonMusk() throws IncorrectNameException {
        new Abonne("X AE A-12 Musk");
    }

    @Test (expected = IncorrectNameException.class)
    public void InvalidName_empty() throws IncorrectNameException {
        new Abonne("");
    }

    @Test
    public void ValidRIB() throws IncorrectNameException{
        String rib = "10278-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void InvalidRIB_letters() throws IncorrectNameException{
        String rib = "102A8-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void InvalidRIB_symbols() throws IncorrectNameException{
        String rib = "102%8-08000-00022270603-02";
        Abonne a = new Abonne("John",rib);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void InvalidRIB_badFormat() throws IncorrectNameException{
        String rib = "10278 08000 00022270603 02";
        Abonne a = new Abonne("John",rib);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void Id() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        Assert.assertTrue(fred.getID() >= 0);
        Assert.assertEquals(jube.getID(), fred.getID()+1);
    }

    @Test
    public void Equals() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        Assert.assertTrue(fred.equals(fred));
        Assert.assertFalse(jube.equals(fred));
        Assert.assertFalse(jube.equals(new String()));
        Assert.assertTrue(fred.equals((Object)fred));
    }

    @Test
    public void Equals_diffType() throws IncorrectNameException{
        Abonne fred = new Abonne("Frédéric Dadeau");
        Abonne jube = new Abonne("Julien Bernard");

        Assert.assertFalse(jube.equals(new String()));
        Assert.assertTrue(fred.equals((Object)fred));
    }


    @Test
    public void Name() throws IncorrectNameException {
        Abonne a = new Abonne("Fréd");
        Assert.assertEquals("Fréd", a.getNom());
    }

    @Test
    public void UpdateRIB() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs");
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("10278-08000-00022270603-02");
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void UpdateRIB_invalid() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs","10278-08000-00022270603-02");
        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB("XX");
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void UpdateRIB_invalidBis() throws  IncorrectNameException{
        Abonne a = new Abonne("Steve Jobs");
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("XX");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void BlockUnblock() throws IncorrectNameException{
        Abonne a = new Abonne("Neil Armstrong","10278-08000-00022270603-02");
        Assert.assertFalse(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());

    }





}
