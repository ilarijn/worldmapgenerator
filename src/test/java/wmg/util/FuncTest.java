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

    @Test
    public void IntegerSetTest() {
        IntegerSet set = new IntegerSet();
        int[] arr = set.getSet();
        assertTrue(arr.length == 0);
        assertTrue(set.size() == 0);

        set.add(0);
        set.add(0);
        arr = set.getSet();
        assertTrue(arr.length == 1);
        assertTrue(set.size() == 1);

        set.addAll(2, 3, 3, 4, 5, 6, 7, 8, 9, 10);

        arr = set.getSet();
        assertTrue(arr.length == 10);
        assertTrue(set.size() == 10);
    }

}
