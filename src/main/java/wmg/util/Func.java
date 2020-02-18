package wmg.util;

/**
 * Collection of math functions.
 */

public class Func {

    public final static double PI = 3.141592653589793;

    public static double lerp(double a, double b, double w) {
        return (1.0 - w) * a + w * b;
    }

    public static double dot(Vector2 a, Vector2 b) {
        return a.getY() * b.getY() + a.getX() * b.getX();
    }

    public static double dot(Vector2 a, double by, double bx) {
        return a.getY() * by + a.getX() * bx;
    }

    public static double fade(double x) {
        return 6 * Math.pow(x, 5) - 15 * Math.pow(x, 4) + 10 * Math.pow(x, 3);
    }

    public static Vector2 getVector(double val) {
        double angle = val * 2 * PI;
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }

    public static double pow(double x, double y) {
        if (y == 0) return 1;
        if (y == 1) return x;
        double res = x;
        for (int i = 2; i <= y; i++) res *= x;
        return res;
    }

    public static double abs(double x) {
        return (x < 0) ? (x * (-1)) : x;
    }
    
    public static int ceil(double x) {
        int x_int = (int) x;
        if (x_int == x) return x_int;
        else return (x_int + 1);
    }
}
