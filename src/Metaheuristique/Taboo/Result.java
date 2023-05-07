package Metaheuristique.Taboo;
import Metaheuristique.Solution;

public class Result {

    Solution solution;
    Transformation transformation;

    public Result() {
    }

    public Result(Solution solution, Transformation transformation) {
        this.solution = solution;
        this.transformation = transformation;
    }

    public Solution getSolution() {
        return solution;
    }

    public Transformation getTransformation() {
        return transformation;
    }

}
