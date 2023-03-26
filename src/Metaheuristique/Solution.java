package Metaheuristique;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;

import java.util.ArrayList;
import Utils.SolutionUtils;
public class Solution implements Cloneable {

    ArrayList<Road> roads = new ArrayList<Road>();

    private Configuration config;
    int totalDistanceCovered = 0;
    int nbClientsServed = 0;
    int nbClients = 0;
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

    public void setTotalDistanceCovered() {
        for(Road road : roads)
        {
            totalDistanceCovered += road.getDistance();
        }

    }

    public int getTotalDistanceCovered() {
        return totalDistanceCovered;
    }

    public int getNbClientsServed() {
        return nbClientsServed;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
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
            System.out.println("Temps : "+road.getTime());

            int quantityDelivered = config.getTruck().getCapacity()-road.getCapacityDelivered();
            System.out.println("Quantite livree : "+quantityDelivered+"kg");
            System.out.println("Destination : ");
            for (Destination destination : road.getDestinations())
            {

                System.out.print(destination.getIdName()+"-");
            }
            System.out.println("");
            System.out.println("Edges : ");

            for (Edge edge : road.getEdges())
            {
                System.out.print(edge.toString());
            }
            System.out.println("");
            System.out.println("Fin de la route");
            System.out.println("");
        }

    }

    public boolean isSolutionValid()
    {
        for(Road road : roads)
        {
            if (!road.getEdges().get(0).getDepartClient().getIdName().equals("d1") ||
                    !road.getEdges().get(road.getEdges().size()-1).getArriveClient().getIdName().equals("d1"))
            {
                return false;
            }

            if(road.getDestinations().size() != config.getListClients().size()+2)
            {
                return false;
            }

            int time = 0;
            int capacity = config.getTruck().getCapacity();
            for (int i = 0; i < road.getDestinations().size()-1; i++) {
                Destination destination = road.getDestinations().get(i);
                Destination nextDestination = road.getDestinations().get(i+1);
                time += SolutionUtils.distanceBetweenTwoDestination(destination, nextDestination);
                if (destination instanceof Client) {
                    capacity -= ((Client) destination).getDemand();
                }
                if (time > nextDestination.getDueTime() || capacity < 0) {
                    return false;
                }
            }

        }
        return true;
    }

    public Solution clone()
    {
        Solution newSolution = new Solution();
        newSolution.setConfig(this.getConfig());
        newSolution.setTotalDistanceCovered();
        newSolution.setRoads((ArrayList<Road>) this.getRoads().clone());
        return newSolution;
    }


}
