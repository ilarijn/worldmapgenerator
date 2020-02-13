package wmg.domain;

import wmg.util.IntegerSet;

// River generation by finding shortest path from source to destination.
// Heightmap values are weights in the graph.

public class Rivers {

    double[][] heightMap;
    double[][] graph;
    IntegerSet[] neighbors;

    int n;

    public Rivers(double[][] grid) {
        this.heightMap = grid;
        n = grid.length * grid[0].length;
    }

    // Return array of shortest paths from graph node src
    // to all other nodes.
    // TODO: write this again with a priority queue
    public double[] dijkstra(int src) {

        double[] distance = new double[n];
        boolean[] included = new boolean[n];

        for (int i = 0; i < n; i++) {
            distance[i] = Double.MAX_VALUE;
            included[i] = false;
        }

        distance[src] = 0;

        for (int i = 0; i < n - 1; i++) {
            double min = Double.MAX_VALUE;
            int min_index = -1;

            for (int v = 0; v < n; v++) {
                if (included[v] == false && distance[v] <= min) {
                    min = distance[v];
                    min_index = v;
                }
            }

            included[min_index] = true;

            //System.out.println("Neighbors of node " + min_index);
            //System.out.println(Arrays.toString(neighbors[min_index].getSet()));
            for (int node : neighbors[min_index].getSet()) {
                if (!included[node]
                        && distance[min_index] != Double.MAX_VALUE
                        && distance[min_index] + graph[min_index][node] < distance[node]) {
                    distance[node] = distance[min_index] + graph[min_index][node];
                }   
            }
        }

        return distance;
    }

