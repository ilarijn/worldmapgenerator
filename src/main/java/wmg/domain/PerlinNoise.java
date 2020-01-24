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

    public void generateGradients() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid[y][x] = Func.randomVector();
            }
        }
    }

    public double getValue(int y, int x) {
        int cellY = (int) Math.floor(y / cellSize);
        int cellX = (int) Math.floor(x / cellSize);

        double relativeY = (y - cellY * cellSize * 1.0) / cellSize;
        double relativeX = (x - cellX * cellSize * 1.0) / cellSize;

        relativeX = Func.fade(relativeX);
        relativeY = Func.fade(relativeY);

        // These aren't necessary but how to not have them??
        if (cellX + 1 >= grid[0].length) {
            cellX = grid[0].length - 2;
        }
        if (cellY + 1 >= grid.length) {
            cellY = grid.length - 2;
        }

        Vector2 topLeftGradient = grid[cellY][cellX];
        Vector2 topRightGradient = grid[cellY][cellX + 1];
        Vector2 bottomLeftGradient = grid[cellY + 1][cellX];
        Vector2 bottomRightGradient = grid[cellY + 1][cellX + 1];

        double topLeftValue = Func.dot(topLeftGradient, relativeY, relativeX);
        double topRightValue = Func.dot(topRightGradient, relativeY, relativeX - 1);
        double bottomLeftValue = Func.dot(bottomLeftGradient, relativeY - 1, relativeX);
        double bottomRightValue = Func.dot(bottomRightGradient, relativeY - 1, relativeX - 1);

        double topLerp = Func.lerp(topLeftValue, topRightValue, relativeX);
        double bottomLerp = Func.lerp(bottomLeftValue, bottomRightValue, relativeX);

        double res = Func.lerp(topLerp, bottomLerp, relativeY);

        return res;
        //return res / (Math.sqrt(2) / 2);
    }

    public Vector2[][] getGrid() {
        return grid;
    }

    public void printGrid() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                System.out.println(grid[y][x]);
            }
        }
    }

}
