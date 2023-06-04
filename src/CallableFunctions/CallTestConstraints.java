package CallableFunctions;

import Logistics.Client;
import Logistics.Configuration;
import Logistics.Destination;
import Metaheuristics.Road;
import Utils.SolutionUtils;

import java.util.ArrayList;

public class CallTestConstraints {

    public static void displayMinimalNumberOfVehicles(String fileNumber)
    {
        Configuration configuration = new Configuration(fileNumber);
        int minimalNumberOfVehicles = configuration.getNumberOfMinimalVehicles();
        System.out.println("Le nombre minimal de vehicules est : " + minimalNumberOfVehicles);
    }

    public static void calculateDistanceBetweenTwoDestination()
    {
        Configuration configuration = new Configuration("4");

        Integer[] rightDistances = {32,34,25,41};
        for (int i = 0; i < 4; i++) {
            Client client1 = configuration.getClientsList().get(i);
            Client client2 = configuration.getClientsList().get(i+1);

            int distance = SolutionUtils.distanceBetweenTwoDestination(client1, client2);

            if(distance == rightDistances[i])
            {
                System.out.println("Distance calculee entre " + client1.getIdName()+ " et " + client2.getIdName() +
                        " : " + distance + " (OK)");
            }
            else
            {
                System.out.println("Distance calculee entre " + client1.getIdName()+ " et " + client2.getIdName() +
                        " : " + distance + " (ERREUR)");
            }
        }
    }

    public static void testIfConstraintsAreRespected()
    {
        Configuration configuration = new Configuration("4");
        boolean[] respectedConstraints = {true, true, true, false};
        int time = 0;
        int capacity = configuration.getTruck().getCapacity();

        for (int i = 0; i < 4; i++) {
            Client client1 = configuration.getClientsList().get(i);
            Client client2 = configuration.getClientsList().get(i+1);
            int distanceBetweenTwoClients = SolutionUtils.distanceBetweenTwoDestination(client1, client2);
            if(SolutionUtils.isClientCanBeDelivered(configuration.getCentralDepot(),client1, client2, time, capacity, true) == respectedConstraints[i])
            {
                System.out.println("L'algorithme a bien detecte que la contrainte est  :" + respectedConstraints[i]);
            }
            else
            {
                System.out.println("L'algorithme n'a pas bien detecte que la contrainte est  :" + respectedConstraints[i]);
            }
            time += distanceBetweenTwoClients;
            capacity -= client2.getDemand();
        }



    }

    public static void testCalculateInfos()
    {
        Configuration configuration = new Configuration("4");
        int distance = 0;
        int time = 0;
        int capacity = configuration.getTruck().getCapacity();
        int[] infosToRetrieve = {230,135,132};
        Road newRoad = new Road(1);
        ArrayList<Destination> destinations = newRoad.getDestinations();

        destinations.add(configuration.getCentralDepot());
        for (int index=0; index<3; index++)
        {
            destinations.add(configuration.getClientsList().get(index));
            System.out.println(configuration.getClientsList().get(index).getDemand());
        }
        destinations.add(configuration.getCentralDepot());
        newRoad.setDestinations(destinations);

        for (int i = 0; i < newRoad.getDestinations().size()-1; i++) {
            Client client1 = configuration.getClientsList().get(i);
            Client client2 = configuration.getClientsList().get(i+1);
            int distanceBetweenTwoClients = SolutionUtils.distanceBetweenTwoDestination(client1, client2);
            int[] infos = SolutionUtils.calculateInfosUsingDistanceBetween2Dests(client2, time, distance, distanceBetweenTwoClients, capacity);
            time = infos[0];
            capacity = infos[1];
            distance = infos[2];
        }
        if (time == infosToRetrieve[0] && capacity == infosToRetrieve[1] && distance == infosToRetrieve[2])
        {
            System.out.println("Les infos calculees sont correctes");
        }
        else
        {
            System.out.println("Les infos calculees sont incorrectes");
        }

        System.out.println("Distance : " + distance);
        System.out.println("Time : " + time);
        System.out.println("Capacity : " + capacity);
    }
}
