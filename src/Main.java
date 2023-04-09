import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Solution;
import Utils.SolutionUtils;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration("2");
        System.out.println("Nombre de vehicules minimal : " + config.getNumberOfMinimalVehicles());
        System.out.println("Solution aleatoire generee : ");
        Solution solution = SolutionUtils.generateRandomSolution(config, true);


        System.out.println("Solution initiale : ");
        solution.displaySolution();
        SolutionVisualization.DisplayGraph(config, solution);

        TwoOptAndCrossExchange.generateAllNeighborsCrossExchange(solution);

        //TwoOptAndCrossExchange.generateAllNeighbors2Opt(solution);
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