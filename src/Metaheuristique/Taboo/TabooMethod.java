package Metaheuristique.Taboo;
import Metaheuristique.NeighborOperators.NeighboorOperation;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Metaheuristique.MetaheuristiquesUtils.AllNeighbors;

public class TabooMethod {

    static ArrayList<Transformation>  tabooList = new ArrayList<Transformation>();
    static int tabuSize = 10;

    public static int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }


    private static ArrayList<Solution> SolutionWithoutForbidenTransformation(ArrayList<HashMap<Solution, Transformation>> voisins, ArrayList<Transformation> tabooList )
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


    public static Solution TabouSearch(Solution initialSol){
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
