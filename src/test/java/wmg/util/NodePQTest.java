package wmg.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NodePQTest {

    @Test
    public void smallestIsHighest() {
        Node n1 = new Node(0.8, 1, 3, 4);
        Node n2 = new Node(0.2, 2, 5, 44);
        Node n3 = new Node(0.4, 3, 2, 2);
        
        NodePQ pq = new NodePQ();
        
        pq.add(n1);
        pq.add(n2);
        pq.add(n3);
        
        assertTrue(pq.poll() == n2);
        assertTrue(!pq.isEmpty());
        assertTrue(pq.poll() == n3);
        assertTrue(pq.poll() == n1);
        assertTrue(pq.isEmpty());
        assertTrue(pq.poll() == null);
    }
}
