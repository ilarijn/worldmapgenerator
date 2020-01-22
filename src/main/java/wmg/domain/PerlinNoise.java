package wmg.domain;

import wmg.model.Vector2;

public class PerlinNoise {

    Vector2[][] grid;
    int height, width, cellsize;

    public PerlinNoise(int h, int w, int c) {
        height = h;
        width = w;
        cellsize = c;
    }

    public void getGrayscale(int width, int height, int cellsize) {

    }

    public void generateGradients() {
        int gridHeight = (int) Math.ceil(height / cellsize) + 1;
        int gridWidth = (int) Math.ceil(width / cellsize) + 1;
        //System.out.println("gridheight: " + gridHeight + " gridwidth: " + gridWidth);
        grid = new Vector2[gridHeight][gridWidth];
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid[y][x] = randomVector();
           }
        }
    }

    public Vector2 randomVector() {
        double angle = Math.random() * 2 * Math.PI;
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }

    public Vector2[][] getGrid() {
        return grid;
    }

}
