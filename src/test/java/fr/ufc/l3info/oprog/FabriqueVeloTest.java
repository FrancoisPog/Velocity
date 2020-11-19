package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Constructor;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
import java.util.regex.*;

public class FabriqueVeloTest {

    final String regex_toString = "^Vélo cadre (homme|femme|mixte)(, (cadre aluminium|freins à disque|assistance électrique|suspension arrière|suspension avant)){0,5} - \\d+.\\d km$";

    private FabriqueVelo fabrique;

    final String[][] options = {
            {"CADRE_ALUMINIUM", "cadre aluminium"},
            {"SUSPENSION_AVANT", "suspension avant"},
            {"SUSPENSION_ARRIERE", "suspension arrière"},
            {"FREINS_DISQUE", "freins à disque"},
            {"ASSISTANCE_ELECTRIQUE", "assistance électrique"}
    };

    final private FabriqueVelo f = FabriqueVelo.getInstance();
    final private String DEFAULT_TO_STRING = "Vélo cadre mixte - 0.0 km";

    @Before
    public void initFabrique() {
        fabrique = FabriqueVelo.getInstance();
    }

    @Test
    public void notNull() {
        assertNotNull(fabrique);
    }

    @Test
    public void constructeurPrive() {
        for (Constructor c : FabriqueVelo.class.getDeclaredConstructors()) {
            assertFalse(c.isAccessible());
        }
    }

    @Test
    public void uneSeuleInstance() {
        assertEquals(FabriqueVelo.getInstance(), FabriqueVelo.getInstance());
    }

    @Test
    public void veloNotNull() {
        assertNotNull(fabrique.construire('h', "ASSISTANCE_ELECTRIQUE"));
    }

    @Test
    public void optionValide(){
        IVelo velo = fabrique.construire('f',"CADRE_ALUMINIUM");
        assertTrue(Pattern.matches(regex_toString,velo.toString()));
        assertTrue(velo.toString().contains("cadre aluminium"));
        assertEquals(2,velo.toString().split(",").length);
        assertEquals(2.2,velo.tarif(),1e-3);
    }


    @Test
    public void memeOptionPlusieursFois() {

        for (int i = 0; i < options.length; i++) {
            String[] opt = new String[(i + 1) * 2];

            for (int j = 0; j <= i * 2; j = j + 2) {
                opt[j] = options[j / 2][0];
                opt[j + 1] = options[j / 2][0];
            }

            IVelo velo = fabrique.construire('m', opt);

            assertTrue(Pattern.matches(regex_toString, velo.toString()));
            assertEquals(i + 2, velo.toString().split(",").length);

            for (int j = 0; j < i; j++) {
                assertTrue(velo.toString().contains(options[j][1]));
            }

        }

    }

    @Test
    public void optionCadreHomme() {
        assertEquals("Vélo cadre homme, assistance électrique - 0.0 km", fabrique.construire('H', "ASSISTANCE_ELECTRIQUE").toString());
        assertEquals("Vélo cadre homme, freins à disque - 0.0 km", fabrique.construire('h', "FREINS_DISQUE").toString());
    }

    @Test
    public void optionCadreFemme() {
        IVelo velo = fabrique.construire('F', "FREINS_DISQUE");
        assertEquals("Vélo cadre femme, freins à disque - 0.0 km", velo.toString());
        assertEquals(2.3, velo.tarif(),1e-3);
    }

    @Test
    public void sansOption() {
        assertEquals("Vélo cadre mixte - 0.0 km", fabrique.construire('\0').toString());
    }

