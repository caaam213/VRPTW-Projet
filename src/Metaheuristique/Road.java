package Metaheuristique;
import Logistique.Destination;

import java.awt.*;
import java.util.ArrayList;

public class Road implements Cloneable{
    private int distance;

    private int time;

    private int capacityDelivered;

    private ArrayList<Destination> destinations;
    private ArrayList<Edge> edges;

    private static int nbRoad = 1;
    private int idRoad;

    public Road() {
        distance = 0;
        time = 0;
        capacityDelivered = 0;

        destinations = new ArrayList<Destination>();
        edges = new ArrayList<Edge>();
        idRoad = nbRoad;
        nbRoad++;
    }

    public int getIdRoad() {
        return idRoad;
    }

    public Road(int distance) {
        this.distance = distance;
        destinations = new ArrayList<Destination>();
        edges = new ArrayList<Edge>();
    }

    public int getDistance() {
        return distance;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * Add a destination to the road and update information
     * @param destination destination to add
     * @param edge edge to add
     */
    public void addDestinationsToRoad(Destination destination, Edge edge) {

        this.destinations.add(destination);
        if (edge.getArriveClient() != null)
        {
            this.edges.add(edge);
            distance += edge.getDistance();
            time = edge.getTime();
            capacityDelivered += edge.getQuantityDelivered();
        }
    }

    /**
     * Remove an edge to the road and update information
     * @param indexDest index of the edge to remove
     * @param edge edge to remove
     */
    public void removeEdgeToRoad(int indexDest, Edge edge) {
        this.edges.remove(indexDest);
        distance -= edge.getDistance();
        time = edges.get(edges.size()-1).getTime();
        capacityDelivered -= edge.getQuantityDelivered();

    }


    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Destination getClientByIdName(String idName)
    {
        for (Destination destination : destinations)
        {
            if (destination.getIdName().equals(idName))
            {
                return (Destination) destination;
            }
        }
        return null;
    }



    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCapacityDelivered() {
        return capacityDelivered;
    }

    public void setCapacityDelivered(int capacity) {
        this.capacityDelivered = capacity;
    }

    public Road clone() {
        Road road = null;
        try {
            road = (Road) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        road.setEdges((ArrayList<Edge>) edges.clone());
        road.setDestinations((ArrayList<Destination>) destinations.clone());
        return road;
    }

    public String toString()
    {
        String str = "Road " + idRoad + " : ";
        for (Destination destination : destinations)
        {
            str += destination.getIdName() + " ";
        }

        for (Edge edge : edges)
        {
            System.out.print(edge.toString());
        }
        return str;
    }


}
