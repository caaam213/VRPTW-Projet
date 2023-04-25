package Metaheuristique.Genetics;

import Graphics.SolutionVisualization;
import Metaheuristique.Solution;
import Utils.MapUtil;
import Utils.SolutionUtils;

import java.util.*;


public class GeneticMethod {


    /**
     * Apply genetic method to a solution to generate a new solution
     * @param solution : Solution to apply genetic method
     * @param method : Method to apply
     * @return new solution
     */
    private static Solution applyMethod(Solution solution, String method) {

        Solution generatedSolution;
        switch (method) {
            case "2opt":
                //System.out.println("2opt");
                generatedSolution =  Mutation.apply2OptMethod(solution);
                if (generatedSolution == null)
                    return null;
                //SolutionUtils.verifyIfAClientAppearsTwoTimesInARoad(generatedSolution, "2opt done");

            default:
                //System.out.println("CrossExchange");
                generatedSolution =  Mutation.applyCrossExchangeMethod(solution);
                if (generatedSolution == null)
                    return null;
                //SolutionUtils.verifyIfAClientAppearsTwoTimesInARoad(generatedSolution, "CrossExchange done");
        }
        return generatedSolution;
    }

    /**
     * Apply genetic method to a solution to generate a new solution
     * @param solution : Solution to apply genetic method
     * @param populationSize : Size of the population
     * @return new solution
     */
    private ArrayList<Solution> createInitialPopulation(Solution solution, int populationSize) {
        ArrayList<Solution> population = new ArrayList<>();
        while (population.size() < populationSize) {

            Solution neighborGenerated = applyMethod(solution.clone(),  SelectRandomElements.selectNeighborOperator());
            if (neighborGenerated != null)
            {
                //System.out.println("Distance parcourue: " + neighborGenerated.getTotalDistanceCovered() + "\n");
                population.add(neighborGenerated);
            }
        }
        System.out.println("Fin de la generation de la population initiale");
        return population;
    }

    /**
     * Select the best solution from a population
     * @param population : Population to select the best solution
     * @return best solution
     */
    private static Solution selectBestSolution(ArrayList<Solution> population) {
        Solution bestSolution = null;
        int bestDistance = Integer.MAX_VALUE;
        for (Solution solution : population) {
            if (solution.getTotalDistanceCovered() < bestDistance) {
                bestDistance = solution.getTotalDistanceCovered();
                bestSolution = solution;
            }
        }
        return bestSolution;
    }


    /**
     * Reverse the probability to get the solution with the lower fitness as the higher probability
     * @param solutionsWProbabilities : Solutions with their probabilities
     * @return solutions with their probabilities reversed
     */
    private static LinkedHashMap<Solution,Float> reverseProbabilities(LinkedHashMap<Solution,Float> solutionsWProbabilities)
    {
        LinkedHashMap<Solution,Float> solutionsWProbabilitiesSorted =
                (LinkedHashMap<Solution, Float>) MapUtil.sortByValue(solutionsWProbabilities);


        LinkedHashMap<Solution,Float> reversedSolutionsWProbabilities = new LinkedHashMap<>();

        Set<Solution> keySet = solutionsWProbabilitiesSorted.keySet();

        for(int i=0; i<keySet.size(); i++)
        {
            reversedSolutionsWProbabilities.put((Solution) keySet.toArray()[i], solutionsWProbabilitiesSorted.get(keySet.toArray()[keySet.size()-1-i]));
        }

        return reversedSolutionsWProbabilities;
    }

    public static int getSumFitness(ArrayList<Solution> population)
    {
        int sumFitness = 0;
        for (Solution solution : population) {
            sumFitness += solution.getTotalDistanceCovered();
        }
        return sumFitness;
    }

