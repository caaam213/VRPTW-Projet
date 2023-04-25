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

    int idSolutionCreated;

    public Solution() {
        idSolutionCreated = idSolution;
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

    public Road getARoad(int indexRoad) {
        return roads.get(indexRoad);
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
        int countRoad = 1;
        for(Road road : roads)
        {
            System.out.println("Route : "+countRoad);
            System.out.println("Temps : "+road.getTime());
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
            System.out.println("Distance parcourue : "+road.getDistance()+"km");
            System.out.println("");
            System.out.println("Fin de la route");
            System.out.println("");
            countRoad++;
        }

    }

    public void removeRoadByItsId(int idRoad)
    {
        int index = 0;
        for (Road road : roads)
        {
            if (road.getIdRoad() == idRoad)
            {
                roads.remove(index);
                break;
            }
            index++;
        }
    }

    public int getIdSolutionCreated() {
        return idSolutionCreated;
    }

    public Solution clone()
    {
        Solution newSolution = new Solution();
        newSolution.setConfig(this.getConfig());
        newSolution.setTotalDistanceCovered();
        ArrayList<Road> clonedList = new ArrayList<Road>();
        for (Road road : this.getRoads()) {
            clonedList.add(road.clone());
        }
        newSolution.setRoads(clonedList);

        return newSolution;
    }


}
