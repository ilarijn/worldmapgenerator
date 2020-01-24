package wmg.util;

public class Vector2 {

    private double y, x;

    public Vector2(double y, double x) {
        this.y = y;
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    @Override
    public String toString() {
        return "y: " + y + ", x: " + x;
    }

}
