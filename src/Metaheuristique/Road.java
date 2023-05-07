package Metaheuristique;
import Logistique.Client;
import Logistique.Destination;

import java.awt.*;
import java.util.ArrayList;

import static Utils.SolutionUtils.*;
import Graphics.SolutionVisualization;
import Utils.SolutionUtils;

public class Road implements Cloneable{
    private int distance;

    private int time;

    private int capacityDelivered;

    private ArrayList<Destination> destinations;
    private ArrayList<Edge> edges;

    private static int nbRoad = 1;
    private int idRoad;

    private String color;

    public Road() {
        distance = 0;
        time = 0;
        capacityDelivered = 0;
        destinations = new ArrayList<Destination>();
        edges = new ArrayList<Edge>();
        idRoad = nbRoad;
        nbRoad++;
        color = SolutionVisualization.generateRandomColor();
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

    /*public void addEdge(Edge edge) {

        if (edge.getArriveClient() != null)
        {
            this.edges.add(edge);
            distance += edge.getDistance();
            time = edge.getTime();
            capacityDelivered += edge.getQuantityDelivered();
        }
    }

    public void addEdgeToRoad(Edge edge) {
        this.edges.add(edge);
        distance += edge.getDistance();
        time = edge.getTime();
        capacityDelivered += edge.getQuantityDelivered();
    }*/

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

    public void removeDestinationToRoad(int indexDest)
    {
        Edge firstEdge = this.getEdges().get(indexDest);
        Edge secondEdge = this.getEdges().get(indexDest);
        this.edges.remove(indexDest-1);
        this.edges.remove(indexDest);
        this.getDestinations().remove(indexDest);
        distance = distance - firstEdge.getDistance() - secondEdge.getDistance();
        time = edges.get(edges.size()-1).getTime();
        capacityDelivered = firstEdge.getQuantityDelivered() - secondEdge.getQuantityDelivered();
    }


    private int[] getDistanceAndCapacityByIndex(int indexDest)
    {
        int distance = 0;
        int capacityDelivered = 0;
        for (Edge edge : this.getEdges())
        {
            if (edge.getPosEdge() == indexDest)
            {
               distance += edge.getDistance();
               capacityDelivered += edge.getQuantityDelivered();
            }
        }
        return new int[]{distance, capacityDelivered};
    }



    public void removeDestinationToRoadAndUpdateInfo(int indexDest)
    {
        time = 0;
        distance = 0;
        capacityDelivered = 0;
        edges.clear();

        destinations.remove(indexDest);

        int distanceCalculated;
        int capacityDeliveredCalculated;
        //Reconstruct the edges
        for(int i = 1; i<destinations.size(); i++)
        {

            Destination startDestination = destinations.get(i-1);
            Destination endDestination = destinations.get(i);

            if(startDestination.getIdName() == endDestination.getIdName())
                break;
            Edge edge = new Edge(startDestination, endDestination);

            distanceCalculated = distanceBetweenTwoDestination(startDestination, endDestination);
            distance += distanceCalculated;
            edge.setDistance(distanceCalculated);

            time = SolutionUtils.calculateTime(endDestination, time,distanceCalculated);
            edge.setTime(time);

            capacityDeliveredCalculated = endDestination instanceof Client ? ((Client) endDestination).getDemand() : 0;
            capacityDelivered += capacityDeliveredCalculated;
            edge.setQuantityDelivered(capacityDeliveredCalculated);

            edge.setPosEdge(i-1);
            edges.add(edge);

        }
    }


    public Road addDestinationsAndUpdateEdgeToRoad(Destination destination, int indexDest) {
        this.getDestinations().add(indexDest, destination);
        Road newRoad = new Road();
        int currentDistance = 0;
        for( int i = 0; i < this.getEdges().size()+2; i++)
        {
            Destination firstDestination = this.getDestinations().get(i);
            Destination secondDestination = this.getDestinations().get(i+1);
            Edge edge = new Edge(firstDestination, secondDestination);
            currentDistance = currentDistance + distanceBetweenTwoDestination(firstDestination, secondDestination);
            edge.setDistance(distanceBetweenTwoDestination(firstDestination, secondDestination));
            edge.setTime(currentDistance);
            edge.setPosEdge(i);
            if (secondDestination instanceof Client)
                edge.setQuantityDelivered(((Client) secondDestination).getDemand());
            newRoad.addDestinationsToRoad(firstDestination, edge);
            //capacityRemained = sol.getConfig().getTruck().getCapacity() - newRoad.getCapacityDelivered();
            //int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(firstDestination, secondDestination);
            //newRoad = calculateRoad(secondDestination, newRoad.getTime(), distanceBetweenTwoDestinations, capacityRemained, newRoad, edge, i);
        }
        newRoad.getDestinations().add(this.getDestinations().get(0));
        return newRoad;
    }

    public Road constrcutEdgeToRoad() {
        Road newRoad = new Road();
        int currentDistance = 0;
        for( int i = 0; i < this.getEdges().size(); i++)
        {
            Destination firstDestination = this.getDestinations().get(i);
            Destination secondDestination = this.getDestinations().get(i+1);
            Edge edge = new Edge(firstDestination, secondDestination);
            currentDistance = currentDistance + distanceBetweenTwoDestination(firstDestination, secondDestination);
            edge.setDistance(distanceBetweenTwoDestination(firstDestination, secondDestination));
            edge.setTime(currentDistance);
            edge.setPosEdge(i);
            if (secondDestination instanceof Client)
                edge.setQuantityDelivered(((Client) secondDestination).getDemand());
            newRoad.addDestinationsToRoad(firstDestination, edge);
            //capacityRemained = sol.getConfig().getTruck().getCapacity() - newRoad.getCapacityDelivered();
            //int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(firstDestination, secondDestination);
            //newRoad = calculateRoad(secondDestination, newRoad.getTime(), distanceBetweenTwoDestinations, capacityRemained, newRoad, edge, i);
        }
        newRoad.getDestinations().add(this.getDestinations().get(0));
        return newRoad;
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

    public void setIdRoad(int idRoad) {
        this.idRoad = idRoad;
    }

    public int getTimeByIndex(int indexDest) {
        return this.getEdges().get(indexDest).getTime();
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Road clone() {
        Road road = null;
        try {
            road = (Road) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        road.setEdges((ArrayList<Edge>) edges.clone());
        ArrayList<Destination> destinationsCloned = new ArrayList<Destination>();
        for (Destination destination : destinations)
        {
            destinationsCloned.add(destination.clone());
        }

        ArrayList<Edge> edgesCloned = new ArrayList<Edge>();
        for (Edge edge : road.getEdges())
        {
            edgesCloned.add(edge.clone());
        }
        road.setDestinations(destinationsCloned);
        road.setColor(color);
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

    public ArrayList<String> returnListOfIdClient()
    {
        ArrayList<String> listOfIdClient = new ArrayList<String>();
        for (Destination destination : destinations)
        {
            if (destination instanceof Client)
            {
                listOfIdClient.add(destination.getIdName());
            }
        }
        return listOfIdClient;

    }

    public boolean verifyIfEdgeInRoad(String startId, String endId)
    {

        for (Edge edgeInRoad : edges)
        {
            if (edgeInRoad.getDepartClient().getIdName().equals(startId) && edgeInRoad.getArriveClient().getIdName().equals(endId))
                return true;
        }
        return false;
    }



}
