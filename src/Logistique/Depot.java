package Logistique;

public class Depot extends Destination {

    public Depot(GPSCoordinates localisation, String idName, int readyTime, int dueTime) {
        super(localisation, idName, readyTime, dueTime);
    }

    @Override
    public String toString() {
        return "Depot{" +
                "localisation=" + super.localisation +
                ", idName=" + super.idName +
                ", readyTime=" + super.readyTime +
                ", dueTime=" + super.dueTime +
                '}';
    }
}
