package Utils;

import Logistics.Client;
import Logistics.Configuration;
import Logistics.Depot;
import Logistics.Destination;
import Metaheuristics.Road;
import Metaheuristics.Solution;

import java.util.ArrayList;

/**
 * Cette classe contient toutes les méthodes utiles pour gérer les solutions
 */
public class SolutionUtils {

    /**
     * Récupérer un client aléatoire dans une liste de clients
     * @param clients : la liste de clients
     * @return un client aléatoire
     */
    public static Client getRandomClient(ArrayList<Client> clients)
    {
        int randomIndex = (int) (Math.random() * clients.size());
        return clients.get(randomIndex);
    }


    /**
     * Permet de faire la racine carrée d'un nombre
     * @param a : le nombre
     * @return racine carrée de a
     */
    static private double sqr(double a) {
        return a*a;
    }

    /**
     * Calculer la distance entre deux destinations
     * @param start : la destination de départ
     * @param arrive : la destination d'arrivée
     * @return la distance entre les deux destinations
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
     * Fonction qui permet de vérifier si les contraintes sont respectées
     * @param startClient : le client de départ
     * @param arriveClient : le client d'arrivée
     * @param time : le temps actuel
     * @param capacity : la capacité actuelle
     * @param timeConstraint : si on doit vérifier la contrainte de temps
     * @return true si les contraintes sont respectées, false sinon
     */
    public static boolean isClientCanBeDelivered(Destination startClient, Destination arriveClient, int time, int capacity,
                                                 boolean timeConstraint)
    {

        // On vérifie si la destination d'arrivée est null
        if(arriveClient == null)
        {
            return false;
        }

        if (arriveClient instanceof Client)
        {
            // Si on vérifie la contrainte de temps, on vérifie si time+distance > arriveClient.getDueTime()
            int timeCalculated = time + distanceBetweenTwoDestination(startClient, arriveClient);
            if (timeConstraint) {
                if (timeCalculated > arriveClient.getDueTime()) {
                    return false;
                }
            }
            // Vérifie si la capacité est suffisante
            if (((Client) arriveClient).getDemand() > capacity)
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Fonction qui permet de vérifier si les contraintes sont respectées pour un client donné ou sinon de le
     * remplacer par un autre
     * @param clientsNotDeliveredToManageDueTime Liste de clients non livrés
     * @param startClient Client de départ
     * @param client Client de destination à vérifier
     * @param time Temps actuel
     * @param capacity Capacité actuelle
     * @param timeConstraint Si on doit vérifier la contrainte de temps
     * @return Le client à livrer
     */
    public static Client selectClientMoreThoroughly(ArrayList<Client> clientsNotDeliveredToManageDueTime, Destination startClient, Client client, int time, int capacity, boolean timeConstraint)
    {

        while (clientsNotDeliveredToManageDueTime.size()>0)
        {
            if (isClientCanBeDelivered(startClient,client,  time,  capacity, timeConstraint))
            {
                break;
            }
            else
            {
                // On retire le client de la liste et on en prend un autre
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
     * Permet de calculer le temps
     * @param client : le client
     * @param time : le temps
     * @param distanceBetweenTwoDestinations : la distance entre deux destinations
     * @return le temps
     */
    public static int calculateTime(Destination client, int time, int distanceBetweenTwoDestinations)
    {
        // Si time+distanceBetweenTwoDestinations < client.getReadyTime() alors time = client.getReadyTime()
        if(time+distanceBetweenTwoDestinations<client.getReadyTime())
            time = client.getReadyTime();
        else
            time += distanceBetweenTwoDestinations;

        // Si la Destination est un client, on ajoute le temps de service
        if (client instanceof Client)
        {
            time+= ((Client) client).getService();
        }

        return time;
    }


    /**
     * Permet de calculer le temps, la distance et la capacité pour un client donné et le dernier client de la liste
     * @param road : la route
     * @param client : le client
     * @param time : le temps
     * @param distance : la distance
     * @param capacity : la capacité
     * @return un tableau contenant le temps, la distance, la capacité et la distance entre deux destinations
     */
    public static int[] calculateInfosBClientAndTheLastOnTheList(Road road, Destination client, int time, int distance, int capacity)
    {
        int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-1), client);

        time = calculateTime(client,time,distanceBetweenTwoDestinations);

        if (client instanceof Client)
        {
            distance += distanceBetweenTwoDestinations;
            capacity -= ((Client) client).getDemand();
        }

        return new int[]{time, distance, capacity, distanceBetweenTwoDestinations};
    }

    /**
     * Permet de calculer le temps, la distance et la capacité pour un client donné en se basant sur la distance
     * entre deux destinations. Cette fonction fait la meme chose que la fonction calculateInfosBClientAndTheLastOnTheList
     * mais elle est utilisée pour les clients qui ne sont pas le dernier de la liste
     * @param client : le client
     * @param time : le temps
     * @param distance : la distance
     * @param distanceBetweenTwoDestinations : la distance entre deux destinations
     * @param capacity : la capacité
     * @return un tableau contenant le temps, la distance et la capacité
     */
    public static int[] calculateInfosUsingDistanceBetween2Dests(Destination client, int time, int distance,
                                                                 int distanceBetweenTwoDestinations, int capacity)
    {

        time = calculateTime(client,time,distanceBetweenTwoDestinations);
        distance += distanceBetweenTwoDestinations;

        if (client instanceof Client)
        {
            capacity -= ((Client) client).getDemand();
        }

        return new int[]{time, capacity, distance};
    }
    private static Destination getNearestClient(ArrayList<Client> clientsNotDeliveredToManageDueTime, Destination startClient, int time, int capacity,
                                                boolean timeConstraint)
    {
        Client nearestClient = getRandomClient(clientsNotDeliveredToManageDueTime);
        int distance = Integer.MAX_VALUE;
        for (Client client : clientsNotDeliveredToManageDueTime)
        {
            int distanceBetweenTwoDestinations = distanceBetweenTwoDestination(startClient, client);
            if (distanceBetweenTwoDestinations < distance && isClientCanBeDelivered(startClient, client, time, capacity, timeConstraint))
            {
                distance = distanceBetweenTwoDestinations;
                nearestClient = client;
            }
        }
        return nearestClient;
    }


    /**
     * Permet de générer une solution aléatoire
     * @param conf : la configuration
     * @param generateVeryRandomSolution : si on doit générer une solution très aléatoire
     * @param timeConstraint : si on doit vérifier la contrainte de temps
     * @return la solution générée
     */
    public static Solution generateRandomSolution(Configuration conf, boolean generateVeryRandomSolution, boolean timeConstraint) {
        // Initialiser les variables pour la solution
        ArrayList<Client> clientsNotDelivered = (ArrayList<Client>)conf.getClientsList().clone();
        int totalDistance = 0;
        Solution solution = new Solution();
        int time = 0;
        int distance = 0;
        int capacityRemained = conf.getTruck().getCapacity();
        int roadId = 1;
        Road road = new Road(roadId);

        road.getDestinations().add(conf.getCentralDepot());


        Client client;
        while (clientsNotDelivered.size() > 0) {


            if (generateVeryRandomSolution)
            {
                // Prendre un voisin au hasard
                client = getRandomClient(clientsNotDelivered);
                ArrayList<Client> clientsNotDeliveredToManageDueTime = (ArrayList<Client>)clientsNotDelivered.clone();
                client = selectClientMoreThoroughly(clientsNotDeliveredToManageDueTime,
                        road.getDestinations().get(road.getDestinations().size()-1),
                        client,
                        time,
                        capacityRemained,
                        timeConstraint);
            }
            else
            {
                // Prendre un voisin proche
                client = (Client) getNearestClient(clientsNotDelivered, road.getDestinations().get(road.getDestinations().size()-1), time, capacityRemained, timeConstraint);
            }


            // Si le client peut être livré, alors on maj chaque info
            if (isClientCanBeDelivered(road.getDestinations().get(road.getDestinations().size()-1), client, time, capacityRemained, timeConstraint)) {

                int infos[] = calculateInfosBClientAndTheLastOnTheList(road, client, time, distance, capacityRemained);
                time = infos[0];
                distance = infos[1];
                capacityRemained = infos[2];

                road.getDestinations().add(client);
                clientsNotDelivered.remove(client);


            } else {
                // Si les contraintes ne sont pas respectée, on ajoute le dépôt à la fin de la route et on crée une
                // nouvelle
                road.getDestinations().add(conf.getCentralDepot());
                distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-2),
                        conf.getCentralDepot());

                solution.addRoads(road); // Ajouter la route à la solution
                road.setDistance(distance); // On affecte la distance à la route
                totalDistance += distance; // On incrémente la distance totale

                roadId++;
                road = new Road(roadId);
                road.getDestinations().add(conf.getCentralDepot());

                time = 0;
                distance = 0;
                capacityRemained = conf.getTruck().getCapacity();


            }
        }
        if (road.getDestinations().size() > 1)
        {
            // Même code que les lignes 231 to 234
            road.getDestinations().add(conf.getCentralDepot());
            distance += distanceBetweenTwoDestination(road.getDestinations().get(road.getDestinations().size()-2),
                    conf.getCentralDepot());
            solution.addRoads(road);
            road.setDistance(distance);
            totalDistance += distance;
        }
        solution.setTotalDistanceCovered(totalDistance); // Affecter la distance totale à la solution pour éviter de la recalculer
        solution.setConfig(conf); // Affecter la configuration à la solution

        return solution;
    }


    /**
     * Permet d'ajouter le dépôt à la fin d'une route
     * @param roadToReturn : la route à laquelle on doit ajouter le dépôt
     * @param solution : la solution à laquelle on a ajouté le dépôt
     * @return la route avec le dépôt ajouté
     */
    public static Road addDepotToSwappedRoad(Road roadToReturn, Solution solution)
    {
        // On ajoute le dépôt à la fin de la route si ce n'est pas déjà le cas
        if (!roadToReturn.getDestinations().get(roadToReturn.getDestinations().size()-1).getIdName().equals(
                solution.getConfig().getCentralDepot().getIdName()))
        {
            roadToReturn.getDestinations().add(solution.getConfig().getCentralDepot());
        }
        return roadToReturn;
    }

    /**
     * Permet de vérifier si tous les clients ont bien été livrés une seule fois
     * @param solution : la solution à vérifier
     * @param functionName : le nom de la fonction qui appelle cette fonction
     */
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
                    }
                }
            }
            roadIndex++;
        }
    }

    /**
     * Permet de vérifier si tous les clients ont bien été livrés
     * @param solution : la solution à vérifier
     * @param functionName : le nom de la fonction qui appelle cette fonction
     */
    public static void verifyIfAclientIsNotDelivered(Solution solution, String functionName)
    {
        ArrayList<String> clientsNotDelivered = new ArrayList<>();
        for (Client client : solution.getConfig().getClientsList())
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




}
