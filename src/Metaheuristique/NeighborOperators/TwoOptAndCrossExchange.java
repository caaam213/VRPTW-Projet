package Metaheuristique.NeighborOperators;

import Graphics.SolutionVisualization;
import Logistique.Client;
import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

import java.util.ArrayList;

/**
 * Class to manage 2-opt and cross-exchange neighbor operators
 */
public class TwoOptAndCrossExchange {

    /**
     * Get all destinations which are stable in the solution
     * @param road Road to get stable destinations
     * @param posEdge1 Position of the last edge of the stable road
     * @return Road with stable destinations
     */
    public static Road generateRoadNotChanged(Road road, int posEdge1)
    {
        Road roadInter = new Road();



        for (int i = 0; i < posEdge1; i++)
        {
            Destination desti = road.getDestinations().get(i);


            roadInter.addDestinationsToRoad(desti, road.getEdges().get(i));
        }

        return roadInter;
    }

    /**
     * Recalculate the road with the new edge
     * @param departClient Depart client
     * @param arriveClient Arrive client
     * @param time Time
     * @param distance Distance
     * @param capacityRemained Capacity remained
     * @param roadToReturn Road to return
     * @param posEdge1  Position
     * @return Road recalculated
     */
    private static Road resetEdge(Destination departClient, Destination arriveClient, int time, int distance, int capacityRemained, Road roadToReturn,  int posEdge1)
    {
        Edge edgeChanged = new Edge(departClient, arriveClient);
        if (SolutionUtils.isClientCanBeDelivered(departClient, arriveClient, time, capacityRemained))
            roadToReturn = SolutionUtils.calculateRoad(arriveClient, time, distance, capacityRemained, roadToReturn, edgeChanged, posEdge1);
        else
        {
            System.out.println("Null");
            return null;
        }
        return roadToReturn;
    }

    /**
     * Swap edges in the solution
     * @param solution Solution to swap edges
     * @param edges Edges of the road
     * @param posEdge1 Position of the first edge to swap
     * @param posEdge2 Position of the second edge to swap
     * @param edge1 edge1 to swap
     * @param edge2 edge2 to swap
     * @return Road with edges swapped
     */
    public static Road swapEdges(Solution solution, ArrayList<Edge> edges,int roadSelected,  int posEdge1, int posEdge2, Edge edge1, Edge edge2)
    {
        Road roadToReturn;
        int distance;
        int time;
        int capacityRemained;


        Destination departClient;
        Destination arriveClient;


        if (posEdge1 == 0)
        {
            roadToReturn = new Road();
            // Add the depot and the first client if the first edge to swap is the first edge of the road
            departClient = solution.getConfig().getCentralDepot();
            arriveClient = edge2.getDepartClient();
            Edge edge = new Edge(solution.getConfig().getCentralDepot());
            distance = 0;
            time = 0;
            capacityRemained = solution.getConfig().getTruck().getCapacity();
            roadToReturn.addDestinationsToRoad(departClient, edge);

        }
        else
        {
            roadToReturn = generateRoadNotChanged(solution.getRoads().get(roadSelected), posEdge1);
            distance = roadToReturn.getDistance();
            time = roadToReturn.getTime();
            capacityRemained = solution.getConfig().getTruck().getCapacity() - roadToReturn.getCapacityDelivered();
            // Add the first client if the first edge to swap is not the first edge of the road
            departClient = edge1.getDepartClient();
            arriveClient = edge2.getDepartClient();


        }

        if (posEdge1 != 0)
            roadToReturn.getDestinations().add(posEdge1, departClient);

        // First edge to swap
        roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, posEdge1);


        if (roadToReturn == null)
            return null;

