package wmg.domain;

import wmg.util.Random;

/**
 * Returns a 2D array with values in range [-1.0, 1.0].
 *
 */
public class DiamondSquare {

    double[][] grid;
    int size;
    Random random;

    // Random initial corner values.
    public DiamondSquare(int height, int width, int seed) {
        random = new Random(seed);

        // Grid size is 2^n+1, the power of 2 greater to or equal than the 
        // larger of the given height and width values + 1.
        size = height > width ? height : width;
        size = (1 << (int) Math.ceil(Math.log(size) / Math.log(2))) + 1;
        
        grid = new double[size][size];

        grid[0][0] = random.nextDouble() * 2.0 - 1.0;
        grid[size - 1][0] = random.nextDouble() * 2.0 - 1.0;
        grid[0][size - 1] = random.nextDouble() * 2.0 - 1.0;
        grid[size - 1][size - 1] = random.nextDouble() * 2.0 - 1.0;
    }

    // Fixed initial corner values.
    public DiamondSquare(int height, int width, int seed, double tl, double tr, double bl, double br) {
        random = new Random(seed);

        size = height > width ? height : width;
        size = (1 << (int) Math.ceil(Math.log(size) / Math.log(2))) + 1;
        
        grid = new double[size][size];

        grid[0][0] = tl;
        grid[0][size - 1] = tr;
        grid[size - 1][0] = bl;
        grid[size - 1][size - 1] = br;
    }

    public double[][] getNoise() {
        int sideLength = size - 1;
        double randomWeight = 1.0;

        while (sideLength > 1) {
            int halfSide = sideLength / 2;

            // Diamond step, i.e. set the midpoint of each square
            // already found in the grid to be the average of 
            // its four corner points plus a random value.
            for (int y = 0; y < size - 1; y += sideLength) {
                for (int x = 0; x < size - 1; x += sideLength) {
                    double avg = (grid[y][x]
                            + grid[y + sideLength][x]
                            + grid[y][x + sideLength]
                            + grid[y + sideLength][x + sideLength])
                            / 4;

                    double range = 1.0 - Math.abs(avg);
                    avg += (random.nextDouble() * (2 * range) - range) * randomWeight;

                    grid[y + halfSide][x + halfSide] = avg;
                }
            }

            // Square step, i.e. set the midpoint of each diamond
            // already found in the grid to be the average of 
            // its four corner points plus a random value.
            for (int x = 0; x < size - 1; x += halfSide) {
                for (int y = (x + halfSide) % sideLength; y < size - 1; y += sideLength) {
                    double avg = (grid[y][(x - halfSide + size - 1) % (size - 1)]
                            + grid[y][(x + halfSide) % (size - 1)]
                            + grid[(y + halfSide) % (size - 1)][x]
                            + grid[(y - halfSide + size - 1) % (size - 1)][x])
                            / 4;

                    double range = 1.0 - Math.abs(avg);
                    avg += (random.nextDouble() * (2 * range) - range) * randomWeight;

                    grid[y][x] = avg;

                    if (x == 0) {
                        grid[y][size - 1] = avg;
                    }
                    if (y == 0) {
                        grid[size - 1][x] = avg;
                    }
                }
            }

            // Decrease the significance of the random value after each round.
            randomWeight *= 0.75;
            sideLength /= 2;
        }

        return grid;
    }

}
