package Metaheuristique;

import java.util.ArrayList;
import Logistique.Client;
import Logistique.Destination;

public class NeighboorOperation {
    public static Solution TwoOpsInter(Solution solution, int roadSelected, int edge1, int edge2)
    {

        ArrayList<Road> roads = solution.getRoads();
        Road oneRoad = roads.get(roadSelected);

        if (edge1>oneRoad.getDestinations().size() || edge2>oneRoad.getDestinations().size() || edge1<0 || edge2<0 || Math.abs(edge1-edge2)==1)
        {
            System.out.println("Error: edge1 or edge2 is greater than the number of clients");
            return solution;
        }
        Client clientFromEdge1 = (Client) oneRoad.getDestinations().get(edge1);
        Client clientFromEdge2 = (Client) oneRoad.getDestinations().get(edge2);
        ArrayList<Solution> neighbors = new ArrayList<Solution>();


        ArrayList<Destination> clients = oneRoad.getDestinations();
        Client firstClient = (Client) clients.get(edge1);
        Client secondClient = (Client) clients.get(edge2);
        for (int i = 0; i < clients.size(); i++) {
            for (int j = 0; j<clients.size();j++)
            {

            }
        }

        return solution;
    }

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