        // Revert edges between the first edge to swap and the second edge to swap
        for (int i = posEdge2-1; i > posEdge1; i--)
            {
                time = roadToReturn.getTime();
                distance = roadToReturn.getDistance();
                capacityRemained = roadToReturn.getCapacityDelivered();

                Edge edge = edges.get(i);
                departClient = edge.getArriveClient();
                arriveClient = edge.getDepartClient();
                roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, i);
                if (roadToReturn == null)
                    return null;
            }




        // Second edge to swap
        departClient = edge1.getArriveClient();
        arriveClient = edge2.getArriveClient();
        roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, posEdge2+1);
        if (roadToReturn == null)
            return null;



        // Add the last edges of the road
        for (int i = posEdge2+1; i < edges.size()-1; i++)
        {
            time = roadToReturn.getTime();
            distance = roadToReturn.getDistance();
            capacityRemained = solution.getConfig().getTruck().getCapacity() - roadToReturn.getCapacityDelivered();


            Edge edge = edges.get(i);
            departClient = edge.getDepartClient();
            arriveClient = edge.getArriveClient();
            roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, i);
            if (roadToReturn == null)
                return null;
        }

        // Add the last edge of the road
        roadToReturn = addDepotToSwappedRoad(roadToReturn, solution, distance);

        return roadToReturn;
    }

    /**
     * @param solution Solution to swap edges
     * @param roadSelected Road selected
     * @param indexEdge1 Position of the first edge to swap
     * @param indexEdge2 Position of the second edge to swap
     * @return Solution with edges swapped
     */
    public static Solution runTwoOptIntra(Solution solution, int roadSelected, int indexEdge1, int indexEdge2)
    {
        // Edges verification
        if (Math.abs(indexEdge1-indexEdge2)<=1)
            return null;

        if (indexEdge1 > solution.getRoads().get(roadSelected).getEdges().size()-2 ||
                indexEdge2 > solution.getRoads().get(roadSelected).getEdges().size()-1)
            return null;

        if (indexEdge1 > indexEdge2)
        {
            int indexEdge = indexEdge1;
            indexEdge1 = indexEdge2;
            indexEdge2 = indexEdge;
        }

        // Get edges to swap by their position
        Edge edge1 = solution.getRoads().get(roadSelected).getEdges().get(indexEdge1);
        Edge edge2 = solution.getRoads().get(roadSelected).getEdges().get(indexEdge2);

        // Clone the roads and destinations
        ArrayList<Road> roads = (ArrayList<Road>) solution.getRoads().clone();
        Road road = roads.get(roadSelected).clone();
        ArrayList<Edge> edges = (ArrayList<Edge>) road.getEdges().clone();

        int posEdge1 = edge1.getPosEdge();
        int posEdge2 = edge2.getPosEdge();

        // Swap edges in the road
        Road roadGenerated = swapEdges(solution, edges, roadSelected, posEdge1, posEdge2, edge1, edge2);

        if (roadGenerated == null)
            return null;

        // Create the candidate solution and set road
        Solution candidateSolution = solution.clone();
        candidateSolution.getRoads().set(roadSelected, roadGenerated);
        candidateSolution.setTotalDistanceCovered();

        return candidateSolution;

    }

    private static Road addDepotToSwappedRoad(Road roadToReturn, Solution solution, int distance)
    {
        int[] calculateTotalDistance = SolutionUtils.calculateDistanceBetweenTheLastClientAndDepot(roadToReturn, solution.getConfig(), distance);
        int distanceBetweenTwoDestinations = calculateTotalDistance[1];

        // If the depot is not added, then it is added
        if (!roadToReturn.getEdges().get(roadToReturn.getEdges().size()-1).getArriveClient().getIdName().equals(solution.getConfig().getCentralDepot().getIdName()))
        {
            Edge endEdge = new Edge(roadToReturn.getDestinations().get(roadToReturn.getDestinations().size()-1), solution.getConfig().getCentralDepot());
            roadToReturn = SolutionUtils.addInfoToRoad(solution.getConfig(), distanceBetweenTwoDestinations,roadToReturn,endEdge, roadToReturn.getTime()+distanceBetweenTwoDestinations, roadToReturn.getDestinations().size()-1);
        }
        return roadToReturn;
    }

    private static Road exchangeEdgeBetweenTwoRoads(Solution solution,  int roadSelected,Road otherRoad,
                                                      int posEdge1,int posEdge2, Destination departClient, Destination arriveClient)
    {
        Road roadToReturn;
        int distance;
        int time;
        int capacityRemained;




        if (posEdge1 == 0)
        {
            roadToReturn = new Road();
            // Add the depot and the first client if the first edge to swap is the first edge of the road
            Edge edge = new Edge(solution.getConfig().getCentralDepot());
            distance = 0;
            time = 0;
            capacityRemained = solution.getConfig().getTruck().getCapacity();
            roadToReturn.addDestinationsToRoad(departClient, edge);

        }
        else
        {
            roadToReturn = generateRoadNotChanged(solution.getRoads().get(roadSelected), posEdge1);
            distance = roadToReturn.getDistance();
            time = roadToReturn.getTime();
            capacityRemained = solution.getConfig().getTruck().getCapacity() - roadToReturn.getCapacityDelivered();
            // Add the first client if the first edge to swap is not the first edge of the road
            roadToReturn.getDestinations().add(posEdge1, departClient);

        }

        // Exchange the clients between the two roads
        roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, posEdge1);

        if (roadToReturn == null)
            return null;



        // Get clients delivered after the second client to swap from the second road
        for (int i = posEdge2+1; i < otherRoad.getEdges().size()-1; i++)
        {
            time = roadToReturn.getTime();
            distance = roadToReturn.getDistance();
            capacityRemained = solution.getConfig().getTruck().getCapacity() - roadToReturn.getCapacityDelivered();


            departClient =  roadToReturn.getDestinations().get(roadToReturn.getDestinations().size()-1);
            arriveClient = otherRoad.getDestinations().get(i+1);
            roadToReturn = resetEdge(departClient, arriveClient, time, distance, capacityRemained, roadToReturn, i);
            if (roadToReturn == null)
                return null;
            if (arriveClient.getIdName().equals(solution.getConfig().getCentralDepot().getIdName()))
                return roadToReturn;
        }



        // Add the last edge of the road
        roadToReturn = addDepotToSwappedRoad(roadToReturn, solution, distance);

        return roadToReturn;

    }

    public static Solution runCrossExchange(Solution solutionInit, int roadSelected1, int roadSelected2, int indexEdge1, int indexEdge2)
    {
        // Road verification
        if (roadSelected1 == roadSelected2)
            return null;

        // Get roads
        ArrayList<Road> roads = (ArrayList<Road>) solutionInit.getRoads().clone();
        Road road1 = roads.get(roadSelected1);
        Road road2 = roads.get(roadSelected2);

        //Destinations
        Destination departClientRoad1 = road1.getDestinations().get(indexEdge1);
        Destination arriveClientRoad1 = road1.getDestinations().get(indexEdge1+1);
        Destination departClientRoad2 = road2.getDestinations().get(indexEdge2);
        Destination arriveClientRoad2 = road2.getDestinations().get(indexEdge2+1);

        // Exchange
        Road road1Generated = exchangeEdgeBetweenTwoRoads(solutionInit,  roadSelected1, road2,indexEdge1,indexEdge2, departClientRoad1, arriveClientRoad2);
        Road road2Generated = exchangeEdgeBetweenTwoRoads(solutionInit,  roadSelected2, road1,indexEdge2, indexEdge1,departClientRoad2, arriveClientRoad1);

        if (road1Generated == null)
           return null;

        if (road2Generated == null)
            return null;

        // Create the candidate solution and set road
        Solution candidateSolution = solutionInit.clone();
        candidateSolution.getRoads().set(roadSelected1, road1Generated);
        candidateSolution.getRoads().set(roadSelected2, road2Generated);

        candidateSolution.setTotalDistanceCovered();

        return candidateSolution;




    }


    /**
     * Generate all neighbors of a solution and display it
     * @param solution Solution to generate neighbors
     */
    public static void generateAllNeighbors2Opt(Solution solution)
    {
        int iter = 1;
        for (int selectedRoad = 0; selectedRoad < solution.getRoads().size(); selectedRoad++)
        {

            for (int i =0 ; i < solution.getRoads().get(selectedRoad).getEdges().size()-1; i++)
            {
                for (int j = i+2; j < solution.getRoads().get(selectedRoad).getEdges().size() ; j++)
                {
                    System.out.println("Iteration : "+iter);
                    Solution solutionNeighbour = TwoOptAndCrossExchange.runTwoOptIntra(solution, selectedRoad, i, j);
                    if (solutionNeighbour != null && solutionNeighbour.getRoads().get(selectedRoad)!=null)
                    {
                        System.out.println("Road selected : "+selectedRoad);
                        System.out.println("Echange entre les aretes : "+solution.getRoads().get(selectedRoad).getEdges().get(i)+
                                " et : "+solution.getRoads().get(selectedRoad).getEdges().get(j));
                        //solutionNeighbour.displaySolution();
                        solutionNeighbour.getRoads().get(selectedRoad).toString();
                        SolutionVisualization.DisplayGraph(solution.getConfig(), solutionNeighbour);
                    }
                    iter++;

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
                for (int i =0 ; i < solution.getRoads().get(selectedRoad).getEdges().size(); i++)
                {
                    for (int j =0; j < solution.getRoads().get(selectedRoad2).getEdges().size(); j++)
                    {
                        //Verified condition
                        if (selectedRoad == selectedRoad2 || (i == 0&&j==0))
                            continue;
                        if (i==0 && j==solution.getRoads().get(selectedRoad2).getEdges().size()-1)
                            continue;
                        if (j==0 && i==solution.getRoads().get(selectedRoad).getEdges().size()-1)
                            continue;
                        Solution solutionNeighbour = runCrossExchange(solution, selectedRoad, selectedRoad2, i, j);
                        if (solutionNeighbour != null)
                        {
                            System.out.println("Roads selected : "+selectedRoad+" et "+selectedRoad2);
                            System.out.println("Edges selected : "+i+" et "+j);
                            System.out.println("Echange entre les aretes : "+solution.getRoads().get(selectedRoad).getEdges().get(i)+
                                    " et : "+solution.getRoads().get(selectedRoad2).getEdges().get(j));
                            //solutionNeighbour.displaySolution();
                            solutionNeighbour.getRoads().get(selectedRoad).toString();
                            System.out.println("----------------------------------------------------");
                            solutionNeighbour.getRoads().get(selectedRoad2).toString();
                            System.out.println("Distance totale parcourue : "+solutionNeighbour.getTotalDistanceCovered());
                            SolutionVisualization.DisplayGraph(solution.getConfig(), solutionNeighbour);
                            System.out.println("FIIIIN");

                        }
                    }
                }
            }
        }
        System.out.println("Fin du programme cross-exchange");
    }




}