package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;


public class Relocate {

    static Transformation transformation;


    // Relocate intra route
    public static Result RelocateIntra(Solution solution, int roadSelected, int newIndexClient, int indexClient) {
        if(indexClient == newIndexClient)
        {
            System.out.println("indexClient == newIndexClient");
            return null;
        }
        System.out.println("indexClient = " + indexClient + " newIndexClient = " + newIndexClient);
        System.out.println("BAASE");

        for(int j=0; j < solution.getRoads().get(roadSelected).getDestinations().size(); j++)
        {
            System.out.println("Destinsation : " + solution.getRoads().get(roadSelected).getDestinations().get(j).getIdName());

        }
        System.out.println();

        // on récupère la route du trajet concerné
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        // On recupère la destination du client
        Destination arriveClient = newRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newRoad.getDestinations().remove(indexClient);
        newRoad.getDestinations().add(newIndexClient, arriveClient);


        for(int j=0; j < newRoad.getDestinations().size(); j++)
        {
            System.out.println("Destinsation : " + newRoad.getDestinations().get(j).getIdName());
        }
        // on crée un candidat
        Solution candidate = solution.clone();
        // on ajoute le client à sa nouvelle position
        //newRoad = newRoad.addDestinationsAndUpdateEdgeToRoad(arriveClient, newIndexClient);
        // On vérifie si les conditions sont respectées
        // on vérifie pour chacune des destinations de la route si le nouveau trajet est possible
        candidate.getRoads().set(roadSelected, newRoad);
        int size = candidate.getARoad(roadSelected).getDestinations().size();
        int infos[] = {0,0, solution.getConfig().getTruck().getCapacity(),0};
        newRoad.getEdges().clear();
        for (int i = 0; i < size-1; i++) {
            // time, distance, capacity, distanceBetweenTwoDestinations
            int chrono = infos[0];
            int capacityActul = infos[2];
            Destination departedClient = newRoad.getDestinations().get(i);
            Destination arrivedClient = newRoad.getDestinations().get(i+1);
            int dis = distanceBetweenTwoDestination(departedClient, arrivedClient);
            if (SolutionUtils.isClientCanBeDelivered(departedClient, arrivedClient, chrono, capacityActul) == false) {
                System.out.println("Client : " + departedClient.getIdName());
                System.out.println("Client : " + arrivedClient.getIdName());
                System.out.println("Temps actuel : " + chrono);
                System.out.println("Distance : " + dis);
                System.out.println("DueTime : " + arrivedClient.getDueTime());
                System.out.println("conditions non respectees");
                return null;
            }
            else {
                Edge edge = new Edge(departedClient, arrivedClient);
                // isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
                infos = SolutionUtils.calculateInfos(newRoad, arrivedClient,chrono, dis, capacityActul, 0);
                edge.setDistance(infos[2]);
                edge.setTime(chrono);
                edge.setQuantityDelivered(infos[3]);
                newRoad.getEdges().add(edge);
            }
        }
        System.out.println("Toutes les conditions sont respectees");
        Transformation transformation = new Transformation(roadSelected, roadSelected, newIndexClient, indexClient);
        Result res = new Result(candidate, transformation);
        return res;
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
