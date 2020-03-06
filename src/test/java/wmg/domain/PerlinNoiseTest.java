package wmg.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test to confirm heightmap values are in accepted range.
 *
 */
public class PerlinNoiseTest {

    @Test
    public void pnValuesAreValid() {
        PerlinNoise pn = new PerlinNoise(100, 100, 80, 3, 0.5, 1);
        double[][] pixels = pn.getOctavedNoise();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                assertTrue(pixels[y][x] >= -1.0 && pixels[y][x] <= 1.0);
            }
        }
    }
}
