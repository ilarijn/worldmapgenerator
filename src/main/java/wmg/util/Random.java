package wmg.util;

/**
 * 64-bit Mersenne Twister pseudorandom number generator. Based on:
 * http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/VERSIONS/C-LANG/mt19937-64.c
 * https://en.wikipedia.org/wiki/Mersenne_Twister#Pseudocode
 */
public class Random {

    private final int n;
    private int index;
    private final int LOWER_MASK;
    private final int UPPER_MASK;

    private final long[] mt;

    /**
     * Constructor.
     * @param seed Seed number.
     */
    
    public Random(long seed) {
        n = 624;

        LOWER_MASK = 0x7fffffff;
        UPPER_MASK = 0x80000000;

        mt = new long[n];
        mt[0] = seed;

        for (index = 1; index < n; index++) {
            mt[index] = (6364136223846793005L * (mt[index - 1] ^ (mt[index - 1] >> 62)) + index);
        }
    }

    private long getRandom() {
        if (index >= n) {
            twist();
        }

        long y = mt[index++];
        y = y ^ ((y >> 29) & 0x5555555555555555L);
        y = y ^ ((y << 17) & 0x71D67FFFEDA60000L);
        y = y ^ ((y << 37) & 0xFFF7EEE000000000L);
        y = y ^ (y >> 43);

        return y;
    }

    private void twist() {
        for (int i = 0; i < n; i++) {
            long x = (mt[i] & UPPER_MASK)
                    + (mt[(i + 1) % n] & LOWER_MASK);
            long xA = x >> 1;
            if ((x % 2) != 0) {
                xA = xA ^ 0xB5026F5AA96619E9L;
            }
            mt[i] = mt[(i + 156) % n] ^ xA;

        }
        index = 0;
    }

    /**
     * Get next value in the sequence.
     * @return 
     */
    public double nextDouble() {
        return (getRandom() >> 11) * (2.0 / 9007199254740992.0);
    }
}
