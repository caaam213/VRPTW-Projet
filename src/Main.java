import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Genetics.GeneticMethod;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import Utils.SolutionUtils;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;

import java.util.ArrayList;

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

        Configuration config = new Configuration("111");
        System.out.println("Nombre de vehicules minimal : " + config.getNumberOfMinimalVehicles());
        System.out.println("Solution aleatoire generee : ");
        Solution solution = SolutionUtils.generateRandomSolution(config, false);
        //solution.displaySolution();


        System.out.println("Solution initiale : ");
        //solution.displaySolution();
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(solution);
        /*Solution voisin = solution.clone();
        Road x = voisin.getRoads().get(0);
        Road x1 = voisin.getRoads().get(1);
        voisin.getRoads().get(0).getDestinations().add(2,x1.getDestinations().get(1));
        System.out.println(SolutionUtils.verifyIfTheSolutionIsInList(solutions, voisin));*/

        Solution best = GeneticMethod.runGeneticMethod(solution, 150, 200, 0.33F, 10);
        SolutionUtils.verifyIfAClientAppearsTwoTimesInARoad(best, "Verification de la solution finale done");
        SolutionUtils.verifyIfAclientIsNotDelivered(best, "Verification de la solution initiale done");
        //SolutionVisualization.DisplayGraph(best, "Solution finale");

        //best.displaySolution();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        displayExecutionTime(duration);


        /*for (int i =0; i<100; i++)
        {
            TwoOptAndCrossExchange.generateAllNeighborsCrossExchange(solution);

        }*/

        //TwoOptAndCrossExchange.generateAllNeighborsCrossExchange(solution);
        //solvoisine.displaySolution();
        //TwoOptAndCrossExchange.runCrossExchange(solution, 2, 1, 4,0);

        //TwoOpt.generateAllNeighborsCrossExchange(solution);
        //TwoOptAndCrossExchange.generateAllNeighbors2Opt(solution);
        /*for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Solution voisine = NeighboorOperation.RelocateIntra(solution, 0, i, 2);
        }*/

        //TwoOpt.generateAllNeighbors(solution);
        /*for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Solution voisin = NeighboorOperation.RelocateIntra(solution, 0, i, 2);
        }*/

    }

}