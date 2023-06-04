package Metaheuristics.Taboo;


import Graphics.SolutionVisualization;
import Utils.MetaheuristicUtils;

import Metaheuristics.Solution;

import java.util.ArrayList;
import java.util.HashSet;

import static Utils.MetaheuristicUtils.fitness;


/**
 * Cette classe contient les méthodes de l'algorithme Tabou
 */
public class TabooMethod {


    /**
     * Cette méthode permet d'appliquer l'algorithme Tabou
     * @param x0 Solution initiale
     * @param maxIter Nombre d'itérations
     * @param tabuSize Taille de la liste tabou
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return Solution
     */
    public static Solution TabooSearch(Solution x0, int maxIter, int tabuSize, boolean timeConstraint, int chosenTransformation) {
        if (x0 == null) {
            return null;
        }

        // Visualisation de la solution
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(x0, "Solution");


        // Affecter la solution initiale à la solution courante
        Solution xmin  = x0.clone();
        int fmin = fitness(x0);

        ArrayList<Transformation> tabooList = new ArrayList<>();
        Transformation transformation;
        Solution xi = x0.clone();

        for (int i = 0; i < maxIter; i++) {
            Solution bestNeighbor;
            System.out.println("Fitness de la solution courante : " + fitness(xi));

            // Récupération des voisins
            HashSet<Result> neighborsSolutions = MetaheuristicUtils.GetAllNeighbors(xi, timeConstraint, chosenTransformation);

            ArrayList<Result> filteredNeighbors = new ArrayList<>();

            // Récupérer les voisins qui ne sont pas dans la liste Taboo
            for (Result neighbor : neighborsSolutions) {
                if (neighbor.getTransformation()==null)
                    continue;
                boolean isTaboo = false;

                // vérifier si le voisin est dans la liste taboo
                for (Transformation taboo : tabooList) {

                    // Eviter les transformations qui annulent la précédente
                    if (taboo.equals(neighbor.getTransformation().getTransformationInverse())) {
                        isTaboo = true;
                        break;
                    }

                    if (taboo.equals(neighbor.getTransformation())) {
                        isTaboo = true;
                        break;
                    }
                }

                if (!isTaboo) {
                    filteredNeighbors.add(neighbor);
                }
            }


            // Recherche de la meilleure solution voisine
            bestNeighbor = null;
            transformation = null;
            int fMinNeighbors = Integer.MAX_VALUE;
            ArrayList<Result> equalNeighbors = new ArrayList<>();



            for (Result neighbor : filteredNeighbors) {
                // Si le voisin a une meilleure fitness que la solution courante
                if (fitness(neighbor.getSolution()) < fMinNeighbors) {

                    // On remplace les variables
                    bestNeighbor = neighbor.getSolution();
                    fMinNeighbors = fitness(bestNeighbor);
                    transformation = neighbor.getTransformation();
                    equalNeighbors.clear();
                    equalNeighbors.add(neighbor);

                }
                else if(fitness(neighbor.getSolution()) == fMinNeighbors)
                {
                    equalNeighbors.add(neighbor);
                }

            }

            // Selection du meilleur voisin si la liste des voisins égaux n'est pas vide
            if(equalNeighbors.size() > 1)
            {
                int randomIndex = (int) (Math.random() * equalNeighbors.size());
                bestNeighbor = equalNeighbors.get(randomIndex).getSolution();
                transformation = equalNeighbors.get(randomIndex).getTransformation();
            }



            if (bestNeighbor == null) {
                System.out.println("Pas de meilleur voisin");
                break;
            }

            // Si la fitness du voisin est pire que la fitness de la solution courante, on ajoute la transformation inverse
            if (fMinNeighbors-fitness(xi)>=0) {
                if (transformation!=null)
                {
                    tabooList.add(transformation.getTransformationInverse());
                }
                if (tabooList.size() > tabuSize) {
                    tabooList.remove(0);
                }
            }

            System.out.println("Transformation du meilleur voisin : " + transformation.toString());
            System.out.println("Fitness du meilleur voisin : " + bestNeighbor.getTotalDistanceCovered());


            // On met à jour la meilleure solution si la fitness du voisin est meilleure
            xi = bestNeighbor.clone();
            if (fitness(xi) < fmin) {
                xmin = xi.clone();
                fmin = fitness(xi);
            }


            // On met à jour la visualisation
            solutionVisualization.updateGraphNode(xmin);

            // On affiche les résultats
            System.out.println("Fitness initiale : "+ x0.getTotalDistanceCovered());
            System.out.println("Fitness bestNeighbor : "+bestNeighbor.getTotalDistanceCovered());
            System.out.println("Nombre de vehicules initiaux : "+x0.getRoads().size());
            System.out.println("Pour l'iteration "+i+" la meilleure solution a une fitness de "+
                    xmin.getTotalDistanceCovered());
            System.out.println("Pour l'iteration "+i+" le nombre de vehicules est de "+xmin.getRoads().size());
            System.out.println();
        }
        return xmin ;
    }
}
