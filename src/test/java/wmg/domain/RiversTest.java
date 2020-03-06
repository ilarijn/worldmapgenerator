package wmg.domain;

import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import wmg.util.IntegerSet;
import wmg.util.Node;

public class RiversTest {

    /**
     * Test to confirm graph nodes correspond to original array cells in a 5x5
     * array. Looks at corner and middle points.
     */
    @Test
    public void graphIsValid() {
        int n = 5;
        DiamondSquare ds = new DiamondSquare(n, 1);
        double[][] grid = ds.getNoise();

        Rivers r = new Rivers(grid);
        Node[] graph = r.setup();

        assertTrue(roundToFive(graph[0].getVal() - 1.0)
                == roundToFive(grid[0][0]));

        assertTrue(roundToFive(graph[n - 1].getVal() - 1.0)
                == roundToFive(grid[0][n - 1]));

        assertTrue(roundToFive(graph[(n - 1) * n].getVal() - 1.0)
                == roundToFive(grid[n - 1][0]));

        assertTrue(roundToFive(graph[n * n - 1].getVal() - 1.0)
                == roundToFive(grid[n - 1][n - 1]));

        assertTrue(roundToFive(graph[(n * n - 1) / 2].getVal() - 1.0)
                == roundToFive(grid[n / 2][n / 2]));

    }

    /**
     * Test to confirm correct graph neighbors in a graph created from a 5x5
     * array. Looks at corner and middle points.
     */
    @Test
    public void graphNeighborsAreValid() {
        int n = 5;
        DiamondSquare ds = new DiamondSquare(n, 0);
        double[][] grid = ds.getNoise();

        Rivers r = new Rivers(grid);
        r.setup();
        IntegerSet[] neighbors = r.getNeighbors();

        assertTrue(neighbors[0].contains(1));
        assertTrue(neighbors[0].contains(5));
        assertTrue(neighbors[0].contains(6));

        assertTrue(neighbors[4].contains(3));
        assertTrue(neighbors[4].contains(8));
        assertTrue(neighbors[4].contains(9));

        assertTrue(neighbors[20].contains(15));
        assertTrue(neighbors[20].contains(16));
        assertTrue(neighbors[20].contains(21));

        assertTrue(neighbors[24].contains(18));
        assertTrue(neighbors[24].contains(19));
        assertTrue(neighbors[24].contains(23));

        assertTrue(neighbors[6].contains(0));
        assertTrue(neighbors[6].contains(1));
        assertTrue(neighbors[6].contains(2));
        assertTrue(neighbors[6].contains(5));
        assertTrue(neighbors[6].contains(7));
        assertTrue(neighbors[6].contains(10));
        assertTrue(neighbors[6].contains(11));
        assertTrue(neighbors[6].contains(12));

    }

    /**
     * Confirm that Dijkstra's algorithm works by comparing distances and
     * shortest paths. Prints feedback.
     */
    @Test
    public void dijkstraTest() {
        double[][] grid = new double[][]{
            {0, 2, 2},
            {0, 1, 10},
            {2, 0, 4}
        };
        Rivers r = new Rivers(grid);
        r.setup();
        double[] distances = r.dijkstra(0, 1, true);

        System.out.println("\nDijkstra input grid");
        System.out.println("*******************");
        for (double[] row : grid) {
            System.out.print("[");
            for (double d : row) {
                System.out.print((d + 1.0) + ", ");
            }
            System.out.println("]");
        }
        System.out.println("");
        for (int i = 0; i < distances.length; i++) {
            System.out.println("Distance to node " + i + ": " + distances[i]);
        }
        int[] path = r.getPath();
        int dest = 5;
        System.out.print("\nRoute from " + dest + " to 0: " + dest);
        while (dest != 0) {
            dest = path[dest];
            System.out.print(" -> " + dest);
        }
        System.out.println("\n\nNeighbors of 3: " + Arrays.toString(r.getNeighbors()[3].getSet()));

        assertTrue(distances[5] == 13.0);
        assertTrue(path[5] == 7);
        assertTrue(path[7] == 3);
        assertTrue(path[3] == 0);
    }

    /**
     * Confirm that river is applied along the related shortest path in an
     * array.
     */
    @Test
    public void riverIsApplied() {
        double[][] grid = new double[][]{
            {0, 2, 2, 3, 1},
            {0, 1, 10, 1, 3},
            {1, 1, 10, 2, 4},
            {2, 0, 4, 5, 6},
            {2, 0, 4, 5, 1}
        };
        Rivers r = new Rivers(grid);
        double rv = -0.2;
        r.apply(1, 1, 3, 3, rv);

        assertTrue(grid[2][1] == rv);
        assertTrue(grid[3][2] == rv);
    }

    private static double roundToFive(double val) {
        return (double) Math.round(val * 100000d) / 100000d;
    }
}
