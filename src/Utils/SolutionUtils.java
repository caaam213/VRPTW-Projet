package Utils;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;

import java.util.ArrayList;

public class SolutionUtils {

    /**
     * Returns a random client from the list of clients
     * @param clients : list of clients
     * @return a random client
     */
    private static Client getRandomClient(ArrayList<Client> clients)
    {
        int randomIndex = (int) (Math.random() * clients.size());
        return clients.get(randomIndex);
    }

    /**
     * Returns the square of a number
     * @param a : a number
     * @return the square of the number
     */
    static private double sqr(double a) {
        return a*a;
    }

    /**
     * Returns the distance between two destinations
     * @param start : the start destination
     * @param arrive : the arrive destination
     * @return the distance between the two destinations
     */
    private static int distanceBetweenTwoDestination(Destination start, Destination arrive)
    {
        int x1 = start.getLocalisation().getX();
        int y1 = start.getLocalisation().getY();
        int x2 = arrive.getLocalisation().getX();
        int y2 = arrive.getLocalisation().getY();
        return (int)Math.sqrt(sqr(y2 - y1) + sqr(x2 - x1));
    }


    /**
     * Verify if the client can be delivered
     * @param startClient : the start client
     * @param arriveClient : the arrive client
     * @param time : the time
     * @param capacity : the capacity
     * @return true if the client can be delivered, false otherwise
     */
    private static boolean isClientCanBeDelivered(Destination startClient, Client arriveClient, int time, int capacity)
    {
        // Verify if the client can be delivered in time
        // time + distance from startClient to arriveClient + time to deliver the client has to be less than the due time of the client
        if (time + distanceBetweenTwoDestination(startClient, arriveClient)+ arriveClient.getDeliveryTime() > arriveClient.getDueTime())
        {
            return false;
        }

        // Verify if the capacity left is enough to deliver the client
        if (arriveClient.getDemand() > capacity)
        {
            return false;
        }

        return true;


    }

    /**
     * Returns the client that can be delivered the most thoroughly
     * @param clientsNotDeliveredToManageDueTime : the list of clients not delivered
     * @param startClient : the start client
     * @param time : the time
     * @param capacity : the capacity
     * @return the client that can be delivered
     */
    private static Client selectClientMoreThoroughly(ArrayList<Client>clientsNotDeliveredToManageDueTime, Destination startClient,Client client, int time, int capacity)
    {

        while (clientsNotDeliveredToManageDueTime.size()>0)
        {
            if (isClientCanBeDelivered( startClient,client,  time,  capacity))
            {
                break;
            }
            else
            {
                clientsNotDeliveredToManageDueTime.remove(client);
                if (clientsNotDeliveredToManageDueTime.size() == 0)
                {
                    break;
                }
                client = getRandomClient(clientsNotDeliveredToManageDueTime);
            }
        }
        return client;
    }

    /**
     * Generate a random solution
     * @param conf : the configuration
     * @param generateSmarterSolution : if we want to generate a smarter solution
     * @return the solution generated
     */
    public static Solution generateRandomSolution(Configuration conf, boolean generateSmarterSolution) {
        // Initialization
        ArrayList<Client> clientsNotDelivered = (ArrayList<Client>)conf.getListClients().clone();
        Solution solution = new Solution();
        int time = 0;
        int distance = 0;
        int totalDistance = 0;
        int capacity = conf.getTruck().getCapacity();
        Road road = new Road();

        road.addDestinationsToRoad(conf.getCentralDepot()); //Add depot to the road

        while (clientsNotDelivered.size() > 0) {
            Client client = getRandomClient(clientsNotDelivered);

            // If we want to generate a smarter solution
            if (generateSmarterSolution)
            {
                ArrayList<Client> clientsNotDeliveredToManageDueTime = (ArrayList<Client>)clientsNotDelivered.clone();
                client = selectClientMoreThoroughly(clientsNotDeliveredToManageDueTime,
                        road.getDestinations().get(road.getDestinations().size()-1),
                        client,
                        time,
                        capacity);
            }


            // If the client can be delivered
            if (isClientCanBeDelivered(road.getDestinations().get(road.getDestinations().size()-1), client, time, capacity)) {
                // If time is before the ready time, we wait until the ready time
                if(time<client.getReadyTime())
                    time = client.getReadyTime();

                time += client.getService()+distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
                distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
                capacity -= client.getDemand();

                road.addDestinationsToRoad(client);
                clientsNotDelivered.remove(client);

            } else {
                // If the client can't be delivered, we add the depot to the road and we create a new road
                distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), conf.getCentralDepot());

                totalDistance += distance; // Add distance from the last client to the depot
                road.addDestinationsToRoad(conf.getCentralDepot()); //Add return to the depot
                road.setDistance(distance); // Set the distance of the road
                solution.addRoads(road); // Add the road to the solution

                road = new Road();
                road.addDestinationsToRoad(conf.getCentralDepot());
                time = 0;
                distance = 0;
                capacity = conf.getTruck().getCapacity();


            }
        }
        if (road.getDestinations().size() > 1)
        {
            distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), conf.getCentralDepot()); // Add distance from the last client to the depot
            totalDistance += distance; // Add distance from the last client to the depot
            road.addDestinationsToRoad(conf.getCentralDepot()); //Add return to the depot
            road.setDistance(distance);
            solution.addRoads(road);
        }

        solution.setConfig(conf); // Set the configuration of the solution
        solution.setTotalDistanceCovered(totalDistance); // Set the total distance covered by the solution
        return solution;
    }

}
