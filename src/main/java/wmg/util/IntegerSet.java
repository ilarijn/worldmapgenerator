package wmg.util;

/**
 * Set structure for integers. Used to record node neighbors (max 8). 
 */
public class IntegerSet {

    int[] set;
    int n;

    public IntegerSet() {
        n = 0;
        set = new int[8];
    }

    public void add(int x) {
        if (!this.contains(x)) {
            if (n >= set.length) expand();
            set[n++] = x;
        }
    }

    public void addAll(int... args) {
        for (int i : args) add(i);        
    }

    private void expand() {
        int[] clone = new int[set.length * 2];
        for (int i = 0; i < set.length; i++) clone[i] = set[i];
        set = clone;
    }

    public boolean contains(int x) {
        for (int i = 0; i < n; i++) {
            if (set[i] == x) return true;            
        }
        return false;
    }

    public int get(int i) {
        return set[i];
    }

    public int size() {
        return n;
    }

    public int[] getSet() {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) res[i] = set[i];
        return res;
    }
}
