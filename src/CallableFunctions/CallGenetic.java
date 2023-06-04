package CallableFunctions;

import Graphics.SolutionVisualization;
import Logistics.Configuration;
import Metaheuristics.Genetics.GeneticMethod;
import Metaheuristics.Solution;
import Utils.SolutionUtils;

public class CallGenetic {

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
