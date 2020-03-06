package wmg.util;

import java.util.PriorityQueue;
import org.junit.BeforeClass;
import org.junit.Test;
import wmg.domain.Rivers;

/**
 * Performance testing for Dijkstra's algorithm using NodePQ, PriorityQueue and
 * an n^2 solution.
 *
 */
public class HeapPerformance {

    static int iterations;

    Rivers r;

    static double[][] grid;
    static Random random;
    static int n;
    static int dest;
    static int seed;

    static double distPQ;
    static double distNPQ;
    static double distNSquare;

    static int includeNSquare;

    @BeforeClass
    public static void setup() {

        try {
            iterations = Integer.parseInt(System.getProperty("testiter"));
        } catch (NumberFormatException e) {
            iterations = 1;
        }

        try {
            n = Integer.parseInt(System.getProperty("testsize"));
        } catch (NumberFormatException e) {
            n = 400;
        }

        try {
            includeNSquare = Integer.parseInt(System.getProperty("nsquare"));
        } catch (NumberFormatException e) {
            includeNSquare = 0;
        }

        dest = (n - 1) * (n - 1);
    }

    @Test
    public void dijkstraNodePQ() {

        double avg = 0;

        for (int i = 0; i < iterations; i++) {

            setupGrid();

            r = new Rivers(grid);
            r.setup();

            long start = System.nanoTime();
            double distances[] = r.dijkstra(0, dest, true);
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);
            avg += res;

            distNPQ = distances[dest];

            System.out.println("All shortest paths in [" + n + "][" + n + "] using NodePQ: " + res + "s");
        }
        System.out.println("Average: " + (avg / iterations));
    }

    @Test
    public void dijkstraPQ() {

        double avg = 0;

        for (int i = 0; i < iterations; i++) {

            setupGrid();

            r = new Rivers(grid);
            r.setup();

            long start = System.nanoTime();
            double distances[] = dijkstraPQ(0, dest, true, r.getGraph());
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);
            avg += res;

            distPQ = distances[dest];

            System.out.println("All shortest paths in [" + n + "][" + n + "] using PriorityQueue<Node>: " + res + "s");
        }
        System.out.println("Average: " + (avg / iterations));
    }

    @Test
    public void dijkstraNSquare() {

        if (includeNSquare > 0) {

            double avg = 0;

            for (int i = 0; i < iterations; i++) {

                setupGrid();

                r = new Rivers(grid);
                r.setup();

                long start = System.nanoTime();
                double distances[] = dijkstraNSquare(0);
                long end = System.nanoTime();

                double res = ((end - start) / 1e9);
                avg += res;

                distNSquare = distances[dest];

                System.out.println("All shortest paths in [" + n + "][" + n + "] using n^2 comparison: " + res + "s");
            }
            System.out.println("Average: " + (avg / iterations) + "s");
        }
    }

    private static void setupGrid() {
        seed = 123;
        grid = new double[n][n];
        random = new Random(seed);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                grid[y][x] = random.nextDouble();
            }
        }
    }

    /**
     * Dijkstra's algorithm using PriorityQueue.
     *
     * @param src Source node.
     * @param dest Destination node.
     * @param all Find all distances if true.
     * @param graph Graph
     * @return
     */
    public double[] dijkstraPQ(int src, int dest, boolean all, Node[] graph) {

        PriorityQueue<Node> pq = new PriorityQueue<>();
        double[] distances = new double[n * n];
        boolean[] included = new boolean[n * n];
        int[] path = new int[n * n];
        IntegerSet[] neighbors = r.getNeighbors();
        double INF = r.getINF();

        for (int i = 0; i < n * n; i++) {
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
     * n^2 version of Dijkstra's algorithm.
     *
     * @param src Source node.
     * @return All distances from source.
     */
    public double[] dijkstraNSquare(int src) {

        IntegerSet[] neighbors = r.getNeighbors();
        Node[] graph = r.getGraph();

        double[] distance = new double[n * n];
        boolean[] included = new boolean[n * n];

        for (int i = 0; i < n * n; i++) {
            distance[i] = Double.MAX_VALUE;
            included[i] = false;
        }

        distance[src] = 0;

        for (int i = 0; i < n * n; i++) {
            double min = Double.MAX_VALUE;
            int min_index = -1;

            for (int v = 0; v < n * n; v++) {
                if (included[v] == false && distance[v] <= min) {
                    min = distance[v];
                    min_index = v;
                }
            }

            included[min_index] = true;

            for (int neighbor : neighbors[min_index].getSet()) {
                if (!included[neighbor]
                        && distance[min_index] != Double.MAX_VALUE
                        && distance[min_index] + graph[neighbor].getVal() < distance[neighbor]) {
                    distance[neighbor] = distance[min_index] + graph[neighbor].getVal();
                }
            }
        }

        return distance;
    }

}
