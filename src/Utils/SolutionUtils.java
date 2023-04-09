package Utils;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.Road;
import Metaheuristique.Solution;

import java.util.ArrayList;

public class SolutionUtils {

    /**
     * Returns a random client from the list of clients
     * @param clients : list of clients
     * @return a random client
     */
    public static Client getRandomClient(ArrayList<Client> clients)
    {
        int randomIndex = (int) (Math.random() * clients.size());
        return (Client) clients.get(randomIndex);
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
    public static int distanceBetweenTwoDestination(Destination start, Destination arrive)
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
    public static boolean isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
    {
        // Verify if the client can be delivered in time
        // time + distance from startClient to arriveClient + time to deliver the client has to be less than the due time of the client
        if(arriveClient == null)
        {
            return false;
        }


        if (arriveClient instanceof Client)
        {
            if (time + distanceBetweenTwoDestination(startClient, arriveClient)+ ((Client) arriveClient).getDeliveryTime() > arriveClient.getDueTime())
            {
                return false;
            }
            // Verify if the capacity left is enough to deliver the client
            if (((Client) arriveClient).getDemand() > capacity)
            {
                return false;
            }
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
    public static Client selectClientMoreThoroughly(ArrayList<Client> clientsNotDeliveredToManageDueTime, Destination startClient, Client client, int time, int capacity)
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
                    return null;
                }
                client = getRandomClient(clientsNotDeliveredToManageDueTime);
            }
        }
        return client;
    }

    /**
     * Calculate the time, distance, capacity and distance between two destinations
     * @param road : the road
     * @param client : the client
     * @param time : the time
     * @param distance : the distance
     * @param capacity : the capacity
     * @param posEdge : the position of the edge
     * @return an array with the time, distance, capacity and distance between two destinations
     */
    private static int[] calculateInfos(Road road, Destination client, int time, int distance, int capacity, int posEdge)
    {
        int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
        if(time+distanceBetweenTwoDestinations<client.getReadyTime())
            time = client.getReadyTime();
        else
            time += distanceBetweenTwoDestinations;

        if (client instanceof Client)
        {
            int timeBetweenTwoDestinations = ((Client) client).getService()+distanceBetweenTwoDestinations;

            distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);
            capacity -= ((Client) client).getDemand();
        }
        else
        {
            time = time + distanceBetweenTwoDestinations;
        }
        return new int[]{time, distance, capacity, distanceBetweenTwoDestinations};
    }

    /**
     * Calculate the road
     * @param client : the client
     * @param time : the time
     * @param distance : the distance
     * @param capacity : the capacity
     * @param road : the road
     * @param edge : the edge
     * @param posEdge : the position of the edge
     * @return the road
     */
    public static Road calculateRoad(Destination client, int time, int distance, int capacity, Road road, Edge edge, int posEdge)
    {
        int[] infos = calculateInfos(road, client, time, distance, capacity, posEdge);
        time = infos[0];
        distance = infos[1];
        capacity = infos[2];
        int distanceBetweenTwoDestinations = infos[3];

        edge.setArriveClient(client);
        edge.setDistance(distanceBetweenTwoDestinations);
        edge.setTime(time);
        if (client instanceof Client)
            edge.setQuantityDelivered(((Client) client).getDemand());
        edge.setPosEdge(posEdge);

        road.addDestinationsToRoad(client,edge);
        road.setTime(time);
        road.setDistance(distance);
        road.setCapacityDelivered(capacity);

        return road;
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
        int capacityRemained = conf.getTruck().getCapacity();
        Road road = new Road();
        int distanceBetweenTwoDestinations;


        Edge edge = new Edge(conf.getCentralDepot());
        road.addDestinationsToRoad(conf.getCentralDepot(),edge); //Add depot to the road
        int posEdge = 0;

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
                        capacityRemained);
            }


            // If the client can be delivered
            if (isClientCanBeDelivered(road.getDestinations().get(road.getDestinations().size()-1), client, time, capacityRemained)) {
                // If time is before the ready time, we wait until the ready time


                road = calculateRoad(client, time, distance, capacityRemained, road, edge, posEdge);
                posEdge++;
                edge = new Edge(client);
                clientsNotDelivered.remove(client);
                time = road.getTime();
                distance = road.getDistance();
                capacityRemained = road.getCapacityDelivered();

            } else {
                // If the client can't be delivered, we add the depot to the road and we create a new road
                int[] calculateTotalDistance = calculateDistanceBetweenTheLastClientAndDepot(road, conf, distance);
                distanceBetweenTwoDestinations = calculateTotalDistance[1];
                road = addInfoToRoad(conf, distanceBetweenTwoDestinations,road,edge,time+distanceBetweenTwoDestinations, posEdge);

                solution.addRoads(road); // Add the road to the solution

                road = new Road();
                edge = new Edge(conf.getCentralDepot());

                road.addDestinationsToRoad(conf.getCentralDepot(), edge);
                time = 0;

                distance = 0;
                capacityRemained = conf.getTruck().getCapacity();
                posEdge = 0;


            }
        }
        if (road.getDestinations().size() > 1)
        {
            int[] calculateTotalDistance = calculateDistanceBetweenTheLastClientAndDepot(road, conf, distance);
            distanceBetweenTwoDestinations = calculateTotalDistance[1];
            road = addInfoToRoad(conf, distanceBetweenTwoDestinations,road,edge,time+distanceBetweenTwoDestinations, posEdge);

            solution.addRoads(road); // Add the road to the solution
        }
        solution.setTotalDistanceCovered(); // Set the total distance covered by the solution
        solution.setConfig(conf); // Set the configuration of the solution

        return solution;
    }

    /**
     * Calculate the distance from a client to the depot
     * @param road : the road
     * @param conf : the configuration
     * @param distance : the distance
     * @return an array with the distance and the distance between two destinations
     */
    public static int[] calculateDistanceBetweenTheLastClientAndDepot(Road road, Configuration conf, int distance)
    {
        int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(
                road.getDestinations().get(road.getDestinations().size()-1),
                conf.getCentralDepot());
        distance += distanceBetweenTwoDestinations;

        int[] returnTab = new int[2];
        returnTab[0] = distance;
        returnTab[1] = distanceBetweenTwoDestinations;

        return returnTab;
    }

    /**
     * Add info to the road (time, distance, depot) and return the road
     * @param conf : the configuration
     * @param distanceBetweenTwoDestinations : the distance between two destinations
     * @param road : the road
     * @param edge : the edge
     * @param time : the time
     * @param posEdge : the position of the edge
     * @return the road
     */
    public static Road addInfoToRoad(Configuration conf, int distanceBetweenTwoDestinations,  Road road, Edge edge,int time,  int posEdge) {
        edge.setArriveClient(conf.getCentralDepot());
        edge.setTime(time);
        edge.setPosEdge(posEdge);
        road.addDestinationsToRoad(conf.getCentralDepot(),edge); //Add return to the depot
        road.setTime(edge.getTime());
        road.setDistance(road.getDistance());

        return road;
    }

}
