import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Logistique.Destination;
import Metaheuristique.Edge;
import Metaheuristique.Solution;
import Metaheuristique.NeighborOperators.Exchange;
import Metaheuristique.Taboo.Result;
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
        long startTime = System.nanoTime();
        // Write code
        Configuration config = new Configuration("2");
        Solution solution = SolutionUtils.generateRandomSolution(config, true);

        System.out.println("Solution initiale : ");
        solution.displaySolution();
        SolutionVisualization.DisplayGraph(solution, "Initial");

        for (int i = 1; i < solution.getARoad(0).getDestinations().size()-1 ; i++) {
            Result voisin = Exchange.ExchangeIntra(solution, 0, i, 2);
            for(Destination dest : voisin.getSolution().getARoad(0).getDestinations())
            {
                System.out.println(dest.toString());
            }
            for (Edge edge : voisin.getSolution().getARoad(0).getEdges())
            {
                System.out.print(edge.toString());
            }
            SolutionVisualization.DisplayGraph(voisin.getSolution(), "Exchange");
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        displayExecutionTime(duration);
    }
}