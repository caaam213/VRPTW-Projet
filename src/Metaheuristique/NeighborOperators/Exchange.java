package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Exchange {

    static Transformation transformation;

    public static Result Exchange(Solution solution, int roadSelected, int firstClient, int secondClient)
    {
        Result result;
        transformation = new Transformation(firstClient, secondClient,roadSelected, roadSelected );
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        Solution candidate = solution.clone();
        // récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(secondClient);
        ArrayList<Boolean> isRoadPossible = new ArrayList<Boolean>();
        newRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newRoad.getDestinations().set(secondClient, firstClientDestinsation);
        newRoad = newRoad.constrcutEdgeToRoad();
        newRoad.setTime(0);
        newRoad.setDistance(0);
        int size = candidate.getARoad(roadSelected).getEdges().size();
        for (int i = 0; i < size-1; i++) {
            int infos[] = new int[4];
            infos = SolutionUtils.calculateInfos(newRoad, newRoad.getEdges().get(i).getArriveClient(), newRoad.getTime(), newRoad.getDistance(), solution.getConfig().getTruck().getCapacity(), 0);
            if (SolutionUtils.isClientCanBeDelivered(newRoad.getEdges().get(i).getDepartClient(), newRoad.getEdges().get(i).getArriveClient(), infos[0], infos[2] ) == false) {
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
            candidate.getRoads().set(roadSelected, newRoad);
            result = new Result(candidate, transformation);
            return result;
        }
    }

    // Exchange inter route
    public static Result ExchangeInter(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient, int secondClient)
    {
        Result result;
        transformation = new Transformation(firstClient, secondClient,firstClientRoad, secondClientRoad );
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
            candidate.getRoads().set(firstClientRoad, newFirstRoad);
            candidate.getRoads().set(secondClientRoad, newSecondRoad);
            result = new Result(candidate, transformation);
            return result;
        }
    }
}
