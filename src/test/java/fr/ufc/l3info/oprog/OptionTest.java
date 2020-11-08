package fr.ufc.l3info.oprog;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class OptionTest {

    private IVelo veloBase, veloToutesOptions;
    final String regex_toString = "^Vélo cadre (homme|femme|mixte)(, (cadre aluminium|freins à disque|assistance électrique|suspension arrière|suspension avant))* - \\d+.\\d km$";


    @Before
    public void InitVelos() {
        veloBase = new Velo();

        veloToutesOptions = new Velo();
        veloToutesOptions = new OptFreinsDisque(veloToutesOptions);
        veloToutesOptions = new OptSuspensionArriere(veloToutesOptions);
        veloToutesOptions = new OptAssistanceElectrique(veloToutesOptions);
        veloToutesOptions = new OptCadreAlu(veloToutesOptions);
        veloToutesOptions = new OptSuspensionAvant(veloToutesOptions);
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



}
