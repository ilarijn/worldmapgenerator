package wmg.domain;

public class Rivers {

    double[][] grid;

    public Rivers(double[][] grid) {
        this.grid = grid;
    }

    // Create adjacency matrix from height map, adding 1.0
    // to original values to avoid negative values in returned graph.
    public double[][] createGraph() {

        int n = grid.length * grid[0].length;
        double[][] graph = new double[n][n];

        for (int y = 1; y < grid.length - 1; y++) {
            for (int x = 1; x < grid[y].length - 1; x++) {

                int current = y * grid[y].length + x;

                int northWest = current - grid[y].length - 1;
                int north = current - grid[y].length;
                int northEast = current - grid[y].length + 1;
                int east = current + 1;
                int southEast = current + grid[y].length + 1;
                int south = current + grid[y].length;
                int southWest = current + grid[y].length - 1;
                int west = current - 1;

                graph[current][northWest] = grid[y - 1][x - 1] + 1.0;
                graph[northWest][current] = grid[y][x] + 1.0;

                graph[current][north] = grid[y - 1][x] + 1.0;
                graph[north][current] = grid[y][x] + 1.0;

                graph[current][northEast] = grid[y - 1][x + 1] + 1.0;
                graph[northEast][current] = grid[y][x] + 1.0;

                graph[current][east] = grid[y][x + 1] + 1.0;
                graph[east][current] = grid[y][x] + 1.0;

                graph[current][southEast] = grid[y + 1][x + 1] + 1.0;
                graph[southEast][current] = grid[y][x] + 1.0;

                graph[current][south] = grid[y + 1][x] + 1.0;
                graph[south][current] = grid[y][x] + 1.0;

                graph[current][southWest] = grid[y + 1][x - 1] + 1.0;
                graph[southWest][current] = grid[y][x] + 1.0;

                graph[current][west] = grid[y][x - 1] + 1.0;
                graph[west][current] = grid[y][x] + 1.0;
            }
        }

        // Edge rows and columns.
        for (int y = 0; y < grid.length; y += grid.length - 1) {
            for (int x = 1; x < grid[y].length - 1; x++) {
                int current = y * grid[y].length + x;
                int east = current + 1;
                int west = current - 1;
                graph[current][east] = grid[y][x + 1] + 1.0;
                graph[east][current] = grid[y][x] + 1.0;
                graph[current][west] = grid[y][x - 1] + 1.0;
                graph[west][current] = grid[y][x] + 1.0;
            }
        }

        for (int x = 0; x < grid[0].length; x += grid[0].length - 1) {
            for (int y = 1; y < grid.length - 1; y++) {
                int current = y * grid[y].length + x;
                int north = current - grid[y].length;
                int south = current + grid[y].length;
                graph[current][north] = grid[y - 1][x] + 1.0;
                graph[north][current] = grid[y][x] + 1.0;
                graph[current][south] = grid[y + 1][x] + 1.0;
                graph[south][current] = grid[y][x] + 1.0;
            }
        }

        return graph;
    }

}
