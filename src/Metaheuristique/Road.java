package Metaheuristique;
import Logistique.Client;
import Logistique.Destination;

import java.awt.*;
import java.util.ArrayList;

import static Utils.SolutionUtils.*;
import Graphics.SolutionVisualization;
import Utils.SolutionUtils;

public class Road implements Cloneable{

    private ArrayList<Destination> destinations;

    private static int nbRoad = 1;
    private int idRoad;

    private String color;

    public Road() {
        destinations = new ArrayList<Destination>();
        idRoad = nbRoad;
        nbRoad++;
        color = SolutionVisualization.generateRandomColor();
    }

    public int calculateDistance()
    {
        int distance = 0;
        for(int i =0;i<destinations.size()-1;i++)
        {
            distance += SolutionUtils.distanceBetweenTwoDestination(destinations.get(i),destinations.get(i+1));
        }
        return distance;
    }
    public int getIdRoad() {
        return idRoad;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
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










    public void removeDestinationToRoadAndUpdateInfo(int indexDest)
    {

        destinations.remove(indexDest);

    }


    public void setIdRoad(int idRoad) {
        this.idRoad = idRoad;
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

        ArrayList<Destination> destinationsCloned = new ArrayList<Destination>();
        for (Destination destination : destinations)
        {
            destinationsCloned.add(destination.clone());
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





}
