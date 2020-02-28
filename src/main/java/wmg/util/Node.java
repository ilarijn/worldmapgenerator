package wmg.util;

/**
 * Nodes for a graph created from a heightmap.
 * Implements Comparable for performance testing purposes.
 */

public class Node implements Comparable<Node> {

    private final double val;
    private final int id;
    private final int y;
    private final int x;

    public Node(double val, int id, int y, int x) {
        this.val = val;
        this.id = id;
        this.y = y;
        this.x = x;
    }

    public double getVal() {
        return val;
    }

    public int getId() {
        return id;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public int compareTo(Node n) {
        return Double.compare(this.val, n.val);
    }

}
