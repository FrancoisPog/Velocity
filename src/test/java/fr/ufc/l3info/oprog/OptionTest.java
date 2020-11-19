package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class OptionTest {

    private IVelo veloBase, veloToutesOptions;
    final String regex_toString = "^Vélo cadre (homme|femme|mixte)(, (cadre aluminium|freins à disque|assistance électrique|suspension arrière|suspension avant))* - \\d+.\\d km$";

    final private IVelo DEFAULT_VELO = new Velo();
    private OptCadreAlu vSimple;
    private IVelo v;

    @Before
    public void InitVelos() {
        veloBase = new Velo();

        veloToutesOptions = new Velo();
        veloToutesOptions = new OptFreinsDisque(veloToutesOptions);
        veloToutesOptions = new OptSuspensionArriere(veloToutesOptions);
        veloToutesOptions = new OptAssistanceElectrique(veloToutesOptions);
        veloToutesOptions = new OptCadreAlu(veloToutesOptions);
        veloToutesOptions = new OptSuspensionAvant(veloToutesOptions);

        this.v = new Velo();
        this.vSimple = new OptCadreAlu(this.v);
    }

    @Test
    public void propagationFonctionsVelo() {
        assertEquals(veloToutesOptions.decrocher(), -1);
        veloToutesOptions.parcourir(102.34);
        assertEquals(veloToutesOptions.kilometrage(), 102.34, 1e-3);
        assertEquals(veloToutesOptions.prochaineRevision(), 500 - 102.34, 1e-3);
        assertEquals(veloToutesOptions.arrimer(), 0);
        assertEquals(veloToutesOptions.arrimer(), -1);

        veloToutesOptions.abimer();
        assertTrue(veloToutesOptions.estAbime());

        assertEquals(veloToutesOptions.reviser(), -1);
        assertEquals(veloToutesOptions.decrocher(), 0);

        assertEquals(veloToutesOptions.reviser(), 0);
        assertEquals(veloToutesOptions.prochaineRevision(), 500, 1e-3);

        assertEquals(veloToutesOptions.reparer(), -2);
    }

    @Test
    public void toStringVeloBase(){
        // Pas de virgules
        assertTrue(Pattern.matches(regex_toString,veloBase.toString()));
        assertEquals(1,veloBase.toString().split(",").length);

        veloBase = new OptFreinsDisque(veloBase);

        // Contient une virgule, et le nom de l'option seulement
        assertTrue(Pattern.matches(regex_toString,veloBase.toString()));
        assertEquals(2,veloBase.toString().split(",").length);
        assertTrue(veloBase.toString().contains("freins à disque"));
        assertFalse(veloBase.toString().contains("suspension arrière"));

        // Contient deux virgules, et le nom des deux options seulement
        veloBase = new OptSuspensionArriere(veloBase);
        assertTrue(Pattern.matches(regex_toString,veloBase.toString()));
        assertEquals(3,veloBase.toString().split(",").length);
        assertTrue(veloBase.toString().contains("freins à disque"));
        assertTrue(veloBase.toString().contains("suspension arrière"));
    }

    @Test
    public void toStringVeloOptions(){
        assertTrue(Pattern.matches(regex_toString,veloToutesOptions.toString()));
        assertEquals(6,veloToutesOptions.toString().split(",").length);
        assertTrue(veloToutesOptions.toString().contains("freins à disque"));
        assertTrue(veloToutesOptions.toString().contains("suspension arrière"));
        assertTrue(veloToutesOptions.toString().contains("suspension avant"));
        assertTrue(veloToutesOptions.toString().contains("assistance électrique"));
        assertTrue(veloToutesOptions.toString().contains("cadre aluminium"));
    }

    @Test
    public void tarifToutesOptions(){
        assertEquals(5.5, veloToutesOptions.tarif(),1e-3);
    }

    @Test
    public void tarifVelo(){
        assertEquals(2,veloBase.tarif(),1e-3);
        veloBase = new OptSuspensionArriere(veloBase);
        assertEquals(2.5,veloBase.tarif(),1e-3);
        veloBase = new OptSuspensionAvant(veloBase);
        assertEquals(3,veloBase.tarif(),1e-3);
        veloBase = new OptCadreAlu(veloBase);
        assertEquals(3.2,veloBase.tarif(),1e-3);
        veloBase = new OptFreinsDisque(veloBase);
        assertEquals(3.5,veloBase.tarif(),1e-3);
        veloBase = new OptAssistanceElectrique(veloBase);
        assertEquals(5.5,veloBase.tarif(),1e-3);
    }

    /* ---------- Méthodes de vélo ---------- */

    @Test
    public void testVeloMethods_kilometrage() {
        Assert.assertEquals(vSimple.kilometrage(), 0, 0.001);
        Assert.assertEquals(vSimple.prochaineRevision(), 500, 0.001);
        vSimple.parcourir(100);
        Assert.assertEquals(vSimple.kilometrage(), 100, 0.001);
        vSimple.parcourir(-100);
        Assert.assertEquals(vSimple.kilometrage(), 100, 0.001);
        Assert.assertEquals(vSimple.prochaineRevision(), 500 - 100, 0.001);
    }

    @Test
    public void testVeloMethods_lifecycle() {
        Assert.assertEquals(vSimple.decrocher(), -1);
        Assert.assertEquals(vSimple.arrimer(), 0);
        Assert.assertEquals(vSimple.arrimer(), -1);
        Assert.assertEquals(vSimple.decrocher(), 0);
    }

    @Test
    public void testVeloMethods_lifecycle2() {
        Assert.assertFalse(vSimple.estAbime());
        v.abimer();
        Assert.assertTrue(vSimple.estAbime());
        Assert.assertEquals(vSimple.arrimer(), 0);
        Assert.assertEquals(vSimple.reviser(), -1);
        Assert.assertEquals(vSimple.decrocher(), 0);
        Assert.assertEquals(vSimple.reviser(), 0);
        Assert.assertFalse(vSimple.estAbime());
    }

    @Test
    public void testVeloMethods_lifecycle3() {
        Assert.assertFalse(vSimple.estAbime());
        vSimple.abimer();
        Assert.assertTrue(vSimple.estAbime());
        vSimple.arrimer();
        Assert.assertEquals(vSimple.reparer(), -1);
        vSimple.decrocher();
        Assert.assertEquals(vSimple.reparer(), 0);
        Assert.assertEquals(vSimple.reparer(), -2);
    }

    /* ---------- Option Cadre Aluminium ---------- */

    @Test
    public void testOptionCadreAlu_Tarif() {
        v = new OptCadreAlu(v);

        Assert.assertEquals(v.tarif(), DEFAULT_VELO.tarif() + 0.2, 0);
    }

    @Test
    public void testOptionCadreAlu_ToString() {
        v = new OptCadreAlu(v);

        Assert.assertEquals(v.toString(), "Vélo cadre mixte, cadre aluminium - 0.0 km");
    }

    /* ---------- Option Freins disque ---------- */

    @Test
    public void testOptionFreinsDisque_Tarif() {
        v = new OptFreinsDisque(v);

        Assert.assertEquals(v.tarif(), DEFAULT_VELO.tarif() + 0.3, 0);
    }

    @Test
    public void testOptionFreinsDisque_ToString() {
        v = new OptFreinsDisque(v);

        Assert.assertEquals(v.toString(), "Vélo cadre mixte, freins à disque - 0.0 km");
    }

    /* ---------- Option Suspension avant ---------- */

    @Test
    public void testOptionSuspensionAvant_Tarif() {
        v = new OptSuspensionAvant(v);

        Assert.assertEquals(v.tarif(), DEFAULT_VELO.tarif() + 0.5, 0);
    }

    @Test
    public void testOptionSuspensionAvant_ToString() {
        v = new OptSuspensionAvant(v);

        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension avant - 0.0 km");
    }

    /* ---------- Option Suspension arrière ---------- */

    @Test
    public void testOptionSuspensionArriere_Tarif() {
        v = new OptSuspensionArriere(v);

        Assert.assertEquals(v.tarif(), DEFAULT_VELO.tarif() + 0.5, 0);
    }

    @Test
    public void testOptionSuspensionArriere_ToString() {
        v = new OptSuspensionArriere(v);

        Assert.assertEquals(v.toString(), "Vélo cadre mixte, suspension arrière - 0.0 km");
    }

    /* ---------- Option Assistance électrique ---------- */

    @Test
    public void testOptionAssistanceElectrique_Tarif() {
        v = new OptAssistanceElectrique(v);

        Assert.assertEquals(v.tarif(), DEFAULT_VELO.tarif() + 2, 0);
    }

    @Test
    public void testOptionAssistanceElectrique_ToString() {
        v = new OptAssistanceElectrique(v);

        Assert.assertEquals(v.toString(), "Vélo cadre mixte, assistance électrique - 0.0 km");
    }

}