    /**
     * Select solutions from a population using the roulette wheel selection
     * @param population : Population to select the solutions
     * @param populationSize : Size of the population
     * @return solutions selected
     */
    private static LinkedHashMap<Solution, Integer> rouletteWheelSelection(ArrayList<Solution> population, int totalDistance, int populationSize) {
        ArrayList<Solution> solutionSelected = new ArrayList<>();
        LinkedHashMap<Solution, Integer> newPopulation = new LinkedHashMap<>();
        LinkedHashMap<Solution, Float> solutionsWProbabilities = new LinkedHashMap<>();

        //Create a probability tab based on the probability of each solution
        for (Solution solutionPop : population) {

            float probability = solutionPop.getTotalDistanceCovered() / (float) totalDistance;
            probability = Math.round(probability * 100000) / 100000.0f;
            solutionsWProbabilities.put(solutionPop, probability);


        }
        solutionsWProbabilities = reverseProbabilities(solutionsWProbabilities); //Reverse probabilities
        MapUtil.sortByValue(solutionsWProbabilities);

        for(Map.Entry<Solution, Float> entry : solutionsWProbabilities.entrySet())
        {
            //System.out.println("Distance : "+entry.getKey().getTotalDistanceCovered());
            //System.out.println("Probabilite : "+entry.getValue());
            for (float i = 0; i < entry.getValue(); i+=0.01) {
                solutionSelected.add(entry.getKey());
            }
        }

        //Turn the wheel
        for (int i = 0; i < populationSize; i++) {
            int randomIndex = new Random().nextInt(solutionSelected.size());
            //System.out.println("Fitness de la solution selectionnee par la roue : " + solutionSelected.get(randomIndex).getTotalDistanceCovered());
            newPopulation.put(solutionSelected.get(randomIndex), solutionSelected.get(randomIndex).getTotalDistanceCovered());
        }

        return newPopulation;
    }

    private static ArrayList<Solution> bestSolutionsReproduction(ArrayList<Solution> solutionList, int nbBest) {
        ArrayList<Solution> newPopulation = new ArrayList<>();
        ArrayList<Integer> idsSolutionAlreadySelected = new ArrayList<>();

        for (Solution solution : solutionList) {
            if (!idsSolutionAlreadySelected.contains(solution.getIdSolutionCreated())) {
                newPopulation.add(solution);
                idsSolutionAlreadySelected.add(solution.getIdSolutionCreated());
            }

            if (newPopulation.size() == nbBest)
                break;
        }

        return newPopulation;


        /*if (populationSelectedByRoulette.size() < populationSize) {
            //System.out.println("La taille de la population selectionnee par la roue est inferieure a la taille de la population");
            for (int i = 0; i < populationSelectedByRoulette.size(); i++) {
                newPopulation.add((Solution) populationSelectedByRoulette.keySet().toArray()[i]);

            }
        }
        else
        {
            for (int i = 0; i < populationSize; i++) {
                newPopulation.add((Solution) populationSelectedByRoulette.keySet().toArray()[i]);
            }
        }*/
    }

