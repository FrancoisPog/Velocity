package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

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
}
