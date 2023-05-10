package Metaheuristique.Genetics;

import Logistique.Client;
import Logistique.Depot;
import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.Random;

public class Crossover {

    /***************** Clients which are served more than once functions *****************/

    private static Solution removeClientsServedMoreThanOneInTheRoad(Solution solutionChild, int indexRoad)
    {
        //Remove the clients that are served more than once in the solution
        ArrayList<String> clientsToRemoveId = new ArrayList<>();
        for (int index=1; index<solutionChild.getRoads().get(indexRoad).getDestinations().size()-1;index++)
        {
            for (int index2=index+1; index2<solutionChild.getRoads().get(indexRoad).getDestinations().size()-1;index2++)
            {
                if (solutionChild.getRoads().get(indexRoad).getDestinations().get(index).getIdName().equals(solutionChild.getRoads().get(indexRoad).getDestinations().get(index2).getIdName()))
                {
                    clientsToRemoveId.add(solutionChild.getRoads().get(indexRoad).getDestinations().get(index2).getIdName());
                }
            }
        }
        for (String idClientToRemove : clientsToRemoveId)
        {
            int indexClientToRemove = solutionChild.getRoads().get(indexRoad).returnListOfIdClient().indexOf(idClientToRemove)+1;
            solutionChild.getRoads().get(indexRoad).removeDestinationToRoadAndUpdateInfo(indexClientToRemove);
        }
        return solutionChild;
    }

    /**
     * This function removes the clients that are served more than once in the solution
     * @param solutionChild : Solution which contains the clients that are served more than once
     * @param indexRoad : Index of the road which is not modifiable
     * @return Solution without the clients that are served more than once
     */
    private static Solution removeClientServedMoreThanOnce(Solution solutionChild, int indexRoad)
    {
        // Remove the clients that are served more than once in the road
        solutionChild = removeClientsServedMoreThanOneInTheRoad(solutionChild, indexRoad);

        ArrayList<String> clientsToRemove = solutionChild.getRoads().get(indexRoad).returnListOfIdClient();
        ArrayList<Integer> roadIdToRemove = new ArrayList<>();


        for (int i = 0; i < solutionChild.getRoads().size(); i++)
        {
            if (i != indexRoad)
            {
                ArrayList<Destination> clientsToRemoveTemp = new ArrayList<>();
                for (int j = 0; j < solutionChild.getRoads().get(i).getDestinations().size(); j++)
                {
                    if (clientsToRemove.contains(solutionChild.getRoads().get(i).getDestinations().get(j).getIdName()))
                    {
                        clientsToRemoveTemp.add(solutionChild.getRoads().get(i).getDestinations().get(j));
                    }

                }
                for (Destination clientToRemove : clientsToRemoveTemp)
                {


                    int indexClientToRemove = solutionChild.getRoads().get(i).returnListOfIdClient().indexOf(clientToRemove.getIdName())+1;
                    solutionChild.getRoads().get(i).removeDestinationToRoadAndUpdateInfo(indexClientToRemove);

                }
            }
            if (solutionChild.getRoads().get(i).getDestinations().size() <= 2)
            {
                roadIdToRemove.add(solutionChild.getRoads().get(i).getIdRoad());
            }
        }

        if (roadIdToRemove.size() > 0)
        {
            for (Integer integer : roadIdToRemove) {
                solutionChild.removeRoadByItsId(integer);
            }
        }

        return solutionChild;
    }

    /***************** Clients which are not served functions *****************/
    private static ArrayList<String> getIdClientsNotServed(Solution solutionChild)
    {
        ArrayList<String> clientsNotServed = solutionChild.getConfig().getListClientsName();
        for (Road road : solutionChild.getRoads())
        {
            for (Destination destination : road.getDestinations())
            {
                if (destination instanceof Client)
                {
                    if (clientsNotServed.contains(destination.getIdName()))
                    {
                        clientsNotServed.remove(destination.getIdName());
                    }

                }
            }
        }
        return clientsNotServed;
    }

    private static ArrayList<Client> getClientsNotServed(Solution solutionChild, ArrayList<String> clientsNotServed)
    {
        ArrayList<Client> clientsNotServedTemp = new ArrayList<>();
        for (Client client : solutionChild.getConfig().getListClients())
        {
            if (clientsNotServed.contains(client.getIdName()))
            {
                clientsNotServedTemp.add(client);
            }
        }
        return clientsNotServedTemp;
    }

