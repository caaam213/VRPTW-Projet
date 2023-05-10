package Metaheuristique.NeighborOperators;

import Graphics.SolutionVisualization;
import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Class to manage 2-opt and cross-exchange neighbor operators
 */
public class TwoOptAndCrossExchange extends NeighborsOperators {

    private static ArrayList<Destination> reverseList(ArrayList<Destination> list, int i, int j)
    {
        ArrayList<Destination> reversedList = new ArrayList<>();
        for (int k = 0; k < i; k++)
        {
            reversedList.add(list.get(k));
        }
        for (int k = j; k >= i; k--)
        {
            reversedList.add(list.get(k));
        }
        for (int k = j+1; k < list.size(); k++)
        {
            reversedList.add(list.get(k));
        }
        return reversedList;
    }

    public static ArrayList<Destination> getSubList(ArrayList<Destination> list, int i, int j)
    {
        ArrayList<Destination> subList = new ArrayList<>();
        for (int k = i; k <= j; k++)
        {
            subList.add(list.get(k));
        }
        return subList;
    }

    public static Result runTwoOpt(Solution solution, int selectedRoad, int indexClient1, int indexClient2)
    {
        if (indexClient1 == indexClient2)
            return null;

        if (indexClient2 < indexClient1)
        {
            int temp = indexClient1;
            indexClient1 = indexClient2;
            indexClient2 = temp;
        }

        if(abs(indexClient1 - indexClient2) <= 2)
            return null;

        if (indexClient1 == 0 || indexClient1 == solution.getRoads().get(selectedRoad).getDestinations().size()-1)
            return null;

        if (indexClient2 == 0 || indexClient2 == solution.getRoads().get(selectedRoad).getDestinations().size()-1)
            return null;


        Solution neighbor = solution.clone();
        Road road = neighbor.getRoads().get(selectedRoad);


        ArrayList<Destination> destinations = road.getDestinations();


        destinations = reverseList(destinations, indexClient1+1, indexClient2-1);



        neighbor.getRoads().get(selectedRoad).setDestinations(destinations);
        neighbor = verifyIfRoadValid(neighbor, selectedRoad);

        if (neighbor == null)
            return null;

        Transformation transformation = new Transformation("2-opt", selectedRoad,selectedRoad, indexClient1, indexClient2);
        neighbor.reCalculateTotalDistanceCovered();
        Result result = new Result(neighbor, transformation);
        return result;
    }



    public static Result runCrossExchange(Solution solution, int selectedRoad1, int selectedRoad2, int indexClient1, int indexClient2)
    {
        if (indexClient1==0 || indexClient1 >= solution.getRoads().get(selectedRoad1).getDestinations().size()-2)
            return null;

        if (indexClient2==0 || indexClient2 >= solution.getRoads().get(selectedRoad2).getDestinations().size()-1)
            return null;

        if (indexClient1 == indexClient2)
            return null;


        Solution neighbor = solution.clone();
        Road road1 = neighbor.getRoads().get(selectedRoad1);
        Road road2 = neighbor.getRoads().get(selectedRoad2);

        ArrayList<Destination> destinations1 = road1.getDestinations();
        ArrayList<Destination> destinations2 = road2.getDestinations();

        ArrayList<Destination> newDestinations1 = getSubList(destinations1, 0, indexClient1);
        ArrayList<Destination> subList1NewClient1End = getSubList(destinations2, indexClient2+1, destinations2.size()-1);
        newDestinations1.addAll(subList1NewClient1End);

        ArrayList<Destination> newDestinations2 = getSubList(destinations2, 0, indexClient2);
        ArrayList<Destination> subListNewClient2End = getSubList(destinations1, indexClient1+1, destinations1.size()-1);
        newDestinations2.addAll(subListNewClient2End);


        neighbor.getRoads().get(selectedRoad1).setDestinations(newDestinations1);
        neighbor.getRoads().get(selectedRoad2).setDestinations(newDestinations2);

        neighbor = verifyIfRoadValid(neighbor, selectedRoad1);
        if (neighbor == null)
            return null;

        neighbor = verifyIfRoadValid(neighbor, selectedRoad2);

        if (neighbor == null)
            return null;

        Transformation transformation = new Transformation("Cross-exchange", selectedRoad1, selectedRoad2, indexClient1, indexClient2);
        neighbor.reCalculateTotalDistanceCovered();

        Result result = new Result(neighbor, transformation);
        return result;

    }


    /**
     * Generate all neighbors of a solution and display it
     * @param solution Solution to generate neighbors
     */
    public static void generateAllNeighbors2Opt(Solution solution)
    {
        for (int selectedRoad = 0; selectedRoad < solution.getRoads().size(); selectedRoad++)
        {

            for (int i =0 ; i < solution.getRoads().get(selectedRoad).getDestinations().size()-1; i++)
            {
                for (int j = i+2; j < solution.getRoads().get(selectedRoad).getDestinations().size()-1 ; j++)
                {
                    Result map = TwoOptAndCrossExchange.runTwoOpt(solution, selectedRoad, i, j);

                    if (map == null)
                    {
                        continue;
                    }
                    Solution solutionNeighbour = map.getSolution();


                    if (solutionNeighbour.getRoads().get(selectedRoad)!=null)
                    {
                        solutionNeighbour.getRoads().get(selectedRoad).toString();
                        SolutionVisualization.DisplayGraph(solutionNeighbour, "Solution 2-opt");
                    }
                }
            }
        }
        System.out.println("Fin du programme 2-opt");

    }

    public static void generateAllNeighborsCrossExchange(Solution solution)
    {
        for (int selectedRoad = 0; selectedRoad < solution.getRoads().size(); selectedRoad++)
        {
            for (int selectedRoad2 = selectedRoad+1; selectedRoad2 < solution.getRoads().size(); selectedRoad2++)
            {
                for (int i =0 ; i < solution.getRoads().get(selectedRoad).getDestinations().size()-1; i++)
                {
                    for (int j =0; j < solution.getRoads().get(selectedRoad2).getDestinations().size()-1; j++)
                    {

                        Result res = runCrossExchange(solution, selectedRoad, selectedRoad2, i, j);
                        if (res == null)
                            continue;
                        Solution solutionNeighbour = res.getSolution();
                        if (solutionNeighbour != null)
                        {
                            System.out.println("Roads selected : "+selectedRoad+" et "+selectedRoad2);
                            //solutionNeighbour.displaySolution();
                            System.out.println("Distance totale parcourue : "+solutionNeighbour.getTotalDistanceCovered());
                            SolutionVisualization.DisplayGraph(solutionNeighbour, "Cross-exchange");

                        }
                    }
                }
            }
        }
        System.out.println("Fin du programme cross-exchange");
    }




}
