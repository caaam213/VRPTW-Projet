package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;
import Utils.SolutionUtils;

import java.util.ArrayList;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;

public class Exchange {

    static Transformation transformation;

    public static Result Exchange(Solution solution, int roadSelected, int firstClient, int secondClient)
    {
        if (firstClient == secondClient)
        {
            return null;
        }

        if (firstClient == 0 || secondClient == 0)
        {
            return null;
        }

        if (firstClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1 || secondClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1)
        {
            return null;
        }

        Road newRoad = solution.getRoads().get(roadSelected).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(secondClient);
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        newRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newRoad.getDestinations().set(secondClient, firstClientDestinsation);
        //newRoad = newRoad.constrcutEdgeToRoad();
        int size = candidate.getARoad(roadSelected).getDestinations().size();
        int infos[] = {0,0, solution.getConfig().getTruck().getCapacity(),0};
        for (int i = 0; i < size-1; i++) {
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
                // isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
                infos = SolutionUtils.calculateInfos(newRoad, arrivedClient, chrono, dis, capacityActul);
            }
        }
        System.out.println("Toutes les conditions sont respectees");
            candidate.getRoads().set(roadSelected, newRoad);
            Transformation trans = new Transformation("",roadSelected, roadSelected, firstClient, secondClient);
            Result result = new Result(candidate, trans);
            return result;
    }

    // Exchange inter route
    public static Result ExchangeInter(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient, int secondClient)
    {
        if (firstClient == secondClient)
        {
            return null;
        }

        if (firstClient == 0 || secondClient == 0)
        {
            return null;
        }

        if (firstClient >= solution.getRoads().get(firstClientRoad).getDestinations().size()-1 || secondClient >= solution.getRoads().get(secondClientRoad).getDestinations().size()-1)
        {
            return null;
        }

        Result result;
        transformation = new Transformation("",firstClient, secondClient,firstClientRoad, secondClientRoad );
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(firstClientRoad).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(secondClientRoad).getDestinations().get(secondClient);
        newFirstRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newSecondRoad.getDestinations().set(secondClient, firstClientDestinsation);
        Road Tab[] = {newFirstRoad, newSecondRoad};
        for(int j = 0; j < 2; j++) {
            int infos[] = {0,0, solution.getConfig().getTruck().getCapacity(),0};
            int size = Tab[j].getDestinations().size();
            for (int i = 0; i < size - 1; i++) {
                int chrono = infos[0];
                int capacityActul = infos[2];
                Destination departedClient = Tab[j].getDestinations().get(i);
                Destination arrivedClient = Tab[j].getDestinations().get(i + 1);
                int dis = distanceBetweenTwoDestination(departedClient, arrivedClient);
                if (SolutionUtils.isClientCanBeDelivered(departedClient, arrivedClient, chrono, capacityActul) == false) {
                    System.out.println("Client : " + departedClient.getIdName());
                    System.out.println("Client : " + arrivedClient.getIdName());
                    System.out.println("Temps actuel : " + chrono);
                    System.out.println("Distance : " + dis);
                    System.out.println("DueTime : " + arrivedClient.getDueTime());
                    System.out.println("conditions non respectees");
                    return null;
                } else {
                    // isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
                    infos = SolutionUtils.calculateInfos(Tab[j], arrivedClient, chrono, dis, capacityActul);
                }
            }
        }

        //System.out.println("Toutes les conditions sont respectees");
        candidate.getRoads().set(firstClientRoad, newFirstRoad);
        candidate.getRoads().set(secondClientRoad, newSecondRoad);
        result = new Result(candidate, transformation);
        return result;
    }
}
