package Metaheuristique.Genetics;

import Metaheuristique.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristique.Solution;

/**
 * This class contains the mutation operators and also operators to generate initial solutions
 */
public class Mutation {

    /**
     * Apply 2-opt method to a solution to generate a new solution
     * @param solution : Solution to apply 2-opt method
     * @return new solution
     */
    public static Solution apply2OptMethod(Solution solution)
    {
        int[] roads = SelectRandomElements.selectRandomRoads(solution);
        int road1Index = roads[0];

        int[] edges = SelectRandomElements.selectedRandomEdges(solution, road1Index, road1Index, "2opt");
        int edge1 = edges[0];
        int edge2 = edges[1];

        return TwoOptAndCrossExchange.runTwoOpt(solution, road1Index, edge1, edge2);
    }

    /**
     * Apply cross-exchange method to a solution to generate a new solution
     * @param solution : Solution to apply cross-exchange method
     * @return new solution
     */
    public static Solution applyCrossExchangeMethod(Solution solution)
    {
        int[] roads = SelectRandomElements.selectRandomRoads(solution);
        int road1Index = roads[0];
        int road2Index = roads[1];

        int[] edges = SelectRandomElements.selectedRandomEdges(solution, road1Index, road2Index, "CrossExchange");
        int edge1 = edges[0];
        int edge2 = edges[1];

        return TwoOptAndCrossExchange.runCrossExchange(solution, road1Index, road2Index, edge1, edge2);
    }












}
