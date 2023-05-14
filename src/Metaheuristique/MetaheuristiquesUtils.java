package Metaheuristique;
import Metaheuristique.NeighborOperators.Exchange;
import Metaheuristique.NeighborOperators.Relocate;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.TabooMethod;
import Metaheuristique.Taboo.Transformation;
import java.util.ArrayList;
import java.util.HashSet;


public class MetaheuristiquesUtils {
    public static int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    public static Result methodeDeDesecente(Solution x0, ArrayList<Transformation> tabooList)
    {
        Solution x1 = x0.clone();
        Transformation t1 = null;
        do
        {
            HashSet<Result> voisins = GetAllNeighbors(x0);
            for(Result candidat : voisins)
            {
                if(fitness(candidat.getSolution()) == 0)
                    continue;

                if (fitness(candidat.getSolution()) < fitness(x1))
                {
                    x1 = candidat.getSolution().clone();
                }
            }
        } while ( fitness(x1) >= fitness(x0));
        Result result = new Result(x1);
        return result;
    }

    public static Result getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient) {
        Result sol = null;
        switch(method) {
            case 1:
                System.out.println("Exchange intra");
                sol = Exchange.Exchange(solution,firstClientRoad, newIndexClient, indexClient);
                break;
            case 2:
                System.out.println("Exchange inter");
                sol = Exchange.ExchangeInter(solution,firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 3:
                System.out.println("Relocate intra");
                sol = Relocate.RelocateIntra(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 4:
                sol = Relocate.RelocateInter(solution, firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 5:
                sol = TwoOptAndCrossExchange.runTwoOpt(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 6:
                sol = TwoOptAndCrossExchange.runCrossExchange(solution, firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            default:
                break;
        }
        return sol;
    }

    private static ArrayList<Result> searchAllCandidatesIntra(Solution initialSol) {
        ArrayList<Result> neighbors = new ArrayList<>();
        int[] intraMethods = {1, 3, 5};
        // Pour chaque méthode de voisinage possible
        for(int k : intraMethods) {
            // Pour chaque route
            for(int i = 0; i < initialSol.getRoads().size() ; i++) {
                // Pour une destination j sauf dépot départ et arrivée
                for (int j = 1; j < initialSol.getARoad(i).getDestinations().size()-1; j++)
                {
                    if(initialSol.getARoad(i).getDestinations().size()-2 > 1) {
                        // Pour chaque destination l sauf dépot départ et arrivée
                        for (int l = j+1; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                            Result candidats = getNeighbor(k, initialSol, i, i, l, j);
                            if(candidats != null)
                            {
                                Result candidatValid = candidats;
                                neighbors.add(candidatValid);
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    private static ArrayList<Result> searchAllCandidatesInter(Solution initialSol) {
        ArrayList<Result> neighbors = new ArrayList<>();
        int[] interMethods = {2, 4, 6};
        // Pour chaque méthode de voisinage possible
        for(int k : interMethods) {
            // Pour chaque route i
            for(int i = 0; i < initialSol.getRoads().size() ; i++) {
                for(int m = 0; m < initialSol.getRoads().size() ; m++) {
                    // Pour chaque destination
                    for (int j = 0; j < initialSol.getARoad(i).getDestinations().size()-1; j++) {
                        for (int l = 0; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                            neighbors.add(getNeighbor(k, initialSol, i, i, j,l));
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public static HashSet<Result> GetAllNeighbors(Solution initialSol)
    {
        HashSet<Result> set = new HashSet<>();
        ArrayList<Result> list1 = searchAllCandidatesIntra(initialSol);
        ArrayList<Result> list2 = searchAllCandidatesInter(initialSol);
        set.addAll(list1);
        set.addAll(list2);
        return set;
    }

    public static Solution GetBestNeighbor(Solution initialSol, HashSet<Result> neighbors)
    {
        Solution bestNeighbor = null;
        int bestFitness = initialSol.getTotalDistanceCovered();
        for(Result neighbor : neighbors)
        {
            if(neighbor.getSolution() != null)
            {
                int fitness = fitness(neighbor.getSolution());
                if(fitness < bestFitness)
                {
                    bestFitness = fitness;
                    bestNeighbor = neighbor.getSolution();
                }
            }
        }
        return bestNeighbor;
    }
}
