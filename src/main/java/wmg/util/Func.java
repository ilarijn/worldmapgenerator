package wmg.util;

/**
 * A collection of math functions.
 */
public class Func {

    public static final double PI = 3.141592653589793;

    /**
     * Absolute value.
     *
     * @return The absolute value of the argument.
     */
    public static double abs(double x) {
        return (x < 0) ? (x * (-1)) : x;
    }

    /**
     * Ceiling.
     *
     * @return The closest integer value equal to or greater than the argument
     * value.
     */
    public static int ceil(double x) {
        int xInt = (int) x;
        if (xInt == x) {
            return xInt;
        } else {
            return (xInt + 1);
        }
    }

    /**
     * Dot product.
     *
     * @param a First argument point as a Vector2.
     * @param by Y coordinate of second argument point.
     * @param bx X coordinate of second argument point.
     * @return The dot product of the two argument points.
     */
    public static double dot(Vector2 a, double by, double bx) {
        return a.getY() * by + a.getX() * bx;
    }

    /**
     * Get a normalized Vector2 based on a double value.
     *
     * Used for generating random vectors.
     *
     * @param val An arbitrary double value.
     * @return A normalized Vector2 based the sine and cosine values of an angle
     * value computed from the argument.
     */
    public static Vector2 getVector(double val) {
        double angle = val * 2 * PI;
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Linear interpolation.
     *
     * @param v0 First input.
     * @param v1 Second input.
     * @param t Parameter/alpha value.
     */
    public static double lerp(double v0, double v1, double t) {
        return (1.0 - t) * v0 + t * v1;
    }

    /**
     * Exponentiation.
     *
     * @param x The base.
     * @param y The exponent.
     * @return The base number raised to the power of the second.
     */
    public static double pow(double x, double y) {
        if (y == 0) {
            return 1;
        }
        if (y == 1) {
            return x;
        }
        double res = x;
        for (int i = 2; i <= y; i++) {
            res *= x;
        }
        return res;
    }
}
