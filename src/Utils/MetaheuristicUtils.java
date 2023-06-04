package Utils;
import Logistics.Destination;
import Metaheuristics.NeighborOperators.Exchange;
import Metaheuristics.NeighborOperators.Relocate;
import Metaheuristics.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.Result;
import Metaheuristics.Taboo.Transformation;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.HashSet;

import static Utils.SolutionUtils.distanceBetweenTwoDestination;


/**
 * Cette classe représente les méthodes utiles pour les métaheuristiques
 */
public class MetaheuristicUtils {

    /**
     * Cette méthode sert juste pour faciliter l'implémentation de la méthode Tabou
     * @param solution Solution
     * @return La fitness de la solution
     */
    public static int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    /**
     * Méthode de descente
     * @param x0 Solution initiale
     * @param timeConstraint Contrainte de temps
     * @return Solution
     */
    public static Result hillClimbing(Solution x0, boolean timeConstraint)
    {
        Solution x1 = x0.clone();
        Transformation transformation = null;
        do
        {
            HashSet<Result> candidats = GetAllNeighbors(x0, timeConstraint, 1);


            for(Result voisin : candidats)
            {
                if(voisin == null)
                    continue;
                Solution candidat = voisin.getSolution();
                if(fitness(candidat) == 0)
                    continue;

                if (fitness(candidat) < fitness(x1))
                {
                    x1 = candidat.clone();
                    x0 = x1.clone();
                    transformation = voisin.getTransformation();
                }
            }
        } while ( fitness(x1) < fitness(x0));
        Result result = new Result(x1, transformation);
        return result;
    }

