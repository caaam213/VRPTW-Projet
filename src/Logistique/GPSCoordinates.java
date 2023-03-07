package Logistique;

public class GPSCoordinates {
    int x;
    int y;

    public GPSCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "GPSCoordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
