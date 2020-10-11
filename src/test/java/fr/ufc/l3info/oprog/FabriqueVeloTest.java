package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
    public void initFabrique(){
        fabrique = FabriqueVelo.getInstance();
    }


    @Test
    public void constructeurPrive(){
        for (Constructor c : FabriqueVelo.class.getDeclaredConstructors()) {
            assertFalse(c.isAccessible());
        }
    }

    @Test
    public void uneSeuleInstance(){
        assertTrue(FabriqueVelo.getInstance().equals(FabriqueVelo.getInstance()));
    }


    @Test
    public void MemeOptionPlusieursFois() {

        for(int i = 0 ; i < options.length ; i++){
            IVelo velo = new Velo();

            //System.out.println("i : "+i);

            String[] opt = new String[i*2];

            for(int j = 0 ; j < i*2 ; j = j + 2){
                //System.out.print(j);
                opt[j] = options[j/2][0];
                opt[j+1] = options[j/2][0];
            }

            //System.out.println("size : "+opt.length);

            velo = fabrique.construire('m',opt);
            System.out.println(velo.toString());
            assertTrue(Pattern.matches(regex_toString,velo.toString()));
            assertEquals((int)i +1, velo.toString().split(",").length);

        }

    }
}