    /**
     * Apply genetic method to a solution to find the best solution
     * @param solution : Solution to apply genetic method
     * @param populationSize : Size of the population
     * @param maxGeneration : Max iteration
     * @param mutationRate : Mutation rate
     * @param nbBest : Number of best solutions to select
     * @return best solution
     */
    public static Solution runGeneticMethod(Solution solution, int populationSize, int maxGeneration, float mutationRate,
                                            int nbBest) {
        SolutionVisualization.DisplayGraph(solution, "Solution initiale generee");
        // Generate initial population
        ArrayList<Solution> formerPopulation = new GeneticMethod().createInitialPopulation(solution.clone(),
                populationSize);
        formerPopulation.add(solution);
        Solution bestKnown = selectBestSolution(formerPopulation);

        LinkedHashMap<Solution, Integer> populationSelectedByRoulette;
        ArrayList<Solution> populationk;
        for(int k=0; k<maxGeneration; k++)
        {
            System.out.println("Iteration numero "+k);
            int totalFitness = getSumFitness(formerPopulation);
            //Turn the wheel and get the NbBest
            populationSelectedByRoulette = rouletteWheelSelection(formerPopulation,totalFitness, populationSize);
            populationSelectedByRoulette = (LinkedHashMap<Solution, Integer>) MapUtil.
                    sortByValue(populationSelectedByRoulette);
            formerPopulation = new ArrayList<>(populationSelectedByRoulette.keySet());
            populationk = bestSolutionsReproduction(formerPopulation, nbBest);
            /*for (int i = 0; i < populationk.size(); i++) {
                System.out.println("Fitness de la solution elitiste numero "+i+" est : " + populationk.get(i).getTotalDistanceCovered());
            }*/
            int nbBestSolutionFound = populationk.size();
            int numberSolutionToGenerate = populationSize - nbBestSolutionFound;
            int i = 0; //Counter to generate the number of solution to generate
            while (i != numberSolutionToGenerate)
            {
                //System.out.println("Crossover ou mutation iteration numero "+i);
                // Mutation part
                if (new Random().nextFloat() < mutationRate || populationk.size() <=1)
                {
                    //System.out.println("Mutation selectionnee");
                    Solution neighborGenerated = null;
                    int indexSolutionSelected = new Random().nextInt(formerPopulation.size());
                    Solution solutionSelected = formerPopulation.get(indexSolutionSelected).clone();
                    while(neighborGenerated == null)
                    {
                        neighborGenerated = applyMethod(solutionSelected.clone(),
                                SelectRandomElements.selectNeighborOperator());
                    }
                    i++;
                    if (!SolutionUtils.verifyIfTheSolutionIsInList(populationk, neighborGenerated))
                        populationk.add(neighborGenerated);


                }
                else
                {
                    //System.out.println("Crossover selectionne");

                    Solution solutionGeneratedByCrossover = null;
                    int counter = 0;

                    while(solutionGeneratedByCrossover == null && counter < 2*populationSize)
                    {
                        int solution1Index = new Random().nextInt(formerPopulation.size());
                        Solution solution1 = formerPopulation.get(solution1Index).clone();
                        int solution2Index = new Random().nextInt(formerPopulation.size());
                        while(solution2Index == solution1Index)
                        {
                            solution2Index = new Random().nextInt(formerPopulation.size());
                        }
                        Solution solution2 = formerPopulation.get(solution2Index).clone();


                        if(new Random().nextInt(2) == 0)
                        {
                            //System.out.println("Crossover RBX selectionne");
                            solutionGeneratedByCrossover = Crossover.crossOverRBX(solution1,solution2);

                        }
                        else
                        {
                            //System.out.println("Crossover SBX selectionne");
                            //solutionGeneratedByCrossover = Crossover.crossOverSBX(solution1,solution2);
                            solutionGeneratedByCrossover = Crossover.crossOverSBX(solution1,solution2);
                        }
                        counter++;

                        if(solutionGeneratedByCrossover != null)
                        {
                            //System.out.println("Iteration : "+counterCrossover+" Une solution avec une fitness de "+solutionGeneratedByCrossover.getTotalDistanceCovered()+" a ete generee apres "+counter+" essais");

                                //System.out.println("La solution generee par le crossover est meilleure que la meilleure solution connue donc on l'ajoute");
                            if (!SolutionUtils.verifyIfTheSolutionIsInList(populationk, solutionGeneratedByCrossover))
                                populationk.add(solutionGeneratedByCrossover);
                            i++;

                        }
                    }




                }

            }

            bestKnown = selectBestSolution(populationk);
            //SolutionVisualization.updateGraphNode(bestKnown);
            System.out.println("Fitness initiale : "+solution.getTotalDistanceCovered());
            System.out.println("Nombre de vehicules initiaux : "+solution.getRoads().size());
            System.out.println("Pour l'iteration "+k+" la meilleure solution a une fitness de "+
                    bestKnown.getTotalDistanceCovered());
            System.out.println("Pour l'iteration "+k+" le nombre de vehicules est de "+bestKnown.getRoads().size());
            System.out.println();

            formerPopulation = populationk;

            //System.out.println("Taille de la population : "+formerPopulation.size());


        }
        System.out.println("Fin du programme");
        System.out.println("Fitness de la meilleure solution : " + bestKnown.getTotalDistanceCovered());
        System.out.println("Nombre de vehicules : " + bestKnown.getRoads().size());
        SolutionVisualization.DisplayGraph(bestKnown, "Meilleure solution");
        return bestKnown;

    }



}
