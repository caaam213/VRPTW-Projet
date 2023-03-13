package Utils;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;

import java.util.ArrayList;

public class SolutionUtils {

    private static Client getRandomClient(ArrayList<Client> clients)
    {
        int randomIndex = (int) (Math.random() * clients.size());
        return clients.get(randomIndex);
    }

    private static int distanceBetweenTwoDestination(Destination start, Destination arrive)
    {
        return (int) Math.sqrt(Math.pow(start.getLocalisation().getX() - arrive.getLocalisation().getX(), 2) + Math.pow(start.getLocalisation().getY() - arrive.getLocalisation().getY(), 2));
    }

    private static boolean isClientCanBeDelivered(Configuration conf, Client client, int time)
    {
        if (time < client.getReadyTime())
        {
            return false;
        }

        if (time + client.getDeliveryTime() > client.getDueTime())
        {
            return false;
        }

        if (client.getDemand() >= conf.getTruck().getCapacity())
        {
            return false;
        }
        return true;


    }

    public static Solution generateRandomSolution(Configuration conf) {
        ArrayList<Client> clientsNotDelivered = (ArrayList<Client>)conf.getListClients().clone();
        Solution solution = new Solution();
        int time = 0;
        int distance = 0;
        int totalDistance = 0;
        Road road = new Road();
        road.addDestinationsToRoad(conf.getCentralDepot()); //Add depot to the road
        while (clientsNotDelivered.size() > 0) {
            Client client = getRandomClient(clientsNotDelivered);
            // If the client can be delivered
            if (isClientCanBeDelivered(conf, client, time)) {
                time += client.getService()+distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
                distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
                road.addDestinationsToRoad(client);
                clientsNotDelivered.remove(client);
            } else {
                // If the client can't be delivered, we add the depot to the road and we create a new road
                if (road.getDestinations().size() > 1)
                {
                    road.addDestinationsToRoad(conf.getCentralDepot()); //Add return to the depot
                    distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), conf.getCentralDepot()); // Add distance from the last client to the depot
                    totalDistance += distance;
                    road.setDistance(distance);
                    solution.addRoads(road);
                    road = new Road();
                    road.addDestinationsToRoad(conf.getCentralDepot());
                    time = 0;
                    distance = 0;
                }
                else
                {
                    time+=1;
                }

            }
        }
        solution.setConfig(conf);
        solution.setTotalDistanceCovered(totalDistance);
        return solution;
    }
}
