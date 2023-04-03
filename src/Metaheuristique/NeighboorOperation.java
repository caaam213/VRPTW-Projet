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
        newRoad = newRoad.addDestinationsAndUpdateEdgeToRoad(arriveClient, newIndexClient);
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
            return candidate;
        }
    }

    // Relocate inter routes
    public static Solution RelocateInter(Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
    {
        // on récupère la route du trajet concerné
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        // On recupère la destination du client
        Destination arriveFirstClient = newFirstRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newFirstRoad.removeDestinationToRoad(indexClient);
        // on crée un candidat
        Solution candidate = solution.clone();
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        // on ajoute le client à sa nouvelle position
        newSecondRoad = newSecondRoad.addDestinationsAndUpdateEdgeToRoad(arriveFirstClient, newIndexClient);
        // On vérifie si les conditions sont respectées
        // on vérifie pour chacune des destinations de la route si le nouveau trajet est possible
        candidate.getRoads().set(firstClientRoad, newFirstRoad);
        candidate.getRoads().set(secondClientRoad, newSecondRoad);
        int sizeFirst = candidate.getARoad(firstClientRoad).getEdges().size();
        int sizeSecond = candidate.getARoad(secondClientRoad).getEdges().size();
        for (int i = 0; i < sizeFirst-1; i++) {
            int time = newFirstRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newFirstRoad.getEdges().get(i).getDepartClient(), newFirstRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
                System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        for (int i = 0; i < sizeSecond-1; i++) {
            int time = newFirstRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newSecondRoad.getEdges().get(i).getDepartClient(), newSecondRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
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
            return candidate;
        }
    }

    // Exchange intra route
    public static Solution Exchange(Solution solution, int roadSelected, int firstClient, int secondClient)
    {
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(secondClient);
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        newRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newRoad.getDestinations().set(secondClient, firstClientDestinsation);
        newRoad = newRoad.constrcutEdgeToRoad();
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
            candidate.getRoads().set(roadSelected, newRoad);
            return candidate;
        }
    }

    // Exchange inter route
    public static Solution ExchangeInter(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient, int secondClient) {
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(firstClientRoad).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(secondClientRoad).getDestinations().get(secondClient);
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        newFirstRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newSecondRoad.getDestinations().set(secondClient, firstClientDestinsation);
        newFirstRoad = newFirstRoad.constrcutEdgeToRoad();
        newSecondRoad = newSecondRoad.constrcutEdgeToRoad();
        int sizeFirst = candidate.getARoad(firstClientRoad).getEdges().size();
        int sizeSecond = candidate.getARoad(secondClientRoad).getEdges().size();
        for (int i = 0; i < sizeFirst-1; i++) {
            int time = newFirstRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newFirstRoad.getEdges().get(i).getDepartClient(), newFirstRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
                System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        for (int i = 0; i < sizeSecond-1; i++) {
            int time = newFirstRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newSecondRoad.getEdges().get(i).getDepartClient(), newSecondRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
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
            candidate.getRoads().set(firstClientRoad, newFirstRoad);
            candidate.getRoads().set(secondClientRoad, newSecondRoad);
            return candidate;
        }
    }
}
