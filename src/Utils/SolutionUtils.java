package Utils;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Depot;
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
            int timeCalculated = time + distanceBetweenTwoDestination(startClient, arriveClient)+((Client) arriveClient).getService();
            if (timeCalculated > arriveClient.getDueTime())
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

    public static int calculateTime(Destination client, int time, int distanceBetweenTwoDestinations)
    {
        if(time+distanceBetweenTwoDestinations<client.getReadyTime())
            time = client.getReadyTime();
        else
            time += distanceBetweenTwoDestinations;

        if (client instanceof Client)
        {
            time+= ((Client) client).getService();
        }
        else
        {
            time = time + distanceBetweenTwoDestinations;
        }
        return time;
    }

    /**
     * Calculate the time, distance, capacity and distance between two destinations
     * @param road : the road
     * @param client : the client
     * @param time : the time
     * @param distance : the distance
     * @param capacity : the capacity
     * @return an array with the time, distance, capacity and distance between two destinations
     */
    public static int[] calculateInfos(Road road, Destination client, int time, int distance, int capacity)
    {
        int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);

        time = calculateTime(client,time,distanceBetweenTwoDestinations);
        // No need to verify if time+distanceBetweenTwoDestinations > client.getDueTime() because it is already done in the method isClientCanBeDelivered

        if (client instanceof Client)
        {
            distance += distanceBetweenTwoDestinations;
            capacity -= ((Client) client).getDemand();
        }

        return new int[]{time, distance, capacity, distanceBetweenTwoDestinations};
    }

    private static Destination getNearestClient(ArrayList<Client> clientsNotDeliveredToManageDueTime, Destination startClient, int time, int capacity)
    {
        Client nearestClient = getRandomClient(clientsNotDeliveredToManageDueTime);
        int distance = Integer.MAX_VALUE;
        for (Client client : clientsNotDeliveredToManageDueTime)
        {
            int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(startClient, client);
            if (distanceBetweenTwoDestinations < distance && isClientCanBeDelivered(startClient, client, time, capacity))
            {
                distance = distanceBetweenTwoDestinations;
                nearestClient = client;
            }
        }
        return nearestClient;
    }




    /**
     * Generate a random solution
     * @param conf : the configuration
     * @param generateVeryRandomSolution : if we want to generate a smarter solution
     * @return the solution generated
     */
    public static Solution generateRandomSolution(Configuration conf, boolean generateVeryRandomSolution) {
        // Initialization
        ArrayList<Client> clientsNotDelivered = (ArrayList<Client>)conf.getListClients().clone();
        Solution solution = new Solution();
        int time = 0;
        int distance = 0;
        int capacityRemained = conf.getTruck().getCapacity();
        Road road = new Road();
        int distanceBetweenTwoDestinations;

        road.getDestinations().add(conf.getCentralDepot()); //Add depot to the road


        Client client;
        while (clientsNotDelivered.size() > 0) {

            // If we want to generate a smarter solution
            if (generateVeryRandomSolution)
            {
                client = getRandomClient(clientsNotDelivered);
                ArrayList<Client> clientsNotDeliveredToManageDueTime = (ArrayList<Client>)clientsNotDelivered.clone();
                client = selectClientMoreThoroughly(clientsNotDeliveredToManageDueTime,
                        road.getDestinations().get(road.getDestinations().size()-1),
                        client,
                        time,
                        capacityRemained);
            }
            else
            {
                client = (Client) getNearestClient(clientsNotDelivered, road.getDestinations().get(road.getDestinations().size()-1), time, capacityRemained);
            }


            // If the client can be delivered
            if (isClientCanBeDelivered(road.getDestinations().get(road.getDestinations().size()-1), client, time, capacityRemained)) {
                // If time is before the ready time, we wait until the ready time


                int infos[] = calculateInfos(road, client, time, distance, capacityRemained);
                time = infos[0];
                distance = infos[1];
                capacityRemained = infos[2];
                road.getDestinations().add(client); // Add the client to the road
                clientsNotDelivered.remove(client);


            } else {
                // If the client can't be delivered, we add the depot to the road and we create a new road

                road.getDestinations().add(conf.getCentralDepot());
                solution.addRoads(road); // Add the road to the solution

                road = new Road();
                road.getDestinations().add(conf.getCentralDepot()); //Add depot to the road
                time = 0;

                distance = 0;
                capacityRemained = conf.getTruck().getCapacity();


            }
        }
        if (road.getDestinations().size() > 1)
        {
            road.getDestinations().add(conf.getCentralDepot());
            solution.addRoads(road); // Add the road to the solution
        }
        solution.reCalculateTotalDistanceCovered(); // Set the total distance covered by the solution
        solution.setConfig(conf); // Set the configuration of the solution

        return solution;
    }



    public static Road addDepotToSwappedRoad(Road roadToReturn, Solution solution)
    {
        // If the depot is not added, then it is added
        if (!roadToReturn.getDestinations().get(roadToReturn.getDestinations().size()-1).getIdName().equals(solution.getConfig().getCentralDepot().getIdName()))
        {
            roadToReturn.getDestinations().add(solution.getConfig().getCentralDepot());
        }
        return roadToReturn;
    }

    public static void verifyIfAClientAppearsTwoTimesInARoad(Solution solution, String functionName)
    {
        int roadIndex = 0;
        for (Road road : solution.getRoads())
        {
            for (int i = 0; i < road.getDestinations().size(); i++)
            {
                for (int j = i+1; j < road.getDestinations().size(); j++)
                {
                    if (road.getDestinations().get(i) instanceof Depot)
                        continue;
                    if (road.getDestinations().get(i).getIdName().equals(road.getDestinations().get(j).getIdName()))
                    {
                        System.out.println("Le client " + road.getDestinations().get(i).getIdName() + " apparait deux fois dans la route " + roadIndex + " dans la fonction " + functionName);
                        //SolutionVisualization.DisplayGraph(solution, "functionName "+road.getDestinations().get(i).getIdName());
                    }
                }
            }
            roadIndex++;
        }
    }

    public static void verifyIfAclientIsNotDelivered(Solution solution, String functionName)
    {
        ArrayList<String> clientsNotDelivered = new ArrayList<>();
        for (Client client : solution.getConfig().getListClients())
        {
            clientsNotDelivered.add(client.getIdName());
        }
        for (Road road : solution.getRoads())
        {
            for (Destination client : road.getDestinations())
            {
                if (client instanceof Depot)
                    continue;
                if (clientsNotDelivered.contains(client.getIdName()))
                    clientsNotDelivered.remove(client.getIdName());
            }
        }
        if (clientsNotDelivered.size() > 0)
        {
            System.out.println("Il y a " + clientsNotDelivered.size() + " clients non livres dans la fonction " + functionName);
            for (String client : clientsNotDelivered)
            {
                System.out.println("Le client " + client + " n'est pas livre");
            }
        }
        else
        {
            System.out.println("Tous les clients sont livres");
        }
    }

    public static boolean verifyIfTheSolutionIsInList(ArrayList<Solution> listSolution, Solution solution)
    {
        ArrayList<ArrayList<String>> listIdNameForSolution = new ArrayList<>();
        for (Road road : solution.getRoads())
        {
            listIdNameForSolution.add(road.returnListOfIdClient());
        }

        boolean isSolutionInList = true;
        for (Solution solutionInList : listSolution)
        {
            isSolutionInList = true;

            for (Road road : solutionInList.getRoads())
            {

                if (!listIdNameForSolution.contains(road.returnListOfIdClient()))
                {
                    isSolutionInList = false;
                    break;
                }
            }
            if (!isSolutionInList)
                return false;
        }
        return true;
    }


}
