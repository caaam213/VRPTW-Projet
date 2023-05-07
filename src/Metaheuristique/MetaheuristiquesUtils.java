package Metaheuristique;

import Metaheuristique.NeighborOperators.NeighboorOperation;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Taboo.Transformation;

import java.util.ArrayList;
import java.util.HashMap;

public class MetaheuristiquesUtils {
    public static HashMap<Solution, Transformation> getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient) {
        HashMap<Solution, Transformation> sol = new HashMap<>();
        switch(method) {
            case 1:
                sol = NeighboorOperation.Exchange(solution,firstClientRoad, newIndexClient, indexClient);
                break;
            case 2:
                sol = NeighboorOperation.ExchangeInter(solution,firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 3:
                sol = NeighboorOperation.RelocateIntra(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 4:
                sol = NeighboorOperation.RelocateInter(solution, firstClientRoad, secondClientRoad, newIndexClient, indexClient);
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

    private static ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesIntra(Solution initialSol) {
        ArrayList<HashMap<Solution, Transformation>> neighbors = new ArrayList<>();
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
                        for (int l = j; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                            if(l == j)
                                continue;
                            else {
                                // getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
                                HashMap<Solution, Transformation> candidats = (HashMap<Solution, Transformation>) getNeighbor(k, initialSol, i, i, l, j);
                                HashMap<Solution, Transformation> candidatValid = new HashMap<>();
                                if(candidats != null)
                                {
                                    candidatValid = (HashMap<Solution, Transformation>) getNeighbor(k, initialSol, i, i, l, j).clone();
                                    neighbors.add(candidatValid);
                                }
                                else
                                    continue;
                            }
                        }
                    }
                    else
                        continue;
                }
            }
        }
        return neighbors;
    }

    private static ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesInter(Solution initialSol) {
        ArrayList<HashMap<Solution, Transformation> > neighbors = new ArrayList<>();
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

    public static ArrayList<HashMap<Solution, Transformation> > AllNeighbors(Solution initialSol)
    {
        ArrayList<HashMap<Solution, Transformation> > list1 = searchAllCandidatesIntra(initialSol);
        //ArrayList<HashMap<Solution, Transformation> > list2 = searchAllCandidatesIntra(initialSol);
        //list1.addAll(list2);
        return list1;
    }
}