    @Test
    public void optionsNull() {
        IVelo velo = fabrique.construire('H', null, "SUSPENSION_ARRIERE", null);
        assertEquals("Vélo cadre homme, suspension arrière - 0.0 km", velo.toString());
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void optionUnknown() {
        IVelo velo = fabrique.construire('F', "arzrgg", "SUSPENSION_ARRIERE", "rgreg");
        assertEquals("Vélo cadre femme, suspension arrière - 0.0 km", velo.toString());
        assertEquals(2.5, velo.tarif(), 1e-3);
    }

    @Test
    public void optionNull() {
        IVelo velo = fabrique.construire('f', null);
        assertEquals("Vélo cadre femme - 0.0 km", velo.toString());
        assertEquals(2.0, velo.tarif(), 1e-3);
    }

    @Test
    public void toutesOptions(){
        IVelo velo = fabrique.construire('\\',"CADRE_ALUMINIUM",null,"FREINS","FREINS_DISQUE","SUSPENSION_ARRIERE","SUSPENSION_AVANT","ASSISTANCE_ELECTRIQUE","SUSPENSION_ARRIERE");
        assertTrue(Pattern.matches(regex_toString,velo.toString()));
        assertEquals(5.5,velo.tarif(),1e-3);
        assertEquals(6,velo.toString().split(",").length);
        for(String[] opts : options){
            assertTrue(velo.toString().contains(opts[1]));
        }
        assertTrue(velo.toString().startsWith("Vélo cadre mixte"));
    }

    /* ---------- Fabrique Vélo ---------- */

    @Test
    public void testCreateSeveralFabrique() {
        FabriqueVelo f2 = FabriqueVelo.getInstance();
        Assert.assertEquals(f, f2);
    }

    /* ---------- Création d'un vélo avec changement de cadres ---------- */

    @Test
    public void testCreateVelo_MAN() {
        IVelo v = f.construire('H');
        Assert.assertEquals(v.toString(), "Vélo cadre homme - 0.0 km");
    }

    @Test
    public void testCreateVelo_man() {
        IVelo v = f.construire('h');
        Assert.assertEquals(v.toString(), "Vélo cadre homme - 0.0 km");
    }

    @Test
    public void testCreateVelo_WOMAN() {
        IVelo v = f.construire('F');
        Assert.assertEquals(v.toString(), "Vélo cadre femme - 0.0 km");
    }

    @Test
    public void testCreateVelo_woman() {
        IVelo v = f.construire('f');
        Assert.assertEquals(v.toString(), "Vélo cadre femme - 0.0 km");
    }

    @Test
    public void testCreateVelo_mixed() {
        IVelo v = f.construire('P');
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    /* ---------- Création d'un vélo avec des options NULL ---------- */

    @Test
    public void testCreateVelo_OneNull() {
        IVelo v = f.construire('m', null);
        Assert.assertEquals(v.toString(), DEFAULT_TO_STRING);
    }

    @Test
    public void testCreateVelo_SeveralNull() {
        IVelo v = f.construire('m', null, null);
        Assert.assertEquals(v.toString(), DEFAULT_TO_STRING);
    }

    @Test
    public void testCreateVelo_SeveralNullAndValues() {
        IVelo v = f.construire('m', null, null, "CADRE_ALUMINIUM");
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    /* ---------- Création d'un vélo simple ---------- */

    @Test
    public void testCreateVelo_Simple() {
        IVelo v = f.construire('m');
        Assert.assertEquals(v.tarif(), 2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Cadre aluminium ---------- */

    @Test
    public void testCreateVelo_CadreAlu_Once() {
        IVelo v = f.construire('m', "CADRE_ALUMINIUM");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    @Test
    public void testCreateVelo_CadreAlu_Several() {
        IVelo v = f.construire('m', "CADRE_ALUMINIUM", "CADRE_ALUMINIUM");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Freins disque ---------- */

    @Test
    public void testCreateVelo_FreinDisque_Once() {
        IVelo v = f.construire('m', "FREINS_DISQUE");
        Assert.assertEquals(v.tarif(), 2.3, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    @Test
    public void testCreateVelo_FreinDisque_Several() {
        IVelo v = f.construire('m', "FREINS_DISQUE", "FREINS_DISQUE");
        Assert.assertEquals(v.tarif(), 2.3, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Suspension avant ---------- */

    @Test
    public void testCreateVelo_SuspensionAvant_Once() {
        IVelo v = f.construire('m', "SUSPENSION_AVANT");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionAvant_Several() {
        IVelo v = f.construire('m', "SUSPENSION_AVANT", "SUSPENSION_AVANT");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Suspension arrière ---------- */

    @Test
    public void testCreateVelo_SuspensionArriere_Once() {
        IVelo v = f.construire('m', "SUSPENSION_ARRIERE");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    @Test
    public void testCreateVelo_SuspensionArriere_Several() {
        IVelo v = f.construire('m', "SUSPENSION_ARRIERE", "SUSPENSION_ARRIERE");
        Assert.assertEquals(v.tarif(), 2.5, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Assistance électrique ---------- */

    @Test
    public void testCreateVelo_AssistanceElectrique_Once() {
        IVelo v = f.construire('m', "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals(v.tarif(), 4.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }

    @Test
    public void testCreateVelo_AssistanceElectrique_Several() {
        IVelo v = f.construire('m', "ASSISTANCE_ELECTRIQUE", "ASSISTANCE_ELECTRIQUE");
        Assert.assertEquals(v.tarif(), 4.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }

    /* ---------- Création d'un vélo | Option : Autre ---------- */

    @Test
    public void testCreateVelo_OtherOption() {
        IVelo v = f.construire('m', "REACTEUR_ARIANE5");
        Assert.assertEquals(v.tarif(), 2.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    /* ---------- Combinaison d'options ---------- */

    @Test
    public void testMultipleOptions_TwoOptions() {
        IVelo v = f.construire('m', "CADRE_ALUMINIUM", "FREINS_DISQUE");

        Pattern p = Pattern.compile("Vélo cadre mixte, ((cadre aluminium, freins à disque)|(freins à disque, cadre aluminium)) - 0\\.0 km");
        Matcher m = p.matcher(v.toString());
        Assert.assertTrue(m.matches());

        final double veloTarif = 2.0;
        final double aluminiumTarif = 0.2;
        final double freinsTarif = 0.3;
        Assert.assertEquals(veloTarif + aluminiumTarif + freinsTarif, v.tarif(), 0.001);
    }

    @Test
    public void testMultipleOptions_TwoOptions_DifferentOrder() {
        IVelo v = f.construire('m', "FREINS_DISQUE", "CADRE_ALUMINIUM");

        Pattern p = Pattern.compile("Vélo cadre mixte, ((cadre aluminium, freins à disque)|(freins à disque, cadre aluminium)) - 0\\.0 km");
        Matcher m = p.matcher(v.toString());
        Assert.assertTrue(m.matches());

        final double veloTarif = 2.0;
        final double aluminiumTarif = 0.2;
        final double freinsTarif = 0.3;
        Assert.assertEquals(veloTarif + aluminiumTarif + freinsTarif, v.tarif(), 0.001);
    }

    /* ---------- Création de vélo avec des options avec changement de casse ---------- */

    @Test
    public void testCreateVelo_CaseSensitive() {
        IVelo v = f.construire('m', "cAdEe_aLuMiNiUm");
        Assert.assertEquals(v.tarif(), 2.0, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte - 0.0 km");
    }

    @Test
    public void testCreateVelo_CaseSensitive_SeveralTimesSameOption() {
        IVelo v = f.construire('m', "CADRE_ALUMINIUM", "cAdEe_aLuMiNiUm");
        Assert.assertEquals(v.tarif(), 2.2, 0);
        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }
}
