package Logistique;

public class Client extends Destination {

    int demand;
    int service;
    int nbClients;
    int deleveryTime;
    boolean isTheClientServed;

    public Client(GPSCoordinates localisation, String idName, int readyTime, int dueTime, int demand, int service) {
        super(localisation, idName, readyTime, dueTime);
        this.demand = demand;
        this.service = service;
    }

    @Override
    public String toString() {
        return "Client{" +
                "demand=" + demand +
                ", service=" + service +
                ", nbClients=" + nbClients +
                ", deleveryTime=" + deleveryTime +
                ", isTheClientServed=" + isTheClientServed +
                ", localisation=" + super.localisation +
                ", idName=" + super.idName +
                ", readyTime=" + super.readyTime +
                ", dueTime=" + super.dueTime +
                '}';
    }

}
