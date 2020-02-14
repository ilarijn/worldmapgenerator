package wmg.util;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class FuncTest {

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
