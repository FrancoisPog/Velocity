package fr.ufc.l3info.oprog;

import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private IVelo velo;

    // TODO : Tester progpagation avec valeur différente de défault

    @Before
    public void InitVelo(){
        velo = new Velo();
    }

    @Test
    public void toStringTest(){
        velo = new OptCadreAlu(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptFreinsDisque(velo);
        velo = new OptFreinsDisque(velo);

        System.out.println(velo);
    }
}
