package Metaheuristique;

import java.util.ArrayList;
import java.util.List;

import Logistique.Client;
import Logistique.Destination;

import Utils.SolutionUtils;

public class NeighboorOperation {











    public Solution Relocate(Solution solution, int roadSelected, int indexClient)
    {
        int bestDistance = solution.getTotalDistanceCovered();
        ArrayList<Road> roads = solution.getRoads();
        Destination clientDestinsation = roads.get(roadSelected).getDestinations().get(indexClient);
        roads.get(roadSelected).getDestinations().remove(indexClient);
        Solution candidate = solution;
        Solution bestCandidate = solution;
        for( int i = 0; i< solution.nbClients; i ++)
        {
            if(indexClient != i)
            {
                roads.get(roadSelected).getDestinations().add(i, clientDestinsation);
                candidate.setRoads(roads);
                int candidateDistance = candidate.getTotalDistanceCovered();
                if(candidateDistance < bestDistance)
                {
                    bestCandidate = candidate;
                    bestDistance = candidateDistance;
                }
            }
        }
        return bestCandidate;
    }

    public Solution Exchange(Solution solution, int roadSelected, int firstClient, int secondClient)
    {
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
}
