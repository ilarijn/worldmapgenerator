package wmg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import wmg.domain.Rivers;

public class DijkstraPerformance {

    static int iterations;

    Rivers r;

    static double[][] grid;
    static Random random;
    static int n;
    static int dest;
    static int seed;

    static double distPQ;
    static double distNPQ;

    @BeforeClass
    public static void setup() {

        try {
            iterations = Integer.parseInt(System.getProperty("dijkstra"));
        } catch (NumberFormatException e) {
            System.out.println(e);
            iterations = 1;
        }

        n = 1000;

        dest = (n - 1) * (n - 1);

        File dir = new File("./log");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @Test
    public void dijkstraNodePQ() {

        File f = new File("nodepqlog.txt");

        for (int i = 0; i < iterations; i++) {

            setupGrid();

            r = new Rivers(grid);
            r.setup();

            long start = System.nanoTime();
            double distances[] = r.dijkstra(0, dest, true);
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);

            try {
                FileWriter fw = new FileWriter("./log/" + f, true);
                fw.append(Double.toString(res));
                fw.append("\n");
                fw.close();
            } catch (IOException e) {
                System.out.println(e);
            }

            distNPQ = distances[dest];

            System.out.println("All shortest paths in [" + n + "][" + n + "] using NodePQ: " + res + "s");
        }
    }

    @Test
    public void dijkstraPQ() {

        File f = new File("pqlog.txt");

        for (int i = 0; i < iterations; i++) {

            setupGrid();

            r = new Rivers(grid);
            r.setup();

            long start = System.nanoTime();
            double distances[] = dijkstraPQ(0, dest, true, r.getGraph());
            long end = System.nanoTime();

            double res = ((end - start) / 1e9);

            try {
                FileWriter fw = new FileWriter("./log/" + f, true);
                fw.append(Double.toString(res));
                fw.append("\n");
                fw.close();
            } catch (IOException e) {
                System.out.println(e);
            }

            distPQ = distances[dest];

            System.out.println("All shortest paths in [" + n + "][" + n + "] using PriorityQueue<Node>: " + res + "s");
        }
    }

    @AfterClass
    public static void equalResults() {
        assertTrue(distPQ == distNPQ);
    }

    public static void setupGrid() {
        seed = 123;
        grid = new double[n][n];
        random = new Random(seed);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                grid[y][x] = random.nextDouble();
            }
        }
    }

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
}
