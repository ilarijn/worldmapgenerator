package wmg.domain;

import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import wmg.util.Func;
import wmg.util.IntegerSet;
import wmg.util.Node;

public class RiversTest {

    @Test
    public void graphIsValid() {
        int n = 4;
        DiamondSquare ds = new DiamondSquare(n, n, 0);
        double[][] grid = ds.getNoise();
        n++;

        Rivers r = new Rivers(grid);
        Node[] graph = r.setup();

        assertTrue(Func.roundToFive(graph[n + 2].getVal() - 1.0)
                == Func.roundToFive(grid[1][2]));
        assertTrue(Func.roundToFive(graph[n + 1].getVal() - 1.0)
                == Func.roundToFive(grid[1][1]));
    }

    @Test
    public void graphNeighborsAreValid() {
        int n = 4;
        DiamondSquare ds = new DiamondSquare(n, n, 0);
        double[][] grid = ds.getNoise();
        n++;

        Rivers r = new Rivers(grid);
        r.setup();
        IntegerSet[] neighbors = r.getNeighbors();

        assertTrue(neighbors[0].contains(1));
        assertTrue(neighbors[0].contains(5));
        assertTrue(neighbors[0].contains(6));

        assertTrue(neighbors[6].contains(0));
        assertTrue(neighbors[6].contains(1));
        assertTrue(neighbors[6].contains(2));
        assertTrue(neighbors[6].contains(5));
        assertTrue(neighbors[6].contains(7));
        assertTrue(neighbors[6].contains(10));
        assertTrue(neighbors[6].contains(11));
        assertTrue(neighbors[6].contains(12));
    }

    @Test
    public void dijkstraTest() {
        double[][] grid = new double[][]{
            {0, 2, 2},
            {0, 1, 10},
            {2, 0, 4}
        };
        Rivers r = new Rivers(grid);
        double[] distances = r.dijkstra(0);

        System.out.println("\nInput grid");
        System.out.println("**********");
        for (double[] row : grid) {
            System.out.print("[");
            for (double d : row) {
                System.out.print((d + 1.0) + ", ");
            }
            System.out.println("]");
        }

        for (int i = 0; i < distances.length; i++) {
            System.out.println("Distance to node " + i + ": " + distances[i]);
        }

        int[] path = r.getPath();
        int dest = 5;

        System.out.print("\n" + dest + " to 0:\n" + dest);
        while (dest != 0) {
            dest = path[dest];
            System.out.print(" -> " + dest);
        }
        System.out.println("\nNeighbors of 0: " + Arrays.toString(r.getNeighbors()[0].getSet()));
    }
}
