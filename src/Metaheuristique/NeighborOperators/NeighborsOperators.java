package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;

public abstract class NeighborsOperators {

    public static Solution verifyIfRoadValid(Solution candidate, int roadSelected)
    {
        int size = candidate.getARoad(roadSelected).getDestinations().size();
        Road newRoad = candidate.getRoads().get(roadSelected);

        int time = 0;
        int capacityRemained = candidate.getConfig().getTruck().getCapacity();
        int infos[];

        for (int i = 0; i < size-1; i++) {
            // time, distance, capacity, distanceBetweenTwoDestinations
            Destination departedClient = newRoad.getDestinations().get(i);
            Destination arrivedClient = newRoad.getDestinations().get(i+1);

            int dis = distanceBetweenTwoDestination(departedClient, arrivedClient);
            if (SolutionUtils.isClientCanBeDelivered(departedClient, arrivedClient, time, capacityRemained) == false) {
                /*System.out.println("Client : " + departedClient.getIdName());
                System.out.println("Client : " + arrivedClient.getIdName());
                System.out.println("Temps actuel : " + time);
                System.out.println("Distance : " + dis);
                System.out.println("DueTime : " + arrivedClient.getDueTime());
                System.out.println("conditions non respectees");*/
                return null;
            }
            else {
                infos = SolutionUtils.calculateInfos(newRoad, arrivedClient,time, dis, capacityRemained);
                time = infos[0];
                capacityRemained = infos[2];

            }
        }
        candidate.getRoads().set(roadSelected, newRoad);
        return candidate;

    }
}
