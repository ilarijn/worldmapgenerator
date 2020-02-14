package wmg.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IntegerSetTest {

    @Test
    public void IntegerSetTest() {

        IntegerSet set = new IntegerSet();
        int[] arr = set.getSet();

        assertTrue(arr.length == 0);
        assertTrue(set.size() == 0);
        assertTrue(!set.contains(0));

        set.add(0);
        set.add(0);

        arr = set.getSet();
        assertTrue(set.contains(0));
        assertTrue(arr.length == 1);
        assertTrue(set.size() == 1);

        set.addAll(2, 3, 3, 4, 5, 6, 7, 8, 9, 10);

        arr = set.getSet();
        assertTrue(arr.length == 10);
        assertTrue(set.size() == 10);
    }
}
