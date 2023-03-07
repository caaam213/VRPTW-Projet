package Logistique;

public abstract class Destination {

    GPSCoordinates localisation;
    String idName;
    int readyTime;
    int dueTime;

    public Destination(GPSCoordinates localisation, String idName, int readyTime, int dueTime) {
        this.localisation = localisation;
        this.idName = idName;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
    }

    public GPSCoordinates getLocalisation() {
        return localisation;
    }
}
