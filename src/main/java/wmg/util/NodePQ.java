package wmg.util;

/**
 * Min heap priority queue for Node objects. Based on the MaxPQ class in
 * Sedgewick &amp; Wayne: Algorithms 4th ed.
 */
public class NodePQ {

    private Node[] pq;
    private int n;

    public NodePQ() {
        // Set size to a number somewhat greater than the approximate amount of pixels in typical heightmap.
        pq = new Node[30_001];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public void add(Node node) {
        if (n == pq.length - 1) {
            resize(pq.length * 2);
        }
        pq[++n] = node;
        swim(n);
    }

    /**
     * Return and remove from queue node with smallest value.
     *
     * @return Node with smallest value in queue.
     */
    public Node poll() {
        if (isEmpty()) {
            return null;
        }
        Node min = pq[1];
        swap(1, n--);
        sink(1);
        pq[n + 1] = null;
        return min;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            swap(k / 2, k);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            swap(k, j);
            k = j;
        }
    }

    private boolean greater(int a, int b) {
        return pq[a].getVal() > pq[b].getVal();
    }

    private void swap(int a, int b) {
        Node t = pq[a];
        pq[a] = pq[b];
        pq[b] = t;
    }

    private void resize(int size) {
        if (size > n) {
            Node[] temp = new Node[size];
            for (int i = 1; i <= n; i++) {
                temp[i] = pq[i];
            }
            pq = temp;
        }
    }

    public int size() {
        return n;
    }

}
