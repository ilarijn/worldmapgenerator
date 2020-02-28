package wmg.util;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class FuncTest {

    @Test
    public void absTest() {
        assertTrue(Func.abs(-1.0) == 1);
        assertTrue(Func.abs(1.0) == 1);
    }

    @Test
    public void ceilTest() {
        double a = 3.43;
        double b = 3.00;

        assertTrue(Func.ceil(a) == 4);
        assertTrue(Func.ceil(b) == 3);
    }

    @Test
    public void powTest() {
        double x = 2;
        assertEquals(1, Func.pow(x, 0), 0);
        assertEquals(2, Func.pow(x, 1), 0);
        assertEquals(1024, Func.pow(x, 10), 0);
    }

    @Test
    public void randomVectorsAreValid() {
        Random r = new Random(123);
        Vector2 v = Func.getVector(r.nextDouble());
        for (int i = 0; i < 50; i++) {
            assertTrue(v.getX() >= -1.0 && v.getX() <= 1.0);
            assertTrue(v.getY() >= -1.0 && v.getY() <= 1.0);
            v = Func.getVector(r.nextDouble());
        }
    }

}
