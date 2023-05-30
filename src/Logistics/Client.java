package Logistics;

/**
 * Cette classe représente une instance d'un client
 */
public class Client extends Destination {

    int demand;
    int service;
    int readyTime;
    int dueTime;
    GPSCoordinates gpsCoordinates;
    String idName;

    public Client(GPSCoordinates gpsCoordinates, String idName, int readyTime, int dueTime, int demand, int service) {
        super(gpsCoordinates, idName, readyTime, dueTime);
        this.demand = demand;
        this.service = service;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.gpsCoordinates = gpsCoordinates;
        this.idName = idName;
    }


    /**
     * @return La quantité demandée par le client
     */
    public int getDemand() {
        return demand;
    }


    /**
     * @return Le temps que le livreur doit passer chez le client
     */
    public int getService() {
        return service;
    }


    /**
     * @return Un double de l'objet
     */
    @Override
    public Client clone() {
        return new Client(gpsCoordinates, idName, readyTime, dueTime, demand, service);
    }



    /**
     * @return Affichage textuel d'un client avec ses informations
     */
    @Override
    public String toString() {
        return "Client{" +
                "demand=" + demand +
                ", service=" + service +
                ", localisation=" + super.localisation +
                ", idName=" + super.idName +
                ", readyTime=" + super.readyTime +
                ", dueTime=" + super.dueTime +
                '}';
    }




}
