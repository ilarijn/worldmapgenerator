package wmg.util;

public class IntegerSet {

    int[] set;
    int ptr;
    boolean zero;

    public IntegerSet() {
        ptr = 0;
        set = new int[8];
        zero = false;
    }

    public void add(int x) {
        if (!this.contains(x) || (x == 0 && !zero) ) {
            if (ptr >= set.length) expand();
            set[ptr++] = x;
            if (x == 0) zero = true;
        }
    }

    public void addAll(int... args) {
        for (int i : args) {
            add(i);
        }
    }

    private void expand() {
        int[] clone = new int[set.length * 2];
        for (int i = 0; i < set.length; i++) {
            clone[i] = set[i];
        }
        set = clone;
    }

    public boolean contains(int x) {
        for (int i : set) {
            if (i == x) {
                return true;
            }
        }
        return false;
    }

    public int get(int i) {
        return set[i];
    }

    public int size() {
        return ptr;
    }

    public int[] getSet() {
        int[] res = new int[ptr];
        for (int i = 0; i < ptr; i++) res[i] = set[i];
        return res;
    }
}
