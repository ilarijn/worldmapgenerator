package wmg.domain;

import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import wmg.util.Func;
import wmg.util.IntegerSet;

public class RiversTest {

    @Test
    public void graphIsValid() {
        int n = 4;
        DiamondSquare ds = new DiamondSquare(n, n, 0);
        double[][] grid = ds.getNoise();
        n++;

        Rivers r = new Rivers(grid);
        double[][] graph = r.setup();

        // Cost from node n + 1 to n + 2, i.e. adjacent point, should equal value of 
        // heightmap[1][2], and from n + 2 to n + 1 it should equal heightmap[1][1].
        assertTrue(Func.roundToFive(graph[n + 1][n + 2] - 1.0)
                == Func.roundToFive(grid[1][2]));
        assertTrue(Func.roundToFive(graph[n + 2][n + 1] - 1.0)
                == Func.roundToFive(grid[1][1]));

        assertTrue(Func.roundToFive(graph[0][n + 1] - 1.0)
                == Func.roundToFive(grid[1][1]));
        assertTrue(graph[n + 1][0] - 1.0 == grid[0][0]);

        assertTrue(Func.roundToFive(graph[0][1] - 1.0)
                == Func.roundToFive(grid[0][1]));
        assertTrue(Func.roundToFive(graph[1][0] - 1.0)
                == Func.roundToFive(grid[0][0]));

        int lastRowFirstNode = (grid.length - 1) * grid[0].length;
        assertTrue(Func.roundToFive(graph[lastRowFirstNode][lastRowFirstNode + 1] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][1]));
        assertTrue(Func.roundToFive(graph[lastRowFirstNode + 1][lastRowFirstNode] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][0]));

        int lastNode = grid.length * grid[0].length - 1;
        assertTrue(Func.roundToFive(graph[lastNode][lastNode - 1] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][grid[grid.length - 1].length - 2]));
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
        double[][] graph = r.setup();
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

        System.out.println("\nGraph\n*****");
        System.out.println("0 to 3: " + (graph[0][3]));
        System.out.println("3 to 6: " + (graph[3][6]));
        System.out.println("4 to 5: " + (graph[4][5]));
        System.out.println("6 to 7: " + (graph[6][7]));
        System.out.println("7 to 8: " + (graph[7][8]) + "\n");

        System.out.println("3 to 7: " + (graph[3][7]));
        System.out.println("7 to 8: " + (graph[7][8]));
        System.out.println("8 to 5: " + (graph[8][5]) + "\n");

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