    /**
     * Cette méthode permet de générer un voisin selon les paramètres
     * @param method Méthode de génération du voisin
     * @param solution Solution
     * @param firstClientRoad Première route
     * @param secondClientRoad Deuxième route
     * @param indexClient Index du client
     * @param newIndexClient Nouvel index du client
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return Result
     */
    public static Result getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int indexClient, int newIndexClient,
                                     boolean timeConstraint, int chosenTransformation) {
        Result sol = null;
        switch(method) {
            case 1:
                sol = Exchange.ExchangeIntra(solution,firstClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            case 2:
                sol = Exchange.ExchangeIntra(solution,firstClientRoad, secondClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            case 3:
                sol = Relocate.RelocateIntra(solution, firstClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            case 4:
                sol = Relocate.RelocateInter(solution, firstClientRoad, secondClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            case 5:
                sol = TwoOptAndCrossExchange.runTwoOpt(solution, firstClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            case 6:
                sol = TwoOptAndCrossExchange.runCrossExchange(solution, firstClientRoad, secondClientRoad, indexClient, newIndexClient, timeConstraint, chosenTransformation);
                break;
            default:
                break;
        }
        return sol;
    }

    /**
     * Recherche de tous les voisins intra-routes
     * @param initialSol : solution initiale
     * @param timeConstraint : contrainte de temps
     * @param chosenTransformation : transformation choisie
     * @return : liste de voisins intra-routes
     */
    public static ArrayList<Result> searchAllCandidatesIntra(Solution initialSol, boolean timeConstraint, int chosenTransformation) {
        ArrayList<Result> neighbors = new ArrayList<>();
        int[] intraMethods = {1, 3, 5};
        // Pour chaque méthode de voisinage possible
        for(int method : intraMethods) {
            // Pour chaque route
            for(int roadIndex = 0; roadIndex < initialSol.getRoads().size() ; roadIndex++) {
                // Pour une destination j sauf dépot départ et arrivée
                for (int client1Index = 0; client1Index < initialSol.getARoad(roadIndex).getDestinations().size()-1;
                     client1Index++)
                {
                    // Pour chaque destination l sauf dépot départ et arrivée
                    for (int client2Index = 0; client2Index < initialSol.getARoad(roadIndex).getDestinations()
                            .size()-1; client2Index++) {

                        if(method != 3 && client1Index >= client2Index)
                            continue;
                        
                        // If the two clients are the same, we skip this iteration
                        if(client1Index == client2Index)
                            continue;

                        Result candidats = getNeighbor(method, initialSol, roadIndex, roadIndex, client1Index, 
                                client2Index, timeConstraint, chosenTransformation);
                        
                        if(candidats != null)
                        {
                            neighbors.add(candidats);
                        }
                    }

                }
            }
        }
        return neighbors;
    }

    /**
     * Recherche de tous les voisins inter-routes
     * @param initialSol : solution initiale
     * @param timeConstraint : contrainte de temps
     * @param chosenTransformation : transformation choisie
     * @return : liste de voisins inter-routes
     */
    private static ArrayList<Result> searchAllCandidatesInter(Solution initialSol, boolean timeConstraint, int chosenTransformation) {
        ArrayList<Result> neighbors = new ArrayList<>();
        int[] interMethods = {2, 4, 6};
        // Pour chaque méthode de voisinage possible
        for(int method : interMethods) {
            // Pour chaque route i
            for(int road1Index = 0; road1Index < initialSol.getRoads().size() ; road1Index++) {
                for(int road2Index = road1Index+1; road2Index < initialSol.getRoads().size() ; road2Index++) {
                    if (road1Index == road2Index)
                        continue;
                    // Pour chaque destination
                    for (int client1Index = 0; client1Index < initialSol.getARoad(road1Index).getDestinations().size()-1;
                         client1Index++) {
                        for (int client2Index = 0; client2Index < initialSol.getARoad(road2Index).getDestinations().size();
                             client2Index++) {

                            Result candidats = getNeighbor(method, initialSol, road1Index, road2Index, client1Index,
                                    client2Index, timeConstraint, chosenTransformation);
                            if(candidats != null)
                            {
                                neighbors.add(candidats);
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Récupérer tous les voisins d'une solution
     * @param initialSol : solution initiale
     * @param timeConstraint : contrainte de temps
     * @param chosenTransformation : transformation choisie
     * @return : liste de voisins
     */
    public static HashSet<Result> GetAllNeighbors(Solution initialSol, boolean timeConstraint, int chosenTransformation)
    {
        HashSet<Result> set = new HashSet<>();
        ArrayList<Result> list1 = searchAllCandidatesIntra(initialSol, timeConstraint, chosenTransformation);
        ArrayList<Result> list2 = searchAllCandidatesInter(initialSol, timeConstraint, chosenTransformation);
        set.addAll(list1);
        set.addAll(list2);
        return set;
    }

    /**
     * Récupérer une sous liste d'une liste de destinations
     * @param list : liste de destinations
     * @param i : indice de début
     * @param j : indice de fin
     * @return : sous liste
     */
    public static ArrayList<Destination> getSubList(ArrayList<Destination> list, int i, int j)
    {
        ArrayList<Destination> subList = new ArrayList<>();
        for (int k = i; k <= j; k++)
        {
            subList.add(list.get(k));
        }
        return subList;
    }

    /**
     * Fonction qui permet de vérifier si une route est valide
     * @param candidate : solution candidate
     * @param roadSelected : route sélectionnée
     * @param timeConstraint : contrainte de temps
     * @return : solution valide ou non
     */
    public static Solution verifyIfRoadValid(Solution candidate, int roadSelected, boolean timeConstraint)
    {
        int size = candidate.getARoad(roadSelected).getDestinations().size();
        Road newRoad = candidate.getRoads().get(roadSelected);
        int time = 0;
        int distance = 0;
        int capacityRemained = candidate.getConfig().getTruck().getCapacity();
        int infos[];
        for (int i = 0; i < size-1; i++) {
            Destination departedClient = newRoad.getDestinations().get(i);
            Destination arrivedClient = newRoad.getDestinations().get(i+1);

            int distanceBetweenDepartAndArrive = distanceBetweenTwoDestination(departedClient, arrivedClient);
            if (!SolutionUtils.isClientCanBeDelivered(candidate.getConfig().getCentralDepot(),
                    departedClient, arrivedClient, time, capacityRemained, timeConstraint)) {

                return null;
            }
            else {
                infos = SolutionUtils.calculateInfosUsingDistanceBetween2Dests(arrivedClient,time, distance,
                        distanceBetweenDepartAndArrive, capacityRemained);
                time = infos[0];
                capacityRemained = infos[1];
                distance = infos[2];
            }
        }
        candidate.getRoads().set(roadSelected, newRoad);
        return candidate;
    }

    /**
     * Fonction qui supprime les routes inutiles
     * @param candidate : solution candidate
     * @return : solution candidate sans routes inutiles
     */
    public static Solution removeUselessRoad(Solution candidate)
    {
        for (int i = candidate.getRoads().size()-1; i >= 0; i--) {
            if (candidate.getRoads().get(i).getDestinations().size() <= 2) {
                candidate.removeRoad(i);
            }
        }

        return candidate;
    }

    /**
     * Fonction qui permet de récupérer une route
     * @param candidate : solution candidate
     * @param roadIndex : indice de la route
     * @return : route
     */
    public static ArrayList<String> getExistingRoad(Solution candidate, int roadIndex)
    {
        ArrayList<String> secondList = new ArrayList<>();
        if(candidate.getRoads().get(roadIndex).getDestinations().size() > 2)
        {
            secondList = candidate.getRoads().get(roadIndex).returnListOfIdClient();
        }
        return secondList;
    }
}
