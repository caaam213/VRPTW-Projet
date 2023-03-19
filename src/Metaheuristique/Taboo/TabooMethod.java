package Metaheuristique.Taboo;
import Logistique.Client;
import Logistique.Destination;
import Metaheuristique.Solution;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Transformation;

import java.util.ArrayList;

public class TabooMethod {

    public int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    public ArrayList<Solution> getNeighbors(Solution sol) {
        return new ArrayList<Solution>();
    }

    public Solution TabouSearch(Solution initialSol){

        Solution bestSolution = initialSol;
        int bestDistance = fitness(initialSol);

        int i = 0;
        int maxIter = 1000;

        ArrayList<Solution> tabooList = new ArrayList<Solution>();
        int tabuSize = 10;

        for ( int j =0; j< maxIter; j++)
        {
            ArrayList<Solution> voisins = getNeighbors(bestSolution);
            Solution bestCandidate = bestSolution;
            for(Solution voisin : voisins)
            {
                if (tabooList.contains(voisin) != true && fitness(voisin) < fitness(bestCandidate))
                {
                    bestCandidate = voisin;
                }
                voisins.remove(voisin);
            }

            if(fitness(bestCandidate) < fitness(bestSolution))
            {
                bestSolution = bestCandidate;
                bestDistance = fitness(bestSolution);
            }

            tabooList.add(bestCandidate);
            if (tabooList.size() > tabuSize) {
                tabooList.remove(0);
            }
        }
        return bestSolution;
    }
}
