package wmg.domain;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import wmg.util.Func;

public class RiversTest {

    @Test
    public void graphIsValid() {
        int n = 4;
        DiamondSquare ds = new DiamondSquare(n, n, 0);
        double[][] grid = ds.getNoise();
        n++;

        Rivers r = new Rivers(grid);
        double[][] graph = r.createGraph();

        // Cost from node n + 1 to n + 2, i.e. adjacent point, should equal value of 
        // heightmap[1][2], and n + 2 to n + 1 equals heightmap[1][1].
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

        System.out.println("------");
        int lastRowFirstNode = (grid.length - 1) * grid[0].length;

        System.out.println(Func.roundToFive(graph[lastRowFirstNode][lastRowFirstNode + 1] - 1.0));
        System.out.println(Func.roundToFive(grid[grid.length - 1][1]));

        assertTrue(Func.roundToFive(graph[lastRowFirstNode][lastRowFirstNode + 1] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][1]));
        assertTrue(Func.roundToFive(graph[lastRowFirstNode + 1][lastRowFirstNode] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][0]));

        int lastNode = grid.length * grid[0].length - 1;
        System.out.println(lastNode);
        assertTrue(Func.roundToFive(graph[lastNode][lastNode - 1] - 1.0)
                == Func.roundToFive(grid[grid.length - 1][grid[grid.length - 1].length - 2]));
    }

}
