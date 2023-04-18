import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.NeighboorOperation;
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

    }

}