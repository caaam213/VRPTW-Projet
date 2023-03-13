package Logistique;

public abstract class Destination {

    protected GPSCoordinates localisation;
    protected String idName;
    protected int readyTime;
    protected int dueTime;

    public Destination(GPSCoordinates localisation, String idName, int readyTime, int dueTime) {
        this.localisation = localisation;
        this.idName = idName;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public int getDueTime() {
        return dueTime;
    }

    public String getIdName() {
        return idName;
    }

    public GPSCoordinates getLocalisation() {
        return localisation;
    }
}
