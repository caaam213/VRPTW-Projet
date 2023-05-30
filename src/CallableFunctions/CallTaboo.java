package CallableFunctions;

import Graphics.SolutionVisualization;
import Logistics.Configuration;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.TabooMethod;
import Utils.SolutionUtils;

public class CallTaboo {

    public static void runTabooWBasicParameter(String numberFile,
                                                   boolean generateVeryRandomSolution,
                                                   boolean timeConstraint,
                                                   int maxIteration,
                                                   int tabooSizeList,
                                                   int chosenTransformation

                                                   )
    {
        Configuration configuration = new Configuration(numberFile);
        SolutionVisualization solutionVisualization = new SolutionVisualization();

        Solution initialSolution = SolutionUtils.generateRandomSolution(configuration, generateVeryRandomSolution,
                timeConstraint);
        solutionVisualization.DisplayGraph(initialSolution, "Solution initiale generee au hasard");

        Solution bestSolution = TabooMethod.TabooSearch(initialSolution, maxIteration, tabooSizeList,
                timeConstraint, chosenTransformation);
        solutionVisualization.DisplayGraph(bestSolution, "Solution generee par l'algo Taboo");

    }
}
