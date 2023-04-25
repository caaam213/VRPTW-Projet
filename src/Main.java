import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Genetics.GeneticMethod;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

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
<<<<<<< HEAD
        Configuration config = new Configuration("111");
        System.out.println("Nombre de vehicules minimal : " + config.getNumberOfMinimalVehicles());
        System.out.println("Solution aleatoire generee : ");
        Solution solution = SolutionUtils.generateRandomSolution(config, true);


        System.out.println("Solution initiale : ");
        solution.displaySolution();
        SolutionVisualization.DisplayGraph(config, solution);

        Solution bestSol = TabooMethod.TabouSearch(solution);
        SolutionVisualization.DisplayGraph(config, bestSol);


        //TwoOptAndCrossExchange.generateAllNeighborsCrossExchange(solution);

        //TwoOptAndCrossExchange.generateAllNeighbors2Opt(solution);
        //solvoisine.displaySolution();
        //TwoOptAndCrossExchange.runCrossExchange(solution, 2, 1, 4,0);

        //TwoOpt.generateAllNeighborsCrossExchange(solution);
        //TwoOptAndCrossExchange.generateAllNeighbors2Opt(solution);
        /*for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Solution voisine = NeighboorOperation.RelocateIntra(solution, 0, i, 2);
        }

        for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Solution voisin = NeighboorOperation.Exchange(solution, 0, i, 2);
            for(Destination dest : voisin.getARoad(0).getDestinations())
            {
                System.out.println(dest.toString());
            }
            for (Edge edge : voisin.getARoad(0).getEdges())
            {
                System.out.print(edge.toString());
            }
            SolutionVisualization.DisplayGraph(solution.getConfig(), voisin);
        }
         */

        /*for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Solution voisin = NeighboorOperation.RelocateIntra(solution, 0, i, 2);
        }*/
=======
        long startTime = System.nanoTime();
        // Write code
        Configuration config = new Configuration("111");
        Solution solution = SolutionUtils.generateRandomSolution(config, false);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        displayExecutionTime(duration);


>>>>>>> 6b27ceda0c15d3efc49dfc0f23cf1a1ac756ef44

    }

}