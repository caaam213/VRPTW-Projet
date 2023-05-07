package Metaheuristique;

import Logistique.Destination;
import Utils.SolutionUtils;
public class Edge {
    private Destination departClient;
    private Destination arriveClient;
    private int distance;
    private int time;
    private int quantityDelivered;
    private int posEdge;

    public Edge(Destination departClient, Destination arriveClient, int distance) {
        this.departClient = departClient;
        this.arriveClient = arriveClient;
        this.distance = distance;
    }

    public Edge(Destination departClient, Destination arriveClient) {
        this.departClient = departClient;
        this.arriveClient = arriveClient;
    }

    public Edge(Destination departClient) {
        this.departClient = departClient;
    }

    public Destination getDepartClient() {
        return departClient;
    }

    public Destination getArriveClient() {
        return arriveClient;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


    public void setDepartClient(Destination departClient) {
        this.departClient = departClient;
    }

    public void setArriveClient(Destination arriveClient) {
        this.arriveClient = arriveClient;
        this.distance = SolutionUtils.distanceBetweenTwoDestination(this.departClient, this.arriveClient);
    }

    public String toString() {
        return departClient.getIdName() + "->"+ arriveClient.getIdName()+", "+distance+"km"+", "+time+"min "+", "+quantityDelivered+"\n";
    }


    public boolean equals(Destination destination1, Destination destination2) {
        return (this.departClient.getIdName().equals(destination1.getIdName()) &&
                this.arriveClient.getIdName().equals(destination2.getIdName()));
    }

    public Edge clone() {
        Edge edge = new Edge(this.departClient, this.arriveClient, this.distance);
        edge.setPosEdge(this.posEdge);
        edge.setQuantityDelivered(this.quantityDelivered);
        edge.setTime(this.time);
        return edge;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPosEdge() {
        return posEdge;
    }

    public void setPosEdge(int posEdge) {
        this.posEdge = posEdge;
    }


    public int getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(int quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }
}
