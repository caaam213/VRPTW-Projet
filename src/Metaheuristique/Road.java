package Metaheuristique;
import Logistique.Destination;

import java.awt.*;
import java.util.ArrayList;

public class Road {
    private int distance;
    private ArrayList<Destination> destinations;

    private static int nbRoad = 1;
    private int idRoad;
    private Color color;

    public Color getColor() {
        return color;
    }

    public Road() {
        distance = 0;
        destinations = new ArrayList<Destination>();
        idRoad = nbRoad;
        nbRoad++;
    }

    public int getIdRoad() {
        return idRoad;
    }

    public Road(int distance) {
        this.distance = distance;
        destinations = new ArrayList<Destination>();
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

    public void addDestinationsToRoad(Destination destination) {
        this.destinations.add(destination);
    }
}
