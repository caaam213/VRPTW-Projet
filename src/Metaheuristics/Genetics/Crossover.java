package Metaheuristics.Genetics;

import Logistics.Client;
import Logistics.Depot;
import Logistics.Destination;
import Metaheuristics.MetaheuristicUtils;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.Random;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;

/**
 * Cette classe contient les fonctions pour le crossover
 */
public class Crossover {

    /*Fonctions qui gèrent les fonctions qui ont été servis plusieurs fois */

    /**
     * Cette fonction va supprimer les clients qui ont été servis plusieurs fois dans la route
     * @param solutionChild : Solution qui contient les clients qui ont été servis plusieurs fois
     * @param indexRoad : Index de la route où l'on supprime les clients qui ont été servis plusieurs fois
     * @return Solution avec la route où les clients ont été supprimés
     */
    private static Solution removeClientsServedMoreThanOneInTheRoad(Solution solutionChild, int indexRoad)
    {
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
            int indexClientToRemove = solutionChild.getRoads().get(indexRoad).returnListOfIdClient()
                    .indexOf(idClientToRemove)+1;
            solutionChild.getRoads().get(indexRoad).getDestinations().remove(indexClientToRemove);
        }
        return solutionChild;
    }


    /**
     * Cette fonction va supprimer les clients qui ont été servis plusieurs fois dans toutes les routes de la solution
     * sauf dans la route <indexRoad>
     * @param solutionChild : Solution qui contient les clients qui ont été servis plusieurs fois
     * @param indexRoad : Index de la route où l'on ne supprime pas les clients qui ont été servis plusieurs fois
     * @param timeConstraint : Booléen qui indique si l'on doit respecter la contrainte de temps
     * @return Solution avec les routes où les clients n'ont été livré qu'une seule fois
     */
    public static Solution removeClientServedMoreThanOnce(Solution solutionChild, int indexRoad, boolean timeConstraint)
    {
        // Supprimer les clients qui ont été servis plusieurs fois dans la route <indexRoad>
        solutionChild = removeClientsServedMoreThanOneInTheRoad(solutionChild, indexRoad);

        ArrayList<String> clientsToRemove = solutionChild.getRoads().get(indexRoad).returnListOfIdClient();

        // Supprimer les clients qui ont été servis plusieurs fois dans les autres routes
        for (int i = 0; i < solutionChild.getRoads().size(); i++)
        {
            if (i != indexRoad)
            {
                ArrayList<Destination> clientsToRemoveTemp = new ArrayList<>();
                // Pour chaque client de la route <i>, on va vérifier si il est dans la liste des clients livrés
                // dans la route <indexRoad> et si oui, on l'ajoute dans la liste temporaire des clients à supprimer
                for (int j = 0; j < solutionChild.getRoads().get(i).getDestinations().size(); j++)
                {
                    if (clientsToRemove.contains(solutionChild.getRoads().get(i).getDestinations().get(j).getIdName()))
                    {
                        clientsToRemoveTemp.add(solutionChild.getRoads().get(i).getDestinations().get(j));
                    }

                }

                // Suppression des clients qui ont été servis plusieurs fois dans la route <i>
                for (Destination clientToRemove : clientsToRemoveTemp)
                {
                    int indexClientToRemove = solutionChild.getRoads().get(i).returnListOfIdClient().indexOf(clientToRemove.getIdName())+1;
                    solutionChild.getRoads().get(i).getDestinations().remove(indexClientToRemove);

                }
            }

        }

        // On vérifie que la solution est valide
        if(MetaheuristicUtils.verifyIfRoadValid(solutionChild, indexRoad, timeConstraint)==null)
        {
            return null;
        }

        // On supprime les routes qui sont vides
        solutionChild = MetaheuristicUtils.removeUselessRoad(solutionChild);

        return solutionChild;
    }

    /* Fonctions pour les clients qui n'ont pas été servis */
    /**
     * Récupérer les clients qui n'ont pas été servis dans la solution
     * @param solutionChild : Solution dont certains clients n'ont potentiellement pas été servis
     * @return Liste des ids des clients qui n'ont pas été servis
     */
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

    /**
     * Récupérer les clients qui n'ont pas été servis dans la solution à partir de leurs id
     * @param solutionChild : Solution dont certains clients n'ont potentiellement pas été servis
     * @param clientsNotServed : Liste des ids des clients qui n'ont pas été servis
     * @return Liste des clients qui n'ont pas été servis
     */
    private static ArrayList<Client> getClientsNotServed(Solution solutionChild, ArrayList<String> clientsNotServed)
    {
        ArrayList<Client> clientsNotServedTemp = new ArrayList<>();
        for (Client client : solutionChild.getConfig().getClientsList())
        {
            if (clientsNotServed.contains(client.getIdName()))
            {
                clientsNotServedTemp.add(client);
            }
        }
        return clientsNotServedTemp;
    }


    /**
     * Cette fonction va servir les clients qui n'ont pas été servis dans la solution
     * @param solutionChild : Solution dont certains clients n'ont potentiellement pas été servis
     * @param timeConstraint : Booléen qui indique si l'on doit respecter la contrainte de temps
     * @return Solution avec les clients qui sont tous servis
     */
    private static Solution serveClientsNotServed(Solution solutionChild, boolean timeConstraint)
    {
        // Récupérer les clients qui n'ont pas été servis
        ArrayList<String> clientsNotServedId = getIdClientsNotServed(solutionChild);
        ArrayList<Client> clientsNotServed = getClientsNotServed(solutionChild, clientsNotServedId);

        Road roadCloned;
        for (Client client : clientsNotServed)
        {
            int indexRoad = 0;
            int indexRoadToInsert = 0;
            Road roadToInsert = null;
            int minDistance = Integer.MAX_VALUE;

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

                    // On va essayer d'affecter le client à la route <road> et on va vérifier si la solution est valide
                    for (int j = 1; j < roadCloned.getDestinations().size()-1; j++)
                    {

                        Destination startDestination = roadCloned.getDestinations().get(j-1);
                        Destination arriveDestination = roadCloned.getDestinations().get(j);
                        int distanceBetweenDepartAndArrive = distanceBetweenTwoDestination(startDestination, arriveDestination);

                        if (SolutionUtils.isClientCanBeDelivered(startDestination, arriveDestination, time, capacityRemained, timeConstraint))
                        {
                            infos = SolutionUtils.calculateInfosUsingDistanceBetween2Dests(arriveDestination, time, distance, distanceBetweenDepartAndArrive,
                                    capacityRemained);
                            roadCloned.getDestinations().add(arriveDestination);
                        }
                        else
                        {
                            roadCloned = null;
                            break;
                        }

                        time = infos[0];
                        capacityRemained = infos[1];
                        distance = infos[2];
                    }
                    if (roadCloned == null)
                    {
                        continue;
                    }

                    // On enlève le depot de la route
                    if (roadCloned.getDestinations().get(roadCloned.getDestinations().size()-1) instanceof Depot)
                        roadCloned.getDestinations().remove(roadCloned.getDestinations().size()-1);

                    // On ajoute le depot à la route avec les bonnes informations
                    roadCloned = SolutionUtils.addDepotToSwappedRoad(roadCloned, solutionChild);

                    // Si lorsque l'on a placé le client dans la route et que celle-ci est valide, on regarde si
                    // la distance est plus petite que la distance minimale. Si c'est le cas, on place le client
                    // dans la route
                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        indexRoadToInsert = indexRoad;
                        roadToInsert = roadCloned;

                    }
                }
                indexRoad++;
            }

            // On remplace la route par la route avec le client inséré et on met à jour la distance totale
            if (minDistance != Integer.MAX_VALUE && roadToInsert != null)
            {
                solutionChild.getRoads().set(indexRoadToInsert, roadToInsert);
                solutionChild.reCalculateTotalDistanceCovered();

            }
            else
            {
                return null;
            }

        }
        return solutionChild;
    }

    /*Fonctions d'appel des crossover*/

    /**
     * Cette fonction va appeler le crossover RBX
     * @param solutionParent1 : Solution 1 à croiser
     * @param solutionParent2 : Solution 2 à croiser
     * @return Solution : La solution enfant
     */
    public static Solution crossOverRBX(Solution solutionParent1, Solution solutionParent2, int indexRoad1, int indexRoad2,
                                        boolean timeConstraint)
    {

        // On récupère les routes à croiser
        Road road1 = solutionParent1.getRoads().get(indexRoad1).clone();
        Road road2 = solutionParent2.getRoads().get(indexRoad2).clone();

        // Si les routes sont les mêmes, on ne fait rien
        if (road1.equals(road2))
            return null;

        // Au début, la solution enfant sera un clone la solution parent 2
        Solution solutionChild = solutionParent2.clone();

        // On met la route <indexRoad1> de la solution parent 1 dans l'indice <indexRoad2> dans la solution enfant
        solutionChild.getRoads().set(indexRoad2, road1);
        solutionChild.getRoads().get(indexRoad2).setIdRoad(indexRoad2+1);

        //On supprime les clients servis plus d'une fois
        solutionChild = removeClientServedMoreThanOnce(solutionChild, indexRoad2, timeConstraint);
        if (solutionChild == null) return null;

        //On sert les clients qui n'ont pas été servis
        solutionChild = serveClientsNotServed(solutionChild, timeConstraint);

        if (solutionChild == null)
            return null;
        else
        {
            solutionChild.reCalculateTotalDistanceCovered(); // On met à jour la distance totale

        }
        return solutionChild;
    }

    /**
     * Cette fonction va appeler le crossover SBX
     * @param solutionParent1 : Solution 1 à croiser
     * @param solutionParent2 : Solution 2 à croiser
     * @return Solution : La solution enfant
     */
    public static Solution crossOverSBX(Solution solutionParent1, Solution solutionParent2, int indexRoad1, int indexRoad2,
                                        int indexBeginningRoad1, int indexEndRoad2, boolean timeConstraint)
    {
        // Si les indices sont incorrects pour faire un crossover, on ne fait rien
        if (indexBeginningRoad1 == 0 || indexEndRoad2 == 0)
            return null;

        if (indexBeginningRoad1 >= solutionParent1.getRoads().size() - 2 ||
                indexEndRoad2 >= solutionParent2.getRoads().size() - 2)
            return null;


        //Selectionner les routes à croiser au hasard
        Random random = new Random();

        Road road1 = solutionParent1.getRoads().get(indexRoad1).clone();
        Road road2 = solutionParent2.getRoads().get(indexRoad2).clone();

        // Si les routes sont les mêmes, on ne fait rien
        if (road1.equals(road2))
            return null;

        // La solution enfant sera un clone de la solution parent 1
        Solution solutionChild = solutionParent1.clone();

        // Récupérer les destinations de la route 1
        ArrayList<Destination> destinations1 = road1.getDestinations();

        // Réupérer les destinations de la route 2
        ArrayList<Destination> destinations2 = road2.getDestinations();

        // Récupérer les destinations de la solution enfant de 0 à <indexBeginningRoad1>
        ArrayList<Destination> newDestinations1 = MetaheuristicUtils.getSubList(destinations1, 0, indexBeginningRoad1);

        // Récupérer les destinations de la solution enfant de <indexEndRoad2> à la fin
        ArrayList<Destination> subList2NewClient2End = MetaheuristicUtils.getSubList(destinations2, indexEndRoad2,
                destinations2.size()-1);

        // On les fusionne
        newDestinations1.addAll(subList2NewClient2End);

        // On met cette route dans la solution enfant à l'indice <indexRoad1>
        solutionChild.getRoads().get(indexRoad1).setDestinations(newDestinations1);

        //Supprimer les clients servis plus d'une fois
        solutionChild = removeClientServedMoreThanOnce(solutionChild, indexRoad1, timeConstraint);


        if (solutionChild == null) return null;

        //Sert les clients qui n'ont pas été servis
        solutionChild = serveClientsNotServed(solutionChild, timeConstraint);


        if (solutionChild == null)
            return null;
        else
        {
            solutionChild.reCalculateTotalDistanceCovered(); // On met à jour la distance totale
        }
        return solutionChild;

    }
}