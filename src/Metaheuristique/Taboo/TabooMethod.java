package Metaheuristique.Taboo;


import Metaheuristique.MetaheuristiquesUtils;

import Metaheuristique.Solution;

import java.util.ArrayList;



public class TabooMethod {

    static int tabuSize = 1;

    public static ArrayList<Solution> SolutionWithoutForbidenTransformation(ArrayList<Result> voisins, ArrayList<Transformation> tabooList )
    {
        ArrayList<Solution> cleanVoisins = new ArrayList<>();
        for( Result voisin : voisins)
        {
            if(!tabooList.contains(voisin.getTransformation()))
                cleanVoisins.add(voisin.getSolution());
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
