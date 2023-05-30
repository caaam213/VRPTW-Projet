package Logistics;

/**
 * Cette classe représente les coordonnées GPS d'une destination
 */
public class GPSCoordinates {
    int x;
    int y;

    /**
     * @param x Coordonnée x
     * @param y Coordonnée y
     */
    public GPSCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return La coordonnée x
     */
    public int getX() {
        return x;
    }

    /**
     * @return La coordonnée y
     */
    public int getY() {
        return y;
    }

    /**
     * @return L'affichage textuel des coordonnées GPS
     */
    @Override
    public String toString() {
        return "GPSCoordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
