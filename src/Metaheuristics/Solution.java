package Metaheuristics;

import Logistics.Configuration;
import Logistics.Destination;

import java.util.ArrayList;
import java.util.Objects;

import Utils.SolutionUtils;

/**
 * Cette classe représente une solution
 */
public class Solution implements Cloneable {

    ArrayList<Road> roads = new ArrayList<Road>();

    private Configuration config;
    int totalDistanceCovered = 0;

    /**
     * @param config La configuration de la solution
     */
    public void setConfig(Configuration config) {
        this.config = config;
    }

    /**
     * @return La configuration de la solution
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * @param road : Ajoute une route à la solution
     */
    public void addRoads(Road road) {
        roads.add(road);
    }

    /**
     * Recalcule la distance totale parcourue par la solution
     */
    public void reCalculateTotalDistanceCovered() {
        totalDistanceCovered = 0;
        for (Road road : roads) {
            int distance = 0;
            for(int i =0;i<road.getDestinations().size()-1;i++)
            {
                distance += SolutionUtils.distanceBetweenTwoDestination(road.getDestinations().get(i),road.getDestinations().get(i+1));

            }
            totalDistanceCovered += distance;
            road.setDistance(distance);
        }

    }

    /**
     * @param totalDistanceCovered La distance totale parcourue par la solution
     */
    public void setTotalDistanceCovered(int totalDistanceCovered) {
        this.totalDistanceCovered = totalDistanceCovered;
    }

    /**
     * @return La distance totale parcourue par la solution
     */
    public int getTotalDistanceCovered() {
        return totalDistanceCovered;
    }

    /**
     * @return La liste des routes de la solution
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * @param indexRoad L'index de la route à récupérer
     * @return La route à l'index donné
     */
    public Road getARoad(int indexRoad) {
        return roads.get(indexRoad);
    }

    /**
     * @param roads La liste des routes à modifier
     */
    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
    }


    /**
     * Affiche la solution sous forme de texte
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
            System.out.println("Destination : ");
            for (Destination destination : road.getDestinations())
            {

                System.out.print(destination.getIdName()+"-");
            }
            System.out.println("");
            System.out.println("Distance parcourue : "+road.getDistance()+"km");
            System.out.println("");
            System.out.println("Fin de la route");
            System.out.println("");
            countRoad++;
        }

    }


    /**
     * @return Une copie de la solution
     */
    public Solution clone()
    {
        Solution newSolution = new Solution();
        newSolution.setConfig(this.getConfig());
        newSolution.setTotalDistanceCovered(this.getTotalDistanceCovered());
        ArrayList<Road> clonedList = new ArrayList<Road>();
        for (Road road : this.getRoads()) {
            clonedList.add(road.clone());
        }
        newSolution.setRoads(clonedList);

        return newSolution;
    }

    /**
     * Supprime une route de la solution et met à jour les id des routes
     * @param indexRoad L'index de la route à supprimer
     */
    public void removeRoad(int indexRoad)
    {
        roads.remove(indexRoad);
        for(int i = 0; i<roads.size();i++)
        {
            roads.get(i).setIdRoad(i+1);
        }

    }

    /**
     * Compare deux solutions
     * @param o La solution à comparer
     * @return true si les deux solutions sont égales, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if(o == null )
        {
            return false;
        }
        Solution solution = (Solution) o;
        for (int i = 0; i < roads.size(); i++) {
            if (solution.getRoadById(roads.get(i).getIdRoad()) == null) {
                return false;
            }
            if (!roads.get(i).equals(solution.getRoadById(roads.get(i).getIdRoad()))) {
                    return false;
            }
        }
        return true;
    }

    /**
     * @param idRoad L'id de la route à récupérer
     * @return La route ayant l'id donné
     */
    public Road getRoadById(int idRoad)
    {
        for (Road road : roads)
        {
            if (road.getIdRoad() == idRoad)
            {
                return road;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roads);
    }
}
