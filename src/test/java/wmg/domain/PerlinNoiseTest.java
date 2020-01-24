package wmg.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class PerlinNoiseTest {

    @Test
    public void RGBValuesAreValid() {
        PerlinNoise pn = new PerlinNoise(100, 100, 2);
        int[][] pixels = pn.getGrayscale();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                assertTrue(pixels[y][x] >= 0 && pixels[y][x] <= 255);
            }
        }
    }

}
