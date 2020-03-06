package wmg.util;

import org.junit.BeforeClass;
import org.junit.Test;
import wmg.domain.DiamondSquare;
import wmg.domain.PerlinNoise;

/**
 * Performance testing for the noise functions.
 *
 */
public class NoisePerformance {

    static int iterations;
    static int n;
    static int seed;

    @BeforeClass
    public static void setup() {

        try {
            n = Integer.parseInt(System.getProperty("testsize"));
        } catch (NumberFormatException e) {
            n = 400;
        }

        try {
            iterations = Integer.parseInt(System.getProperty("testiter"));
        } catch (NumberFormatException e) {
            iterations = 1;
        }

        seed = 10;

    }

    @Test
    public void pnPerformance() {
        double avg = 0;
        for (int i = 0; i < iterations; i++) {
            int octaves = 5;
            PerlinNoise pn = new PerlinNoise(n, n, 2, octaves, 0.5, seed);

            long start = System.nanoTime();
            pn.getOctavedNoise();
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);
            avg += res;

            System.out.println("Perlin noise [" + n + "][" + n + "] " + octaves + ": " + res + "s");
        }
        System.out.println("Average: " + (avg / iterations) + "s");
    }

    @Test
    public void dsPerformance() {

        int size = (1 << (int) Math.ceil(Math.log(n) / Math.log(2))) + 1;
        double avg = 0;

        for (int i = 0; i < iterations; i++) {
            DiamondSquare ds = new DiamondSquare(size, seed);

            long start = System.nanoTime();
            ds.getNoise();
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);
            avg += res;

            System.out.println("Diamond-square [" + n + "][" + n + "]: " + res + "s");
        }
        System.out.println("Average: " + (avg / iterations) + "s");
    }

}
