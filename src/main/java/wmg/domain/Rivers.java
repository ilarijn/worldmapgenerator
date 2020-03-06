package wmg.domain;

import wmg.util.IntegerSet;
import wmg.util.Node;
import wmg.util.NodePQ;

/**
 * River generation by finding shortest path (lowest terrain) from source to
 * destination in a heightmap.
 *
 */
public class Rivers {

    double[][] heightMap;
    Node[] graph;
    IntegerSet[] neighbors;
    int[] path;

    int n;
    double INF = 9999999999.0;

    /**
     * Constructor.
     *
     * @param grid The heightmap where rivers are generated.
     */
    public Rivers(double[][] grid) {
        this.heightMap = grid;
        n = grid.length * grid[0].length;
    }

    /**
     * Generate a river from source point to destination point in the heightmap.
     *
     * @param srcY Y coordinate of source point.
     * @param srcX X coordinate of source point.
     * @param destY Y coordinate of destination point.
     * @param destX X coordinate of destination point.
     * @param riverVal The value that should be assigned to river points.
     */
    public void apply(int srcY, int srcX, int destY, int destX, double riverVal) {

        int rowWidth = heightMap[0].length;
        int src = srcY * rowWidth + srcX;
        int dest = destY * rowWidth + destX;

        setup();
        dijkstra(src, dest, false);

        Node node = graph[dest];
        int prevY = node.getY();
        int prevX = node.getX();

        dest = path[dest];

        while (dest != src) {
            node = graph[dest];
            int y = node.getY();
            int x = node.getX();

            if (y < heightMap.length - 1
                    && x < rowWidth - 1
                    && y > 0 && x > 0) {
                if (heightMap[y][x] == riverVal) {
                    if (prevY == y && prevX != x) {
                        heightMap[y + 1][x] = riverVal;
                        heightMap[y - 1][x] = riverVal;
                    } else {
                        heightMap[y][x + 1] = riverVal;
                        heightMap[y][x - 1] = riverVal;
                        heightMap[y + 1][x] = riverVal;
                        heightMap[y - 1][x] = riverVal;
                    }
                }
                heightMap[y][x] = riverVal;
            }

            prevY = node.getY();
            prevX = node.getX();
            dest = path[dest];
        }
    }

    /**
     * Dijkstra's algorithm. Public for testing purposes.
     *
     * @param src Source node.
     * @param dest Destination node.
     * @param all If false, stop when destination node is reached the first
     * time.
     * @return Array of distances from source to destination.
     */
    public double[] dijkstra(int src, int dest, boolean all) {

        NodePQ pq = new NodePQ();
        double[] distances = new double[n];
        boolean[] included = new boolean[n];
        path = new int[n];

        for (int i = 0; i < n; i++) {
            distances[i] = INF;
            included[i] = false;
        }

        distances[src] = 0;
        pq.add(graph[src]);

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int currentIndex = node.getId();

            if (currentIndex == dest && !all) {
                break;
            }
            if (included[currentIndex]) {
                continue;
            }

            included[currentIndex] = true;

            for (int neighbor : neighbors[currentIndex].getSet()) {
                double currentDist = distances[neighbor];
                double proposal = distances[currentIndex] + graph[neighbor].getVal();

                if (proposal < currentDist) {
                    distances[neighbor] = proposal;
                    path[neighbor] = currentIndex;
                    pq.add(graph[neighbor]);
                }
            }
        }

        return distances;
    }

    /**
     * Create nodes and neighbor sets from height map, adding 1.0 to original
     * values to avoid negative values in returned graph. Public for
     * testing purposes.
     *
     * @return Finished graph.
     */
    public Node[] setup() {

        int rowLength = heightMap[0].length;
        int lastRow = heightMap.length - 1;

        graph = new Node[n];
        neighbors = new IntegerSet[n];

        for (int i = 0; i < n; i++) {
            neighbors[i] = new IntegerSet();
        }

        // First row.
        for (int x = 1; x < rowLength - 1; x++) {
            int y = 0;
            int current = x;
            int east = current + 1;
            int west = current - 1;
            int southEast = current + rowLength + 1;
            int south = current + rowLength;
            int southWest = current + rowLength - 1;

            neighbors[current].addAll(east, west, southEast, south, southWest);

            graph[current] = new Node(heightMap[y][x] + 1.0, current, y, x);
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

            graph[current] = new Node(heightMap[lastRow][x] + 1.0, current, lastRow, x);
        }

        //First column.
        for (int y = 1; y < lastRow; y++) {
            int x = 0;
            int current = y * rowLength;
            int north = current - rowLength;
            int south = current + rowLength;
            int northEast = current - rowLength + 1;
            int east = current + 1;
            int southEast = current + rowLength + 1;

            neighbors[current].addAll(north, south, northEast, east, southEast);

            graph[current] = new Node(heightMap[y][x] + 1.0, current, y, x);
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

            graph[current] = new Node(heightMap[y][rowLength - 1] + 1.0, current, y, rowLength - 1);
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

                graph[current] = new Node(heightMap[y][x] + 1.0, current, y, x);
            }
        }

        // Corners.
        int topLeft = 0;
        int topRight = rowLength - 1;
        int bottomLeft = n - rowLength;
        int bottomRight = n - 1;

        int y = 0;
        int x = 0;
        graph[topLeft] = new Node(heightMap[y][x] + 1.0, topLeft, y, x);
        neighbors[topLeft].addAll(
                topLeft + 1,
                topLeft + rowLength + 1,
                topLeft + rowLength);

        x = rowLength - 1;
        graph[topRight] = new Node(heightMap[y][x] + 1.0, topRight, y, x);
        neighbors[topRight].addAll(
                topRight - 1,
                topRight + rowLength - 1,
                topRight + rowLength
        );

        y = lastRow;
        graph[bottomRight] = new Node(heightMap[y][x] + 1.0, bottomRight, y, x);
        neighbors[bottomRight].addAll(
                bottomRight - 1,
                bottomRight - rowLength - 1,
                bottomRight - rowLength
        );

        x = 0;
        graph[bottomLeft] = new Node(heightMap[y][x] + 1.0, bottomLeft, y, x);
        neighbors[bottomLeft].addAll(
                bottomLeft + 1,
                bottomLeft - rowLength + 1,
                bottomLeft - rowLength
        );

        return graph;
    }

    public IntegerSet[] getNeighbors() {
        return neighbors;
    }

    public int[] getPath() {
        return path;
    }

    public Node[] getGraph() {
        return graph;
    }

    public double getINF() {
        return INF;
    }

}
