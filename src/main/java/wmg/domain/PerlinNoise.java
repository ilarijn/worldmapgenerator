package wmg.domain;

import wmg.util.*;

public class PerlinNoise {

    Vector2[][] grid;
    int height, width, gridHeight, gridWidth, cellSize;

    public PerlinNoise(int h, int w, int c) {
        height = h;
        width = w;
        cellSize = c;

        gridHeight = (int) Math.ceil(height / cellSize) + 1;
        gridWidth = (int) Math.ceil(width / cellSize) + 1;

        grid = new Vector2[gridHeight][gridWidth];
    }

    // Returns array of values in range [0, 255]
    public int[][] getGrayscale() {
        generateGradients();
        int pixels[][] = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = getValue(y, x);
                pixels[y][x] = (int) (128 + 128 * value);
            }
        }
        return pixels;
    }

    // Generate random "gradient", i.e. vector, for each grid point  
    public void generateGradients() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid[y][x] = Func.randomVector();
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
        relativeX = Func.fade(relativeX);
        relativeY = Func.fade(relativeY);

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
