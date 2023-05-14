import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Genetics.Crossover;
import Metaheuristique.Genetics.GeneticMethod;
import Metaheuristique.MetaheuristiquesUtils;
import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Solution;
import Metaheuristique.Taboo.Result;
import Metaheuristique.Taboo.TabooMethod;
import Utils.SolutionUtils;
import java.util.HashSet;
import static Metaheuristique.MetaheuristiquesUtils.GetAllNeighbors;

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
            Configuration config = new Configuration("101");
            System.out.println("Nombre de vehicules minimal : " + config.getNumberOfMinimalVehicles());
            System.out.println("Solution aleatoire generee : ");
            Solution solution = SolutionUtils.generateRandomSolution(config, false);
            System.out.println("Nombre de routes = " + solution.getRoads().size());
            System.out.println("Solution initiale : ");
            solution.displaySolution();

            Solution sol = TabooMethod.TabouSearch(solution.clone());
            SolutionVisualization.DisplayGraph(sol, "Taboo");

            /**
            //Solution sol = GeneticMethod.runGeneticMethod(solution.clone(), 150, 2000, 0.33F, 20);
            if (solution == null)
                System.out.println("Solution null");
            else
            {
                HashSet<Result> res = MetaheuristiquesUtils.GetAllNeighbors(solution);
                System.out.println("Count : " + res.size());
                for(Result r : res)
                {
                    if( r == null) {
                        System.out.println("Solution null");
                        continue;
                    }
                    else {
                        SolutionVisualization.DisplayGraph(r.getSolution(), "Solution");
                    }
                }
                //System.out.println("SOLUTION OPTIMALE TROUVEE :");
                //SolutionVisualization.DisplayGraph(sol, "Crossover");
                //sol.displaySolution();
            }
             **/
            //TwoOptAndCrossExchange.generateAllNeighbors2Opt(solution);
            /*for (int j = 0; j < solution.getRoads().size(); j++)
                for (int m = j+1; m < solution.getRoads().size(); m++)
                    for (int i = 1; i < solution.getARoad(j).getDestinations().size() - 1; i++) {
                        for (int k = 1; k < solution.getARoad(m).getDestinations().size() - 1; k++) {
                        Result voisin = Relocate.RelocateInter(solution, j, m, k, i);
                        if (voisin == null) {
                            System.out.println("Voisin null pour i = " + i + "et la route " + j);
                            continue;
                        } else {
                            System.out.print("Bravo on est à i = " + i + "et la route " + j);
                            for (Destination dest : voisin.getSolution().getARoad(0).getDestinations()) {
                                System.out.println(dest.toString());
                            }
                            for (Edge edge : voisin.getSolution().getARoad(0).getEdges()) {
                                System.out.print(edge.toString());
                            }
                            SolutionVisualization.DisplayGraph(voisin.getSolution(), "Voisin");
                        }
                    }
                }
        }*/
        }
}