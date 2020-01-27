package wmg.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class PerlinNoiseTest {

    @Test
    public void noiseValuesAreValid() {
        PerlinNoise pn = new PerlinNoise(100, 100, 80, 3, 0.5, 10, true);
        double[][] pixels = pn.getOctavedNoise();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                assertTrue(pixels[y][x] >= -1.0 && pixels[y][x] <= 1.0);
            }
        }
    }

}
