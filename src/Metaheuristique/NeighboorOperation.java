package Metaheuristique;


import java.util.ArrayList;
import java.util.List;

import Graphics.SolutionVisualization;
import Logistique.Client;
import Logistique.Destination;

import Utils.SolutionUtils;
public class NeighboorOperation {


    // Relocate intra route
    public static Solution RelocateIntra(Solution solution, int roadSelected, int newIndexClient, int indexClient) {
        Solution voisins = new Solution();
        Road road = solution.getRoads().get(roadSelected).clone();
        Destination startClient = road.getDestinations().get(indexClient-1);
        Destination arriveClient = road.getDestinations().get(indexClient);
        road.removeDestinationToRoad(indexClient);
        Solution candidate = solution.clone();
        boolean isRoadNotPossible = false;
        road.getDestinations().add(newIndexClient, arriveClient);
        for (int i = 0; i < solution.getARoad(roadSelected).getDestinations().size() ; i++) {
            int time = road.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(road.getDestinations().get(i), road.getDestinations().get(i+1), time, road.getEdges().get(i).getQuantityDelivered()) == false) {
                System.out.println("conditions non respectees");
                isRoadNotPossible = true;
                break;
            }
        }
        if(!isRoadNotPossible)
        {
            candidate.getRoads().set(roadSelected, road);
            SolutionVisualization.DisplayGraph(solution.getConfig(), candidate);
        }
        return candidate;
    }

    // Relocate inter routes
    public static ArrayList<Solution> RelocateInter(Solution solution, int clientRoad, int indexClient)
    {
        ArrayList<Solution> voisins = new ArrayList<Solution>();
        ArrayList<Road> roads = solution.getRoads();
        Destination clientDestinsation = roads.get(clientRoad).getDestinations().get(indexClient);
        roads.get(clientRoad).getDestinations().remove(indexClient);
        Solution candidate = solution;
        for (int j = 0; j < roads.size(); j++) {
            for (int i = 0; i < solution.nbClients; i++) {
                roads.get(j).getDestinations().add(i, clientDestinsation);
                candidate.setRoads(roads);
                voisins.add(candidate);
            }
        }
        return voisins;
    }

    // Exchange intra route
    public static Solution Exchange(Solution solution, int roadSelected, int firstClient, int secondClient) {
        ArrayList<Road> roads = solution.getRoads();
        Solution candidate = solution;
        Destination firstClientDestinsation = roads.get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = roads.get(roadSelected).getDestinations().get(secondClient);
        roads.get(roadSelected).getDestinations().remove(firstClient);
        roads.get(roadSelected).getDestinations().add(firstClient, secondClientDestinsation);
        roads.get(roadSelected).getDestinations().remove(firstClient);
        roads.get(roadSelected).getDestinations().add(secondClient, firstClientDestinsation);
        candidate.setRoads(roads);
        return candidate;
    }

    // Exchange inter route
    public static Solution ExchangeInter(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient, int secondClient) {
            ArrayList<Road> roads = solution.getRoads();
            Solution candidate = solution;
            Destination firstClientDestinsation = roads.get(firstClientRoad).getDestinations().get(firstClient);
            Destination secondClientDestinsation = roads.get(secondClientRoad).getDestinations().get(secondClient);
            roads.get(firstClientRoad).getDestinations().remove(firstClient);
            roads.get(firstClientRoad).getDestinations().add(firstClient, secondClientDestinsation);
            roads.get(secondClientRoad).getDestinations().remove(secondClient);
            roads.get(secondClientRoad).getDestinations().add(secondClient, firstClientDestinsation);
            candidate.setRoads(roads);
            return candidate;
    }
}
