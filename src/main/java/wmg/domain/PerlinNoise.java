package wmg.domain;

import java.util.Random;
import wmg.util.*;

public class PerlinNoise {

    Vector2[][] grid;
    int height, width, gridHeight, gridWidth, cellSize, octaves, seed;
    double attenuation;
    boolean fade;

    public PerlinNoise(int h, int w, int c, int o, double a, int s, boolean f) {
        height = h;
        width = w;
        cellSize = c;
        octaves = o;
        attenuation = a;
        seed = s;
        fade = f;
    }

    // Add together n iterations of attenuated noise where n is number of octaves
    public double[][] getOctavedNoise() {
        double[][] res = new double[height][width];
        for (int octave = 0; octave < octaves; octave++) {
            int octaveCell = (int) (cellSize * Math.pow(0.5, octave));
            double octaveAtt = Math.pow(attenuation, octave);
            int temp = cellSize;
            cellSize = octaveCell;
            double[][] noise = getNoise();
            cellSize = temp;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    res[y][x] += noise[y][x] * octaveAtt;
                }
            }
        }
        double maxValue = 0;
        for (int octave = 0; octave < octaves; octave++) {
            maxValue += Math.pow(attenuation, octave);
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                res[y][x] = res[y][x] / maxValue;
            }
        }
        return res;
    }

    // Returns array of values in range [-1.0, 1.0]
    public double[][] getNoise() {
        gridHeight = (int) Math.ceil(height / cellSize) + 1;
        gridWidth = (int) Math.ceil(width / cellSize) + 1;
        grid = new Vector2[gridHeight][gridWidth];

        generateGradients();

        double values[][] = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                values[y][x] = getValue(y, x);
            }
        }
        return values;
    }

    // Generate random "gradient", i.e. vector, for each grid point  
    public void generateGradients() {
        Random r = new Random(seed);
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid[y][x] = Func.getVector(r.nextDouble());
            }
        }
    }

    // Get noise value in range [-1, 1]  at coordinate (x, y)
    public double getValue(int y, int x) {

        // Figure out cell of coordinate, relative to current cell size
        int cellY = (int) Math.floor(y / cellSize);
        int cellX = (int) Math.floor(x / cellSize);

        // Coordinate inside cell
        double relativeY = (y - cellY * cellSize * 1.0) / cellSize;
        double relativeX = (x - cellX * cellSize * 1.0) / cellSize;

        // Apply fade
        if (fade) {
            relativeX = Func.fade(relativeX);
            relativeY = Func.fade(relativeY);
        }

        // Get gradient vectors of each corner node of current cell
        int rightCorner = cellX + 1 >= grid[0].length ? cellX : cellX + 1;
        int bottomCorner = cellY + 1 >= grid.length ? cellY : cellY + 1;

        Vector2 topLeftGradient = grid[cellY][cellX];
        Vector2 topRightGradient = grid[cellY][rightCorner];
        Vector2 bottomLeftGradient = grid[bottomCorner][cellX];
        Vector2 bottomRightGradient = grid[bottomCorner][rightCorner];

        // Compute dot products of each gradient and relative coordinate 
        double topLeftValue = Func.dot(topLeftGradient, relativeY, relativeX);
        double topRightValue = Func.dot(topRightGradient, relativeY, relativeX - 1);
        double bottomLeftValue = Func.dot(bottomLeftGradient, relativeY - 1, relativeX);
        double bottomRightValue = Func.dot(bottomRightGradient, relativeY - 1, relativeX - 1);

        // Interpolate between top and bottom values and argument point
        double topLerp = Func.lerp(topLeftValue, topRightValue, relativeX);
        double bottomLerp = Func.lerp(bottomLeftValue, bottomRightValue, relativeX);

        double res = Func.lerp(topLerp, bottomLerp, relativeY);

        return res;
        //return res / (Math.sqrt(2) / 2);
    }

}
