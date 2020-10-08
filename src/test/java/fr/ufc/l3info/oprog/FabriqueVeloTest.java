package fr.ufc.l3info.oprog;


import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FabriqueVeloTest {

    private FabriqueVelo fabrique;

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


}
