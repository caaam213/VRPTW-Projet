package Metaheuristics.Taboo;
import Metaheuristics.Solution;

import java.util.Objects;

/**
 * Cette classe représente une association entre une solution et une transformation
 */
public class Result {

    Solution solution;
    Transformation transformation;

    /**
     * Constructeur de la classe
     * @param solution Solution
     * @param transformation Transformation
     */
    public Result(Solution solution, Transformation transformation) {
        this.solution = solution;
        this.transformation = transformation;
    }


    /**
     * @return La solution
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * @return La transformation
     */
    public Transformation getTransformation() {
        return transformation;
    }

    /**
     * @param solution La solution à modifier
     */
    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transformation);
    }
}
