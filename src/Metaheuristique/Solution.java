package Metaheuristique;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;

import java.util.ArrayList;

public class Solution {

    ArrayList<Road> roads = new ArrayList<Road>();

    private Configuration config;
    int totalDistanceCovered = 0;
    static int idSolution = 0;

    public Solution() {
        idSolution += 1;
    }


    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }

    public static int getIdSolution() {
        return idSolution;
    }

    public void addRoads(Road road) {
        roads.add(road);
    }

    public void setTotalDistanceCovered(int totalDistanceCovered) {
        this.totalDistanceCovered = totalDistanceCovered;
    }

    public int getTotalDistanceCovered() {
        return totalDistanceCovered;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     *
     * Display the solution in the console
     */
    public void displaySolution()
    {
        System.out.println("Il y a "+roads.size()+" routes");
        System.out.println("Distance totale parcourue : "+totalDistanceCovered+"km");
        System.out.println();
        for(Road road : roads)
        {
            System.out.println("Route : "+road.getIdRoad());
            System.out.println("Destination : ");
            for (Destination destination : road.getDestinations())
            {

                System.out.print(destination.getIdName()+"->");
            }
            System.out.println("Fin de la route");
            System.out.println("Distance parcourue : "+road.getDistance());
            System.out.println("");
        }

    }
}
