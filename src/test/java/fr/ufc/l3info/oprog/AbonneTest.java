package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {

    final static String VALID_USER_NAME = "Steve Jobs";
    final static String VALID_RIB = "12345-98765-12345678912-21";
    final static String INVALID_RIB = "507317-SOLEIL";

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

    /* ---------- Création d'un nouvel abonné (uniquement nom) ---------- */

    @Test
    public void testNouvelAbonne_NomCorrect() throws IncorrectNameException {
        new Abonne("Steve");
        new Abonne("Steve-Jobs");
        Abonne a = new Abonne("  Steve Jobs-Travaux   ");
        Assert.assertEquals(a.getNom(), "Steve Jobs-Travaux");
        Abonne b = new Abonne("Stéve Jobs Àpple  ");
        Assert.assertEquals(b.getNom(), "Stéve Jobs Àpple");
    }

    @Test(expected = IncorrectNameException.class)
    public void testNouvelAbonne_NomNull() throws IncorrectNameException {
        new Abonne(null);
    }

    @Test(expected = IncorrectNameException.class)
    public void testNouvelAbonne_NomVide() throws IncorrectNameException {
        new Abonne("  ");
    }

    @Test(expected = IncorrectNameException.class)
    public void testNouvelAbonnee_NomIncorrect_Caracteres() throws IncorrectNameException {
        new Abonne("   Steve42_");
    }

    @Test(expected = IncorrectNameException.class)
    public void testNouvelAbonnee_NomIncorrect_Espaces() throws IncorrectNameException {
        new Abonne("Stéve  Jobs Àpple");
    }

    @Test(expected = IncorrectNameException.class)
    public void testNouvelAbonnee_NomIncorrect_Tirets() throws IncorrectNameException {
        new Abonne("Stéve--Jobs Àpple");
    }

    /* ---------- Création d'un nouvel abonné (nom et rib) ---------- */

    @Test
    public void testNouvelAbonne_RibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testNouvelAbonne_RibIncorrect_Format() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.INVALID_RIB);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNouvelAbonne_RibIncorrect_Cle() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, "12345-98765-12345678912-42");
        Assert.assertTrue(a.estBloque());
    }

    /* ---------- Getters ---------- */

    @Test
    public void testGetId() throws IncorrectNameException {
        Abonne first = new Abonne(AbonneTest.VALID_USER_NAME);
        Abonne second = new Abonne(AbonneTest.VALID_USER_NAME);
        Assert.assertEquals(first.getID() + 1, second.getID());
    }

    @Test
    public void testGetNom() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);
        Assert.assertEquals(AbonneTest.VALID_USER_NAME, a.getNom());
    }

    /* ---------- Mise à jour du RIB ---------- */

    @Test
    public void testMiseAJourRib_CorrectVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testMiseAJourRiv_NullVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);

        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testMiseAJourRib_CorrectVersCorrect_Bloque() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testMiseAJourRib_IncorrectVersCorrect_Bloque() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testMiseAJourRib_CorrectVersIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(AbonneTest.INVALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testMiseAJourRib_CorrectVersNull() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.estBloque());
        a.miseAJourRIB(null);
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testMiseAJourRib_IncorrectVersCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.INVALID_RIB);

        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB(AbonneTest.VALID_RIB);
        Assert.assertFalse(a.estBloque());
    }

    /* ---------- Bloquer un abonné ---------- */

    @Test
    public void testBloquer_RibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testBloquer_RibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.INVALID_RIB);

        Assert.assertTrue(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
    }

    /* ---------- Débloquer un abonné ---------- */

    @Test
    public void testDebloquer_Simple_OuiVersOui() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testDebloquer_Simple_NonVersOui() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testDebloquer_Echec_RibNull() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);

        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testDebloquer_Echec_RibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.INVALID_RIB);

        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }

    /* ---------- Égalité entre objets ---------- */

    @Test
    public void testEgalite_Correct() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);

        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void testEgalite_Incorrect_Diff() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);
        Abonne b = new Abonne("Steve Woz");

        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testEgalite_Incorrect_Null() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);

        Assert.assertFalse(a.equals(null));
    }

    @Test
    public void testEgalite_Incorrect_AutreObject() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);
        String sculley = "John Sculley";

        Assert.assertFalse(a.equals(sculley));
    }

    @Test
    public void testEgalite_Incorrect_MemeNom() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME);
        Abonne b = new Abonne(AbonneTest.VALID_USER_NAME);

        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testEgalite_Incorrect_MemeNomRib() throws IncorrectNameException {
        Abonne a = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);
        Abonne b = new Abonne(AbonneTest.VALID_USER_NAME, AbonneTest.VALID_RIB);

        Assert.assertFalse(a.equals(b));
    }
}
