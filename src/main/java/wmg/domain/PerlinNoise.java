package wmg.domain;

import wmg.util.Func;
import wmg.util.Random;
import wmg.util.Vector2;

/**
 * Returns a 2D array with values in range [-1.0, 1.0].
 *
 */
public class PerlinNoise {

    Vector2[][] grid;
    int height, width, gridHeight, gridWidth, scale, octaves;
    double amplitude;
    boolean fade;
    Random random;

    /**
     * Constructor.
     * @param h Heightmap height.
     * @param w Heightmap width.
     * @param s Noise scale.
     * @param o Number of octaves of noise.
     * @param a Factor affecting range of resultant values (greater value means
     * more variance).
     * @param seed Random seed.
     */
    public PerlinNoise(int h, int w, int s, int o, double a, int seed) {
        height = h;
        width = w;
        scale = s;
        octaves = o;
        amplitude = a;
        random = new Random(seed);
    }

    /**
     * Add together n iterations of noise where n is number of octaves.
     * @return Finished heightmap.
     */
    public double[][] getOctavedNoise() {
        double[][] res = new double[height][width];

        for (int octave = 0; octave < octaves; octave++) {
            int octaveScale = (int) (scale * Func.pow(0.5, octave));
            double octaveAmp = Func.pow(amplitude, octave);

            int temp = scale;
            scale = octaveScale;
            double[][] noise = getNoise();
            scale = temp;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    res[y][x] += noise[y][x] * octaveAmp;
                }
            }
        }

        double maxValue = 0;
        for (int octave = 0; octave < octaves; octave++) {
            maxValue += Func.pow(amplitude, octave);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                res[y][x] = res[y][x] / maxValue;
            }
        }

        return res;
    }

    // Single iteration of noise.
    private double[][] getNoise() {
        if (scale == 0) {
            scale = 1;
        }

        gridHeight = Func.ceil(height / scale) + 1;
        gridWidth = Func.ceil(width / scale) + 1;
        grid = new Vector2[gridHeight][gridWidth];

        generateGradients();

        double[][] values = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                values[y][x] = getValue(y, x);
            }
        }

        return values;
    }

    // Generate random "gradient", i.e. vector, for each grid point. 
    private void generateGradients() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                double val = random.nextDouble();
                grid[y][x] = Func.getVector(val);
            }
        }
    }

    // Get noise value in range [-1, 1]  at coordinate (x, y).
    private double getValue(int y, int x) {

        // Figure out cell of coordinate, relative to current scale.
        int cellY = y / scale;
        int cellX = x / scale;

        // Coordinate inside cell.
        double relativeY = (y - cellY * scale * 1.0) / scale;
        double relativeX = (x - cellX * scale * 1.0) / scale;

        // Get gradient vectors of each corner node of current cell.
        int rightCorner = cellX + 1 >= grid[0].length ? cellX : cellX + 1;
        int bottomCorner = cellY + 1 >= grid.length ? cellY : cellY + 1;

        Vector2 topLeftGradient = grid[cellY][cellX];
        Vector2 topRightGradient = grid[cellY][rightCorner];
        Vector2 bottomLeftGradient = grid[bottomCorner][cellX];
        Vector2 bottomRightGradient = grid[bottomCorner][rightCorner];

        // Compute dot products of each gradient and relative coordinates.
        double topLeftValue = Func.dot(topLeftGradient, relativeY, relativeX);
        double topRightValue = Func.dot(topRightGradient, relativeY, relativeX - 1);
        double bottomLeftValue = Func.dot(bottomLeftGradient, relativeY - 1, relativeX);
        double bottomRightValue = Func.dot(bottomRightGradient, relativeY - 1, relativeX - 1);

        // Interpolate between top and bottom values and relative coordinate.
        double topLerp = Func.lerp(topLeftValue, topRightValue, relativeX);
        double bottomLerp = Func.lerp(bottomLeftValue, bottomRightValue, relativeX);

        double res = Func.lerp(topLerp, bottomLerp, relativeY);

        return res;
    }

}
