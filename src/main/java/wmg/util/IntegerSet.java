package wmg.util;

public class IntegerSet {

    int[] set;

    public IntegerSet() {
        set = new int[0];
    }

    public void add(int x) {
        if (!this.contains(x)) {
            int[] clone = new int[set.length + 1];
            for (int i = 0; i < set.length; i++) {
                clone[i] = set[i];
            }
            clone[clone.length - 1] = x;
            set = clone;
        }
    }

    public void addAll(int... args) {
        for (int i : args) {
            add(i);
        }
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
        return set.length;
    }

    public int[] getSet() {
        return set;
    }

}
