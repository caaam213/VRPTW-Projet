import Graphics.OrthonormalPlan;
import Logistique.Configuration;
import Metaheuristique.Solution;
import Utils.SolutionUtils;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration("101");
        Solution solution = SolutionUtils.generateRandomSolution(config);
        solution.displaySolution();
        OrthonormalPlan plan = new OrthonormalPlan(solution);
        plan.display();


    }
}