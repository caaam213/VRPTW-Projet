package Metaheuristique.Taboo;
import Graphics.SolutionVisualization;
import Logistique.Client;
import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.NeighboorOperation;
import Metaheuristique.NeighborOperators.TwoOpt;
import Metaheuristique.Solution;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabooMethod {

    ArrayList<Transformation> tabooList = new ArrayList<Transformation>();
    int tabuSize = 10;

    public int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    public HashMap<Solution, Transformation> getNeighbor(int choice, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient) {
        HashMap<Solution, Transformation> sol = new HashMap<>();
        switch(choice) {
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
                //sol = TwoOpt.runTwoOptInter(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 6:
                //sol = TwoOpt.runTwoOptIntra();
                break;
            default:
                sol = null;
                break;
        }
        return sol;
    }

    private ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesIntra(Solution initialSol) {
        ArrayList<HashMap<Solution, Transformation> > neighbors = new ArrayList<>();
        int[] intraMethods = {1, 3, 5};
        // Pour chaque méthode de voisinage possible
        for(int k : intraMethods) {
            // Pour chaque route
            for(int i = 0; i < initialSol.getRoads().size() ; i++) {
                // Pour une destination j
                for (int j = 0; j < initialSol.getARoad(i).getDestinations().size()-1; j++) {
                    // Pour chaque destination on essaie de le mettre au nouvel index l
                    for (int l = 0; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                        neighbors.add(getNeighbor(k, initialSol, i, i, l, j));
                    }
                }
            }
        }
        return neighbors;
    }

    private ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesInter(Solution initialSol) {
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

    private ArrayList<HashMap<Solution, Transformation> > AllNeighbors(Solution initialSol)
    {
        ArrayList<HashMap<Solution, Transformation> > list1 = searchAllCandidatesIntra(initialSol);
        ArrayList<HashMap<Solution, Transformation> > list2 = searchAllCandidatesIntra(initialSol);
        list1.addAll(list2);
        return list1;
    }

    private ArrayList<Solution> SolutionWithoutForbidenTransformation(ArrayList<HashMap<Solution, Transformation>> voisins, ArrayList<Transformation> tabooList )
    {
        ArrayList<Solution> cleanVoisins = new ArrayList<>();
        for( HashMap<Solution, Transformation> voisin : voisins)
        {
            for (Map.Entry mapentry  : voisin.entrySet())
            {
                if(!tabooList.contains((Transformation)mapentry.getValue()))
                    cleanVoisins.add((Solution)mapentry.getKey());
            }
        }
        return cleanVoisins;
    }


    public Solution TabouSearch(Solution initialSol){
        // Xmin
        Solution bestSolution = initialSol.clone();
        // Fmin <- F(X0)
        int bestDistance = fitness(bestSolution);
        int maxIter = 1000;
        // Xi
        Solution currentX = initialSol.clone();
        // Xi+1
        Solution nextX = initialSol.clone();
        for ( int i =0; i< maxIter; i++)
        {
            ArrayList<HashMap<Solution, Transformation>> voisins = AllNeighbors(currentX);
            ArrayList<Solution> candidats = SolutionWithoutForbidenTransformation(voisins, tabooList);
            nextX = candidats.get(0);
            // Xi+1 celui qui a la meilleure fitness parmi les candidats
            for(Solution currentcandidat : candidats)
            {
                if (fitness(currentcandidat) < fitness(nextX))
                {
                    nextX = currentcandidat.clone();
                }
            }
            // F = F(Xi+1) - F(Xi)
            int lembdaF = fitness(nextX) - fitness(currentX);
            Transformation t = null;
            if(lembdaF>=0){
                for( HashMap<Solution, Transformation> voisin : voisins)
                {
                    for (Map.Entry mapentry  : voisin.entrySet())
                    {
                        if( nextX == (Solution)mapentry.getKey())
                             t = (Transformation) mapentry.getValue();
                    }
                }
                tabooList.add(t);
            }

            if(fitness(nextX) < fitness(bestSolution))
            {
                // Xmin <- Xi+1
                bestSolution = nextX;
                // Fmin <- F(Xi+1)
                bestDistance = fitness(nextX);
            }

            // On retire le dernier élément ajouté
            if (tabooList.size() > tabuSize) {
                tabooList.remove(0);
            }

            currentX = nextX;
        }
        return bestSolution;
    }
}
