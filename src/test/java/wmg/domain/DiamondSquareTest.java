package wmg.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class DiamondSquareTest {

    @Test
    public void dsValuesAreValid() {
        DiamondSquare ds = new DiamondSquare(100, 100, 1);
        double[][] pixels = ds.getNoise();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                assertTrue(pixels[y][x] >= -1.0 && pixels[y][x] <= 1.0);
            }
        }
    }

    @Test
    public void dsValuesAreValidFixed() {
        DiamondSquare ds = new DiamondSquare(100, 100, 1, 0.99, 0.99, 0.99, 0.99);
        double[][] pixels = ds.getNoise();
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                assertTrue(pixels[y][x] >= -1.0 && pixels[y][x] <= 1.0);
            }
        }
    }
}
