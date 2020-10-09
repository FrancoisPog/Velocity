package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void initFabrique(){
        fabrique = FabriqueVelo.getInstance();
    }

    @Test
    public void fabriqueTest(){
        IVelo velo = fabrique.construire('F',"SUSPENSION_AVANT","ASSISTANCE_ELECTRIQUE","SUSPENSION_AVANT","SUSPENSION_ARRIERE");
        System.out.println(velo.toString());
    }

    @Test
    public void constructorIsPrivate(){
        for (Constructor c : FabriqueVelo.class.getDeclaredConstructors()) {
            assertFalse(c.isAccessible());
        }


    }

    @Test
    public void oneInstanceOnly(){
        assertTrue(FabriqueVelo.getInstance().equals(FabriqueVelo.getInstance()));
    }


    @Test
    public void MemeOptionPlusieursFois() {
        for(int i = 0 ; i < options.length ; ++i){

        }
        // split().length == 2
    }
}
