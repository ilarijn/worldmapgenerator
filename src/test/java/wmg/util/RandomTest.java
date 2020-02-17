package wmg.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RandomTest {

    @Test
    public void randomValuesAreValid() {
        Random r = new Random(3);
        for (int i = 0; i < 10000; i++) {
            double x = r.nextDouble();
            assertTrue(x < 1.0 && x >= 0);
        }
    }
}
