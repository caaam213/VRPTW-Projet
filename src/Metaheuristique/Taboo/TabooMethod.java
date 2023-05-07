package Metaheuristique.Taboo;


import Metaheuristique.MetaheuristiquesUtils;
import Metaheuristique.NeighborOperators.Exchange;
import Metaheuristique.NeighborOperators.Relocate;

import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TabooMethod {

    static int tabuSize = 10;



    public static ArrayList<Solution> SolutionWithoutForbidenTransformation(ArrayList<HashMap<Solution, Transformation>> voisins, ArrayList<Transformation> tabooList)
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


    public static Solution TabouSearch(Solution x0){
        Solution xmin = x0.clone();
        int fmin = MetaheuristiquesUtils.fitness(xmin);
        int maxIter = 1000;
        ArrayList<Transformation> tabooList = new ArrayList<>();
        Solution xi = x0.clone();
        Result xi1 = new Result();
        for ( int i =0; i< maxIter; i++)
        {
            xi1 = MetaheuristiquesUtils.methodeDeDesecente(xi, tabooList);
            int lambdaF = MetaheuristiquesUtils.fitness(xi1.solution) - MetaheuristiquesUtils.fitness(xi);
            if(lambdaF>=0){
                tabooList.add(xi1.transformation);
            }

            if(MetaheuristiquesUtils.fitness(xi1.solution) < MetaheuristiquesUtils.fitness(xmin))
            {
                xmin = xi1.solution.clone();
                fmin = MetaheuristiquesUtils.fitness(xmin);
            }

            // On retire le dernier élément ajouté si la liste est pleine
            if (tabooList.size() > tabuSize) {
                tabooList.remove(0);
            }

            xi = xi1.solution.clone();
        }
        return xmin;
    }
}
