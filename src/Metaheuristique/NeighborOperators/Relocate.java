package Metaheuristique.NeighborOperators;

import Logistique.Destination;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.Transformation;
import Utils.SolutionUtils;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;


public class Relocate {

    static Transformation transformation;


    // Relocate intra route
    public static Result RelocateIntra(Solution solution, int roadSelected, int newIndexClient, int indexClient) {
        if(indexClient == newIndexClient)
        {
            System.out.println("indexClient == newIndexClient");
            return null;
        }

        if(indexClient == 0 || newIndexClient == 0)
        {
            return null;
        }

        if (indexClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1 || newIndexClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1)
        {
            return null;
        }

        System.out.println("indexClient = " + indexClient + " newIndexClient = " + newIndexClient);
        System.out.println("BAASE");

        for(int j=0; j < solution.getRoads().get(roadSelected).getDestinations().size(); j++)
        {
            System.out.println("Destinsation : " + solution.getRoads().get(roadSelected).getDestinations().get(j).getIdName());

        }
        System.out.println();

        // on récupère la route du trajet concerné
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        // On recupère la destination du client
        Destination arriveClient = newRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newRoad.getDestinations().remove(indexClient);
        newRoad.getDestinations().add(newIndexClient, arriveClient);


        for(int j=0; j < newRoad.getDestinations().size(); j++)
        {
            System.out.println("Destinsation : " + newRoad.getDestinations().get(j).getIdName());
        }
        // on crée un candidat
        Solution candidate = solution.clone();
        // on ajoute le client à sa nouvelle position
        //newRoad = newRoad.addDestinationsAndUpdateEdgeToRoad(arriveClient, newIndexClient);
        // On vérifie si les conditions sont respectées
        // on vérifie pour chacune des destinations de la route si le nouveau trajet est possible
        candidate.getRoads().set(roadSelected, newRoad);
        int size = candidate.getARoad(roadSelected).getDestinations().size();
        int infos[] = {0,0, solution.getConfig().getTruck().getCapacity(),0};
        for (int i = 0; i < size-1; i++) {
            // time, distance, capacity, distanceBetweenTwoDestinations
            int chrono = infos[0];
            int capacityActul = infos[2];
            Destination departedClient = newRoad.getDestinations().get(i);
            Destination arrivedClient = newRoad.getDestinations().get(i+1);
            int dis = distanceBetweenTwoDestination(departedClient, arrivedClient);
            if (SolutionUtils.isClientCanBeDelivered(departedClient, arrivedClient, chrono, capacityActul) == false) {
                System.out.println("Client : " + departedClient.getIdName());
                System.out.println("Client : " + arrivedClient.getIdName());
                System.out.println("Temps actuel : " + chrono);
                System.out.println("Distance : " + dis);
                System.out.println("DueTime : " + arrivedClient.getDueTime());
                System.out.println("conditions non respectees");
                return null;
            }
            else {
                // isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
                infos = SolutionUtils.calculateInfos(newRoad, arrivedClient,chrono, dis, capacityActul);
            }
        }
        System.out.println("Toutes les conditions sont respectees");
        Transformation transformation = new Transformation("Relocate-intra",roadSelected, roadSelected, newIndexClient, indexClient);
        Result res = new Result(candidate, transformation);
        return res;
    }

    // Relocate inter routes
    public static Result RelocateInter(Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
    {
        if(indexClient >= solution.getRoads().get(firstClientRoad).getDestinations().size()-1 || newIndexClient >= solution.getRoads().get(secondClientRoad).getDestinations().size()-1 || newIndexClient == 0 || indexClient == 0)
        {
            //System.out.println("newIndexClient > firstClientRoad || newIndexClient > secondClientRoad");
            return null;
        }

        Result result;
        transformation = new Transformation("Relocate-inter",indexClient, newIndexClient, firstClientRoad, secondClientRoad);
        // on récupère la route du trajet concerné
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        // On recupère la destination du client
        Destination arriveFirstClient = newFirstRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newFirstRoad.getDestinations().remove(indexClient);
        // on crée un candidat
        Solution candidate = solution.clone();
        // on ajoute le client à sa nouvelle position
        newSecondRoad.getDestinations().add(newIndexClient, arriveFirstClient);
        // On vérifie si les conditions sont respectées
        // on vérifie pour chacune des destinations de la route si le nouveau trajet est possible


        /*for(int j=0; j < newFirstRoad.getDestinations().size(); j++)
        {
            System.out.println("Destinsation Premiere Route : " + newFirstRoad.getDestinations().get(j).getIdName());
        }
        System.out.println();
        for(int j=0; j < newSecondRoad.getDestinations().size(); j++)
        {
            System.out.println("Destinsation Seconde Route: " + newSecondRoad.getDestinations().get(j).getIdName());
        }*/
        Road Tab[] = {newFirstRoad, newSecondRoad};
        for(int j = 0; j < 2; j++) {
            int infos[] = {0,0, solution.getConfig().getTruck().getCapacity(),0};
            int size = Tab[j].getDestinations().size();
            for (int i = 0; i < size - 1; i++) {
                int chrono = infos[0];
                int capacityActul = infos[2];
                Destination departedClient = Tab[j].getDestinations().get(i);
                Destination arrivedClient = Tab[j].getDestinations().get(i + 1);
                int dis = distanceBetweenTwoDestination(departedClient, arrivedClient);
                if (SolutionUtils.isClientCanBeDelivered(departedClient, arrivedClient, chrono, capacityActul) == false) {
                    /*System.out.println("Client : " + departedClient.getIdName());
                    System.out.println("Client : " + arrivedClient.getIdName());
                    System.out.println("Temps actuel : " + chrono);
                    System.out.println("Distance : " + dis);
                    System.out.println("DueTime : " + arrivedClient.getDueTime());
                    System.out.println("conditions non respectees");*/
                    return null;
                } else {

                    // isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity)
                    infos = SolutionUtils.calculateInfos(Tab[j], arrivedClient, chrono, dis, capacityActul);

                }
            }
        }
        candidate.getRoads().set(firstClientRoad, newFirstRoad);
        candidate.getRoads().set(secondClientRoad, newSecondRoad);
        if(newFirstRoad.getDestinations().size() <= 2)
        {
            candidate.getRoads().remove(firstClientRoad);
        }
        result = new Result(candidate, transformation);
        return result;
    }

}
