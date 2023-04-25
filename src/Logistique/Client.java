package Logistique;

public class Client extends Destination {

    int demand;
    int service;
    int nbClients;
    boolean isTheClientServed;
    int readyTime;
    int dueTime;
    GPSCoordinates localisation;
    String idName;

    public Client(GPSCoordinates localisation, String idName, int readyTime, int dueTime, int demand, int service) {
        super(localisation, idName, readyTime, dueTime);
        this.demand = demand;
        this.service = service;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.localisation = localisation;
        this.idName = idName;
    }


    public int getDemand() {
        return demand;
    }

    public boolean isTheClientServed() {
        return isTheClientServed;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Client{" +
                "demand=" + demand +
                ", service=" + service +
                ", nbClients=" + nbClients +
                ", isTheClientServed=" + isTheClientServed +
                ", localisation=" + super.localisation +
                ", idName=" + super.idName +
                ", readyTime=" + super.readyTime +
                ", dueTime=" + super.dueTime +
                '}';
    }

    public int getService() {
        return service;
    }

    public int getNbClients() {
        return nbClients;
    }


    @Override
    public Client clone() {
        return new Client(localisation, idName, readyTime, dueTime, demand, service);
    }

}
