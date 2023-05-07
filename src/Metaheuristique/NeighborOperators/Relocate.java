package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Relocate {

    static Transformation transformation;


    // Relocate intra route
    public static Result RelocateIntra(Solution solution, int roadSelected, int newIndexClient, int indexClient) {
        Result result;
        transformation = new Transformation(indexClient, newIndexClient, roadSelected, roadSelected);
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
                //System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        if(isRoadPossible.contains(false))
        {
            //System.out.println("trajet impossible");
            return null;
        }
        else
        {
            //System.out.println("Toutes les conditions sont respectees");
            //SolutionVisualization.DisplayGraph(candidate);
            result = new Result(candidate, transformation);
            return result;
        }
    }

    // Relocate inter routes
    public static Result RelocateInter(Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
    {
        Result result;
        transformation = new Transformation(indexClient, newIndexClient, firstClientRoad, secondClientRoad);
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
                //System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        for (int i = 0; i < sizeSecond-1; i++) {
            int time = newFirstRoad.getTimeByIndex(i);
            if (SolutionUtils.isClientCanBeDelivered(newSecondRoad.getEdges().get(i).getDepartClient(), newSecondRoad.getEdges().get(i).getArriveClient(), time, solution.getConfig().getTruck().getCapacity() ) == false) {
                //System.out.println("conditions non respectees");
                isRoadPossible.add(false);
            }
        }
        if(isRoadPossible.contains(false))
        {
            //System.out.println("trajet impossible");
            return null;
        }
        else
        {
            //System.out.println("Toutes les conditions sont respectees");
            result = new Result(candidate, transformation);
            return result;
        }
    }

}
