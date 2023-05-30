package Metaheuristics;
import Logistics.Client;
import Logistics.Destination;

import java.util.ArrayList;
import java.util.Objects;

import Graphics.SolutionVisualization;

/**
 * Cette classe représente une route
 */
public class Road implements Cloneable{
    private ArrayList<Destination> destinations;
    private int idRoad;
    private String color;
    private int distance;

    /**
     * @param roadId L'identifiant de la route
     */
    public Road(int roadId) {
        destinations = new ArrayList<Destination>();
        idRoad = roadId;
        distance = 0;
        color = SolutionVisualization.generateRandomColor();
    }

    /**
     * @param distance La distance de la route
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * @return La distance de la route
     */
    public int getDistance() {
        return distance;
    }

    /**
     * @param obj L'objet à comparer
     * @return Vrai si les deux routes sont égales, faux sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this.returnListOfIdClient().equals(((Road) obj).returnListOfIdClient()))
            return true;
        return false;
    }

    /**
     * @return La liste des identifiants des clients de la route
     */
    public int getIdRoad() {
        return idRoad;
    }

    /**
     * @return La liste des destinations de la route
     */
    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    /**
     * @param destinations La liste des destinations de la route
     */
    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * @param idRoad L'identifiant de la route
     */
    public void setIdRoad(int idRoad) {
        this.idRoad = idRoad;
    }

    /**
     * @return La couleur de la route
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color La couleur de la route
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return Un clone de la route
     */
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

    /**
     * @return La route sous format texte
     */
    public String toString()
    {
        String str = "Road " + idRoad + " : ";
        for (Destination destination : destinations)
        {
            str += destination.getIdName() + " ";
        }

        return str;
    }

    /**
     * @return La liste des identifiants des clients de la route
     */
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

    @Override
    public int hashCode() {
        return Objects.hashCode(returnListOfIdClient());
    }





}
