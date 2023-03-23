package Metaheuristique;

import Logistique.Client;
import Logistique.Destination;

import java.util.ArrayList;

public class NeighboorOperation {
    public static Solution TwoOpsInter(Solution solution, int roadSelected, int edge1, int edge2) {

        ArrayList<Road> roads = solution.getRoads();
        Road oneRoad = roads.get(roadSelected);

        if (edge1 > oneRoad.getDestinations().size() || edge2 > oneRoad.getDestinations().size() || edge1 < 0 || edge2 < 0 || Math.abs(edge1 - edge2) == 1) {
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
            for (int j = 0; j < clients.size(); j++) {

            }
        }

        return solution;
    }

    // Relocate intra route
    public ArrayList<Solution> RelocateIntra(Solution solution, int roadSelected, int indexClient) {
        ArrayList<Solution> voisins = new ArrayList<Solution>();

        ArrayList<Road> roads = solution.getRoads();
        Destination clientDestinsation = roads.get(roadSelected).getDestinations().get(indexClient);
        roads.get(roadSelected).getDestinations().remove(indexClient);
        Solution candidate = solution;
        for (int i = 0; i < solution.nbClients; i++) {
            roads.get(roadSelected).getDestinations().add(i, clientDestinsation);
            candidate.setRoads(roads);
            voisins.add(candidate);
        }
        return voisins;
    }

    // Relocate inter routes
    public ArrayList<Solution> RelocateInter(Solution solution, int clientRoad, int indexClient)
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
    public Solution Exchange(Solution solution, int roadSelected, int firstClient, int secondClient) {
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
    public Solution ExchangeInter(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient, int secondClient) {
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