    // Create adjacency matrix and neighbor sets from height map, adding 1.0
    // to original values to avoid negative values in returned graph.
    public double[][] setup() {

        int rowLength = heightMap[0].length;
        int lastRow = heightMap.length - 1;

        graph = new double[n][n];
        neighbors = new IntegerSet[n];

        for (int i = 0; i < n; i++) {
            neighbors[i] = new IntegerSet();
        }

        // First row.
        for (int x = 1; x < rowLength - 1; x++) {
            int current = x;
            int east = current + 1;
            int west = current - 1;
            int southEast = current + rowLength + 1;
            int south = current + rowLength;
            int southWest = current + rowLength - 1;

            neighbors[current].addAll(east, west, southEast, south, southWest);

            graph[current][east] = heightMap[0][x + 1] + 1.0;
            graph[current][west] = heightMap[0][x - 1] + 1.0;
            graph[current][southEast] = heightMap[0 + 1][x + 1] + 1.0;
            graph[current][south] = heightMap[0 + 1][x] + 1.0;
            graph[current][southWest] = heightMap[0 + 1][x - 1] + 1.0;
        }

        // Last row.
        for (int x = 1; x < heightMap[lastRow].length - 1; x++) {
            int current = lastRow * rowLength + x;
            int east = current + 1;
            int west = current - 1;
            int northWest = current - rowLength - 1;
            int north = current - rowLength;
            int northEast = current - rowLength + 1;

            neighbors[current].addAll(east, west, northWest, north, northEast);

            graph[current][east] = heightMap[lastRow][x + 1] + 1.0;
            graph[current][west] = heightMap[lastRow][x - 1] + 1.0;
            graph[current][northWest] = heightMap[lastRow - 1][x - 1] + 1.0;
            graph[current][north] = heightMap[lastRow - 1][x] + 1.0;
            graph[current][northEast] = heightMap[lastRow - 1][x + 1] + 1.0;
        }

        //First column.
        for (int y = 1; y < lastRow; y++) {
            int current = y * rowLength;
            int north = current - rowLength;
            int south = current + rowLength;
            int northEast = current - rowLength + 1;
            int east = current + 1;
            int southEast = current + rowLength + 1;

            neighbors[current].addAll(north, south, northEast, east, southEast);

            graph[current][north] = heightMap[y - 1][0] + 1.0;
            graph[current][northEast] = heightMap[y - 1][0 + 1] + 1.0;
            graph[current][east] = heightMap[y][0 + 1] + 1.0;
            graph[current][southEast] = heightMap[y + 1][0 + 1] + 1.0;
            graph[current][south] = heightMap[y + 1][0] + 1.0;
        }

        //Last column.
        for (int y = 1; y < lastRow; y++) {
            int current = y * rowLength + rowLength - 1;
            int north = current - rowLength;
            int south = current + rowLength;
            int northWest = current - rowLength - 1;
            int west = current - 1; 
            int southWest = current + rowLength - 1;

            neighbors[current].addAll(north, south, northWest, west, southWest);

            graph[current][north] = heightMap[y - 1][rowLength - 1] + 1.0;
            graph[current][northWest] = heightMap[y - 1][rowLength - 2] + 1.0;
            graph[current][west] = heightMap[y][rowLength - 2] + 1.0;
            graph[current][southWest] = heightMap[y + 1][rowLength - 2] + 1.0;
            graph[current][south] = heightMap[y + 1][rowLength - 1] + 1.0;
        }

        // Inner cells.
        for (int y = 1; y < heightMap.length - 1; y++) {
            for (int x = 1; x < heightMap[y].length - 1; x++) {

                int current = y * heightMap[y].length + x;

                int northWest = current - heightMap[y].length - 1;
                int north = current - heightMap[y].length;
                int northEast = current - heightMap[y].length + 1;
                int east = current + 1;
                int southEast = current + heightMap[y].length + 1;
                int south = current + heightMap[y].length;
                int southWest = current + heightMap[y].length - 1;
                int west = current - 1;

                neighbors[current].addAll(northWest, north, northEast,
                        east, southEast, south, southWest, west);

                graph[current][northWest] = heightMap[y - 1][x - 1] + 1.0;
                graph[current][north] = heightMap[y - 1][x] + 1.0;
                graph[current][northEast] = heightMap[y - 1][x + 1] + 1.0;
                graph[current][east] = heightMap[y][x + 1] + 1.0;
                graph[current][southEast] = heightMap[y + 1][x + 1] + 1.0;
                graph[current][south] = heightMap[y + 1][x] + 1.0;
                graph[current][southWest] = heightMap[y + 1][x - 1] + 1.0;
                graph[current][west] = heightMap[y][x - 1] + 1.0;
            }
        }

        // Corners.
        int topLeft = 0;
        int topRight = rowLength - 1;
        int bottomLeft = n - rowLength;
        int bottomRight = n - 1;

        graph[topLeft][topLeft + 1] = heightMap[0][1] + 1.0;
        graph[topLeft][topLeft + rowLength + 1] = heightMap[1][1] + 1.0;
        graph[topLeft][topLeft + rowLength] = heightMap[1][0] + 1.0;
        neighbors[topLeft].addAll(
                topLeft + 1,
                topLeft + rowLength + 1,
                topLeft + rowLength);

        graph[topRight][topRight - 1] = heightMap[0][rowLength - 1] + 1.0;
        graph[topRight][topRight + rowLength - 1] = heightMap[1][heightMap[1].length - 2] + 1.0;
        graph[topRight][topRight + rowLength] = heightMap[1][heightMap[1].length - 1] + 1.0;
        neighbors[topRight].addAll(
                topRight - 1,
                topRight + rowLength - 1,
                topRight + rowLength
        );

        graph[bottomLeft][bottomLeft + 1] = heightMap[lastRow][1] + 1.0;
        graph[bottomLeft][bottomLeft - rowLength + 1] = heightMap[lastRow - 1][1] + 1.0;
        graph[bottomLeft][bottomLeft - rowLength] = heightMap[lastRow - 1][0] + 1.0;
        neighbors[bottomLeft].addAll(
                bottomLeft + 1,
                bottomLeft - rowLength + 1,
                bottomLeft - rowLength
        );

        graph[bottomRight][bottomRight - 1] = heightMap[lastRow][rowLength - 2] + 1.0;
        graph[bottomRight][bottomRight - rowLength - 1] = heightMap[lastRow - 1][rowLength - 2] + 1.0;
        graph[bottomRight][bottomRight - rowLength] = heightMap[lastRow][rowLength - 1] + 1.0;
        neighbors[bottomRight].addAll(
                bottomRight - 1,
                bottomRight - rowLength - 1,
                bottomRight - rowLength
        );

        return graph;
    }

    public IntegerSet[] getNeighbors() {
        return neighbors;
    }
    
    

}
