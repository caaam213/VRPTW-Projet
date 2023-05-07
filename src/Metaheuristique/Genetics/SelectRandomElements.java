package Metaheuristique.Genetics;

import Metaheuristique.Solution;

import java.util.Random;

public class SelectRandomElements {
    // TODO : Supprimer ?


    /**
     * @param solution : Solution to get roads
     * @return random roads indexes
     */
    public static int[] selectRandomRoads(Solution solution) {
        int[] roads = new int[2];
        Random random = new Random();
        int randomRoad1 = random.nextInt(solution.getRoads().size());
        int randomRoad2 = random.nextInt(solution.getRoads().size());
        while (randomRoad1 == randomRoad2) {
            randomRoad2 = random.nextInt(solution.getRoads().size());
        }
        roads[0] = randomRoad1;
        roads[1] = randomRoad2;
        return roads;
    }

    /**
     *
     * @param solution : Solution to get roads
     * @param road1 : Index of road to get clients
     * @return random clients indexes
     */
    public static int[] selectRandomClientsForSameRoad(Solution solution, int road1) {
        int[] clients = new int[2];
        Random random = new Random();

        int randomClient1 = random.nextInt(solution.getRoads().get(road1).getDestinations().size());
        int randomClient2 = random.nextInt(solution.getRoads().get(road1).getDestinations().size());

        while (randomClient1 == randomClient2) {
            randomClient2 = random.nextInt(solution.getRoads().get(road1).getDestinations().size());
        }

        clients[0] = randomClient1;
        clients[1] = randomClient2;
        return clients;
    }

    /**
     * Verify if the edge is valid
     * @param solution : Solution to get roads
     * @param roadSelected1 : Index of road to get clients
     * @param roadSelected2 : Index of road to get clients
     * @param edge1 : Index of edge to get clients
     * @param edge2 : Index of edge to get clients
     * @return true if the edge is valid, false otherwise
     */
    private static boolean verifyIfEdgeOk(Solution solution, int roadSelected1, int roadSelected2, int edge1, int edge2)
    {
        if(edge1 == 0 && edge2 == solution.getRoads().get(roadSelected2).getEdges().size()-1)
        {
            return false;
        }
        if(edge2 == 0 && edge1 == solution.getRoads().get(roadSelected1).getEdges().size()-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Select random edges for different roads according to the method
     * @param solution : Solution to get roads
     * @param roadSelected1 : Index of road to get clients
     * @param roadSelected2 : Index of road to get clients
     * @param method : Method to use
     * @return random edges indexes
     */
    public static int[] selectedRandomEdges(Solution solution, int roadSelected1, int roadSelected2, String method) {
        int edges[] = new int[2];
        Random random = new Random();
        int randomEdge1;
        int randomEdge2;
        if(method.equals("CrossExchange"))
        {
            randomEdge1 =  random.nextInt(solution.getRoads().get(roadSelected1).getEdges().size());
            randomEdge2 = random.nextInt(solution.getRoads().get(roadSelected2).getEdges().size());
            while (!verifyIfEdgeOk(solution, roadSelected1, roadSelected2, randomEdge1, randomEdge2))
            {
                randomEdge1 =  random.nextInt(solution.getRoads().get(roadSelected1).getEdges().size());
                randomEdge2 = random.nextInt(solution.getRoads().get(roadSelected2).getEdges().size());
            }
        }
        else
        {
            randomEdge1 =  random.nextInt(solution.getRoads().get(roadSelected1).getEdges().size()-1);
            randomEdge2 = random.nextInt(solution.getRoads().get(roadSelected2).getEdges().size());
            while (randomEdge1 == randomEdge2) {
                randomEdge2 = random.nextInt(solution.getRoads().get(roadSelected2).getEdges().size());
            }
        }

        edges[0] = randomEdge1;
        edges[1] = randomEdge2;
        return edges;


    }




}
