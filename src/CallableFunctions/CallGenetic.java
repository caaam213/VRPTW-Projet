package CallableFunctions;

import Graphics.SolutionVisualization;
import Logistics.Configuration;
import Metaheuristics.Genetics.GeneticMethod;
import Metaheuristics.Solution;
import Utils.SolutionUtils;

public class CallGenetic {

    /**
     * @param numberFile : numéro du fichier à utiliser
     * @param generateVeryRandomSolution : si vrai, la solution initiale sera générée de manière très aléatoire
     * @param timeConstraint : si vrai, on utilise la contrainte de temps
     * @param populationSize : taille de la population
     * @param maxGeneration : nombre maximum de générations
     * @param mutationRate : taux de mutation
     * @param nbBest : nombre de meilleurs individus à garder lors de la sélection
     * @param useMutationHillClimbing : si vrai, on utilise la méthode de descente dans la mutation
     * @param useCrossoverHillClimbing : si vrai, on utilise la méthode de descente dans le crossover
     * @param bestSolutionReproDrastic : si vrai, on ne prend que des solutions distinctes pour la reproduction
     */
    public static void runGenetic(String numberFile,boolean generateVeryRandomSolution,
                                                     boolean timeConstraint, int populationSize, int maxGeneration, float mutationRate,
                                      int nbBest,boolean useMutationHillClimbing,boolean useCrossoverHillClimbing, boolean bestSolutionReproDrastic)
    {
        Configuration configuration = new Configuration(numberFile);
        SolutionVisualization solutionVisualization = new SolutionVisualization();

        Solution initialSolution = SolutionUtils.generateRandomSolution(configuration, generateVeryRandomSolution,
                timeConstraint);
        solutionVisualization.DisplayGraph(initialSolution, "Solution initiale generée au hasard");
        Solution bestSolution = GeneticMethod.runGeneticMethod(initialSolution, populationSize, maxGeneration, mutationRate,
                nbBest, useMutationHillClimbing,useCrossoverHillClimbing, bestSolutionReproDrastic, timeConstraint);
        new SolutionVisualization().DisplayGraph(bestSolution, "Solution generee par l'algo genetique");

    }


}
