package Metaheuristique.Taboo;


import Metaheuristique.MetaheuristiquesUtils;

import Metaheuristique.Solution;

import java.util.ArrayList;
import java.util.HashSet;

import static Metaheuristique.MetaheuristiquesUtils.fitness;
import static Metaheuristique.MetaheuristiquesUtils.methodeDeDesecente;


public class TabooMethod {

    static int tabuSize = 1;

    public static ArrayList<Solution> SolutionWithoutForbidenTransformation(HashSet<Result> voisins, ArrayList<Transformation> tabooList )
    {
        ArrayList<Solution> cleanVoisins = new ArrayList<>();
        for( Result voisin : voisins)
        {
            if( voisin == null)
                continue;
            if(!tabooList.contains(voisin.getTransformation()))
                cleanVoisins.add(voisin.getSolution());
        }
        return cleanVoisins;
    }

    public static Solution TabouSearch(Solution x0) {
        if (x0 == null) {
            return null;
        }
        Solution bestSolution  = x0.clone();
        int fmin = fitness(bestSolution );
        int maxIter = 30;
        ArrayList<Transformation> tabooList = new ArrayList<>();
        Solution currentSolution  = x0.clone();
        Transformation transformation = null;
        for (int i = 0; i < maxIter; i++) {
            // Récupération des voisins de la solution courante
            HashSet<Result> neighbors = MetaheuristiquesUtils.GetAllNeighbors(currentSolution );

            // Filtrage des voisins interdits par la liste tabou
            ArrayList<Solution> filteredNeighbors = new ArrayList<Solution>();
            ArrayList<Result> neighborsSolutions = new ArrayList<Result>(neighbors);
            for (Result neighbor : neighborsSolutions) {
                if (!tabooList.contains(neighbor.getSolution())) {
                    filteredNeighbors.add(neighbor.getSolution());
                }
            }

            // Recherche de la meilleure solution voisine
            Solution bestNeighbor = null;

            for (Solution neighbor : filteredNeighbors) {
                if (bestNeighbor == null || fitness(neighbor) < fitness(bestNeighbor)) {
                    bestNeighbor = neighbor;
                }
            }

            // Mise à jour de la solution courante et de la meilleure solution
            currentSolution = bestNeighbor;
            if (fitness(bestNeighbor) < fitness(bestSolution)) {
                bestSolution = bestNeighbor;
            }

            for (Result neighbor : neighborsSolutions) {
                if( neighbor.getSolution().equals(currentSolution)) {
                    transformation = neighbor.getTransformation();
                }
            }
            // Ajout de la solution courante à la liste tabou
            tabooList.add(transformation);
            if (tabooList.size() > tabuSize) {
                tabooList.remove(0);
            }
        }
        return bestSolution ;
    }
}
