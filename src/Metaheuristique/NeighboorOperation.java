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
        Road initialRoad = solution.getRoads().get(roadSelected).clone();
        // on récupère la route du trajet concerné
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        // On recupère la destination du client
        Destination arriveClient = newRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newRoad.removeDestinationToRoad(indexClient);
        // on crée un candidat
        Solution candidate = solution.clone();
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        // on ajoute le client à sa nouvelle position
        newRoad = newRoad.addDestinationsAndUpdateEdgeToRoad(solution, arriveClient, newIndexClient);
        // On vérifie si les conditions sont respectées
        // on vérifie pour chacune des destinations de la route si le nouveau trajet est possible
        candidate.getRoads().set(roadSelected, newRoad);
        int size = candidate.getARoad(roadSelected).getEdges().size();
        for (int i = 0; i < size-1; i++) {
            int time = newRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newRoad.getEdges().get(i).getDepartClient(), newRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
                System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        if(isRoadPossible.contains(false))
        {
            System.out.println("trajet impossible");
            return null;
        }
        else
        {
            System.out.println("Toutes les conditions sont respectees");
            SolutionVisualization.DisplayGraph(solution.getConfig(), candidate);
            return candidate;
        }
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
    public static Solution Exchange(Solution solution, int roadSelected, int firstClient, int secondClient)
    {
        Road initialRoad = solution.getRoads().get(roadSelected);
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(secondClient);
        ArrayList<Destination> newDestinations = new ArrayList<Destination>();
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        for(int i = 0; i < solution.getRoads().get(roadSelected).getDestinations().size(); i++)
        {
            if(i == firstClient)
            {
                newDestinations.add(secondClientDestinsation);
            }
            else if(i == secondClient)
            {
                newDestinations.add(firstClientDestinsation);
            }
            else
            {
                newDestinations.add(solution.getRoads().get(roadSelected).getDestinations().get(i));
            }
        }
        newRoad.setDestinations(newDestinations);
        int size = candidate.getARoad(roadSelected).getEdges().size();
        for (int i = 0; i < size-1; i++) {
            int time = newRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newRoad.getEdges().get(i).getDepartClient(), newRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
                System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        if(isRoadPossible.contains(false))
        {
            System.out.println("trajet impossible");
            return null;
        }
        else
        {
            System.out.println("Toutes les conditions sont respectees");
            SolutionVisualization.DisplayGraph(solution.getConfig(), candidate);
            return candidate;
        }
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