    /**
     * This function returns the id of the clients that are not served in the solution
     * @param solutionChild : Solution which contains the clients that are not served
     * @return ArrayList<String> : List of the id of the clients that are not served
     */
    private static Solution serveClientsNotServed(Solution solutionChild)
    {
        ArrayList<String> clientsNotServedId = getIdClientsNotServed(solutionChild);
        ArrayList<Client> clientsNotServed = getClientsNotServed(solutionChild, clientsNotServedId);

        Road roadCloned;
        for (Client client : clientsNotServed)
        {
            int indexRoad = 0;
            int indexClientToInsert = 0;
            int indexRoadToInsert = 0;
            Road roadToInsert = null;
            int minDistance = Integer.MAX_VALUE;
            //System.out.println("Ajout du client "+client.getIdName()+" dans la solution");
            for (Road road : solutionChild.getRoads())
            {
                for (int i = 1; i < road.getDestinations().size()-1; i++)
                {
                    roadCloned = road.clone();

                    int distance = 0;
                    int time = 0;
                    int capacityRemained = solutionChild.getConfig().getTruck().getCapacity();
                    int[] infos;

                    roadCloned.getDestinations().add(i, client);

                    for (int j = 1; j < roadCloned.getDestinations().size()-1; j++)
                    {

                        Destination startDestination = roadCloned.getDestinations().get(j-1);
                        Destination arriveDestination = roadCloned.getDestinations().get(j);

                        if (SolutionUtils.isClientCanBeDelivered(startDestination, arriveDestination, time, capacityRemained))
                        {
                            //roadCloned = SolutionUtils.calculateRoad(arriveDestination, time, distance, capacityRemained, roadCloned);
                            infos = SolutionUtils.calculateInfos(roadCloned, arriveDestination, time, distance, capacityRemained);
                            roadCloned.getDestinations().add(arriveDestination);
                        }
                        else
                        {
                            roadCloned = null;
                            break;
                        }

                        time = infos[0];
                        distance = infos[1];
                        capacityRemained = infos[2];

                    }
                    if (roadCloned == null)
                    {
                        continue;
                    }
                    if (roadCloned.getDestinations().get(roadCloned.getDestinations().size()-1) instanceof Depot)
                        roadCloned.getDestinations().remove(roadCloned.getDestinations().size()-1);

                    // Add depot at the end of the road
                    roadCloned = SolutionUtils.addDepotToSwappedRoad(roadCloned, solutionChild);

                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        indexRoadToInsert = indexRoad;
                        indexClientToInsert = i;
                        roadToInsert = roadCloned;

                    }
                }
                indexRoad++;
            }
            if (minDistance != Integer.MAX_VALUE && roadToInsert != null)
            {
                solutionChild.getRoads().set(indexRoadToInsert, roadToInsert);
                solutionChild.reCalculateTotalDistanceCovered();

            }
            else
            {
                //System.out.println("Le client "+client.getIdName()+" n'a pas pu etre ajoute");
                return null;
            }

        }
        return solutionChild;
    }

    /***************** Crossover functions *****************/

    /**
     * Reconstruct the road for the SBX crossover
     * @param solution1 : Solution 1
     * @param road1 : Road 1
     * @param index1 : Index of the road 1
     * @param road2 : Road 2
     * @param index2 : Index of the road 2
     * @return Road : Reconstructed road with road1 : 0->index1 and road2 : index2->end
     */
    private static Road reconstructRoadForSBX(Solution solution1, Road road1, int index1, Road road2, int index2)
    {
        int time = 0;
        int distance = 0;
        int capacityRemained = solution1.getConfig().getTruck().getCapacity();
        int[] infos;
        Road road = new Road();

        road.getDestinations().add(road1.getDestinations().get(0).clone());

        // Get the beginning of the first road
        for (int i = 0; i < index1; i++)
        {
            infos = SolutionUtils.calculateInfos(road, road1.getDestinations().get(i+1), time, distance, capacityRemained);
            time = infos[0];
            distance = infos[1];
            capacityRemained = infos[2];
            road.getDestinations().add(road1.getDestinations().get(i+1).clone());
        }

        road.getDestinations().add(road1.getDestinations().get(index1).clone());

        // Merge the two roads

        if (!road2.getDestinations().get(index2).getIdName().equals(road.getDestinations().get(road.getDestinations().size() - 1).getIdName()))
        {

            if (SolutionUtils.isClientCanBeDelivered(road.getDestinations().get(road.getDestinations().size() - 1).clone(), road2.getDestinations().get(index2).clone(), time, capacityRemained))
            {
                infos = SolutionUtils.calculateInfos(road, road2.getDestinations().get(index2), time, distance, capacityRemained);
                time = infos[0];
                distance = infos[1];
                capacityRemained = infos[2];
                road.getDestinations().add(road2.getDestinations().get(index2).clone());
            }
            else
                return null;
        }


        //Get the end of the second road
        for (int i = index2+1; i < road2.getDestinations().size(); i++)
        {
            Destination startDestination = road.getDestinations().get(road.getDestinations().size() - 1).clone();
            Destination endDestination = road2.getDestinations().get(i).clone();
            if (endDestination instanceof Depot)
            {
                road = SolutionUtils.addDepotToSwappedRoad(road,solution1);
                return road;
            }
            if (!road.returnListOfIdClient().contains(endDestination.getIdName()))
            {
                if (SolutionUtils.isClientCanBeDelivered(startDestination, endDestination, time, capacityRemained))
                {
                    infos = SolutionUtils.calculateInfos(road, endDestination, time, distance, capacityRemained);
                    time = infos[0];
                    distance = infos[1];
                    capacityRemained = infos[2];
                    road.getDestinations().add(endDestination.clone());
                }
                else
                    return null;
            }

        }

        return road;
    }

    /**
     * This function performs the RBX crossover
     * @param solutionParent1 : Solution 1
     * @param solutionParent2 : Solution 2
     * @return Solution : Child solution
     */
    public static Solution crossOverRBX(Solution solutionParent1, Solution solutionParent2, int indexRoad1, int indexRoad2)
    {
        //Select random roads to merge
        Random random = new Random();

        Road road1 = solutionParent1.getRoads().get(indexRoad1).clone();

        Solution solutionChild = solutionParent2.clone();
        solutionChild.getRoads().set(indexRoad2, road1);
        solutionChild.getRoads().get(indexRoad2).setIdRoad(indexRoad2);

        //Remove a client if he is served more than once in the solution
        solutionChild = removeClientServedMoreThanOnce(solutionChild, indexRoad2);

        //Serve the clients not served
        solutionChild = serveClientsNotServed(solutionChild);


        if (solutionChild == null)
            return null;
        else
        {
            //SolutionUtils.verifyIfAClientAppearsTwoTimesInARoad(solutionChild, "RBX function served 2 times");
            solutionChild.reCalculateTotalDistanceCovered();

        }
        return solutionChild;
    }

    /**
     * This function performs the SBX crossover
     * @param solutionParent1 : Solution 1
     * @param solutionParent2 : Solution 2
     * @return Solution : Child solution
     */
    public static Solution crossOverSBX(Solution solutionParent1, Solution solutionParent2, int indexRoad1, int indexRoad2)
    {
        //Select random roads to merge
        Random random = new Random();

        Road road1 = solutionParent1.getRoads().get(indexRoad1).clone();
        Road road2 = solutionParent2.getRoads().get(indexRoad2).clone();

        /*System.out.println("Road 1: " + road1.toString());
        System.out.println("Road 2: " + road2.toString());*/

        // Select the index for the crossover client 0 -> index
        int indexBeginningRoad1 = random.nextInt(road1.getDestinations().size()-2)+1;

        // Select the index for the crossover client end -> index
        int indexEndRoad2 = random.nextInt(road2.getDestinations().size()-2)+1;

        /*System.out.println("Index 1: " + indexBeginningRoad1);
        System.out.println("Index 2: " + indexEndRoad2);
        System.out.println("Route qui doit etre cree : ");

        for (int i = 0; i < indexBeginningRoad1+1; i++)
        {
            System.out.println("Client 1: " + road1.getDestinations().get(i).getIdName());
        }

        for (int i = indexEndRoad2; i < road2.getDestinations().size(); i++)
        {
            System.out.println("Client 2: " + road2.getDestinations().get(i).getIdName());
        }*/

        // Create the new solution
        Solution solutionChild = solutionParent1.clone();
        Road roadChild = reconstructRoadForSBX(solutionParent1, road1, indexBeginningRoad1, road2, indexEndRoad2);
        if (roadChild == null) return null;
        solutionChild.getRoads().set(indexRoad1, roadChild);
        /*System.out.println("Road generated: ");
        for (Destination destination : solutionChild.getRoads().get(indexRoad1).getDestinations())
        {
            System.out.println(destination.getIdName());
        }
        for (Edge edge : solutionChild.getRoads().get(indexRoad1).getEdges())
        {
            System.out.println(edge.toString());
        }*/

        //Remove a client if he is served more than once in the solution
        solutionChild = removeClientServedMoreThanOnce(solutionChild, indexRoad1);

        //Serve the clients not served
        solutionChild = serveClientsNotServed(solutionChild);


        if (solutionChild == null)
            return null;
        else
        {
            //SolutionUtils.verifyIfAClientAppearsTwoTimesInARoad(solutionChild, "SBX function served 2 times");
            solutionChild.reCalculateTotalDistanceCovered();
        }
        return solutionChild;

    }
}