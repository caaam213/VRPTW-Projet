import Graphics.SolutionVisualization;
import Logistique.Configuration;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration("1202");
        System.out.println("Nombre de vehicules minimal : "+config.getNumberOfMinimalVehicles());
        System.out.println("Solution aleatoire generee : ");
        Solution solution = SolutionUtils.generateRandomSolution(config, true);
        solution.displaySolution();
        SolutionVisualization.DisplayGraph(config, solution);
    }
}