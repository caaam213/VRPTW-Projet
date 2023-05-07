package Metaheuristique;
import Metaheuristique.NeighborOperators.Exchange;
import Metaheuristique.NeighborOperators.Relocate;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.TabooMethod;
import Metaheuristique.Taboo.Transformation;
import java.util.ArrayList;


public class MetaheuristiquesUtils {
    public static int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    public static Result methodeDeDesecente(Solution x0, ArrayList<Transformation> tabooList)
    {
        int i = -1;
        Solution xi = x0.clone();
        Solution xi1 = new Solution();
        ArrayList<Result> voisinsXi1 = new ArrayList<Result>();
        do
        {
            ArrayList<Result> voisins = AllNeighbors(xi);
            ArrayList<Solution> candidats = TabooMethod.SolutionWithoutForbidenTransformation(voisins, tabooList);
            for(Solution candidat : candidats)
            {
                if (fitness(candidat) < fitness(xi1))
                {
                    xi1 = candidat.clone();
                    voisinsXi1 = voisins;
                }
            }
            xi = xi1;
            i = i + 1;
        } while ( fitness(xi1) < fitness(xi));
        Transformation t = null;
        for( Result voisin : voisinsXi1)
        {
            if( xi1 == voisin.getSolution())
                t = voisin.getTransformation();

        }
        Result result = new Result(xi1, t);
        return result;
    }

    public static Result getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient) {
        Result sol;
        switch(method) {
            case 1:
                sol = Exchange.Exchange(solution,firstClientRoad, newIndexClient, indexClient);
                break;
            case 2:
                sol = Exchange.ExchangeInter(solution,firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 3:
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
                sol = null;
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
                            // getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
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

    public static ArrayList<Result> AllNeighbors(Solution initialSol)
    {
        ArrayList<Result> list1 = searchAllCandidatesIntra(initialSol);
        ArrayList<Result> list2 = searchAllCandidatesInter(initialSol);
        list1.addAll(list2);
        return list1;
    }
}
