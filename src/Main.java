import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Genetics.GeneticMethod;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

import static Metaheuristique.Taboo.TabooMethod.TabouSearch;

public class Main {
    public static void displayExecutionTime(long duration)
    {
        // Convert time
        int heures = (int) (duration / 3600000000000L);
        int minutes = (int) (duration / 60000000000L) % 60;
        int secondes = (int) (duration / 1000000000L) % 60;

        // Display time
        System.out.printf("Temps d'execution : %d heures, %d minutes, %d secondes", heures, minutes, secondes);
        System.out.println();
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        // Write code
        Configuration config = new Configuration("111");
        Solution solution = SolutionUtils.generateRandomSolution(config, false);
        SolutionVisualization.DisplayGraph(solution, "Initial");
        Solution solution2 = TabouSearch(solution);
        SolutionVisualization.DisplayGraph(solution2, "Tabou");
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        displayExecutionTime(duration);



    }

}