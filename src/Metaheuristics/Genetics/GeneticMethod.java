package Metaheuristics.Genetics;

import Graphics.SolutionVisualization;
import Utils.MetaheuristicUtils;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.Result;
import Utils.MapUtil;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Cette classe contient les méthodes de l'algorithme génétique
 */
public class GeneticMethod {

    /**
     * Permet de faire des mutations et de générer une population de base
     * @param solution La solution de base
     * @param timeConstraint Booléen indiquant si on doit respecter la contrainte de temps
     * @return Une solution où l'on applique une méthode de voisinnage aléatoire
     */
    private static Solution applyMethod(Solution solution, boolean timeConstraint) {
        Result solutionGenerated = null;
        Random random = new Random();

        while (solutionGenerated == null) {
            int method = random.nextInt(6) + 1; // Selectionner une route aléatoire

            // Sélectionner deux routes aléatoires
            int randomRoad1 = random.nextInt(solution.getRoads().size());
            int randomRoad2 = -1;
            if (method % 2 == 0) {
                randomRoad2 = random.nextInt(solution.getRoads().size());
                while (randomRoad1 == randomRoad2) {
                    randomRoad2 = random.nextInt(solution.getRoads().size());
                }
            }

            // Sélectionner deux clients aléatoires
            int randomClient1 = random.nextInt(solution.getRoads().get(randomRoad1).getDestinations().size());
            int randomClient2;
            if (method % 2 == 0) {

                // On applique une méthode inter-route
                randomClient2 = random.nextInt(solution.getRoads().get(randomRoad2).getDestinations().size());

                solutionGenerated = MetaheuristicUtils.getNeighbor(method, solution, randomRoad1, randomRoad2,
                        randomClient1, randomClient2, timeConstraint,1);

            } else {

                // On applique une méthode intra-route
                randomClient2 = random.nextInt(solution.getRoads().get(randomRoad1).getDestinations().size());
                while (randomClient1 == randomClient2) {
                    randomClient2 = random.nextInt(solution.getRoads().get(randomRoad1).getDestinations().size());
                }

                solutionGenerated = MetaheuristicUtils.getNeighbor(method, solution, randomRoad1,
                        randomRoad1, randomClient1, randomClient2, timeConstraint,1);
            }


        }
        return solutionGenerated.getSolution();
    }


    /**
     * Cette fonction va permettre de créer une solution initiale
     * @param solution : Solution de base
     * @param populationSize : Taille de la population
     * @param timeConstraint : Booléen indiquant si on doit respecter la contrainte de temps
     * @return Une population de solutions
     */
    private ArrayList<Solution> createInitialPopulation(Solution solution, int populationSize, boolean timeConstraint) {
        ArrayList<Solution> population = new ArrayList<>();

        while (population.size() < populationSize) {

            Solution neighborGenerated = applyMethod(solution.clone(), timeConstraint);
            if (neighborGenerated != null)
            {
                population.add(neighborGenerated);
            }
        }
        System.out.println("Fin de la generation de la population initiale");
        return population;
    }


    /**
     * Cette fonction va créer sélectionner la meilleure solution de la population
     * @param population : Population de solutions
     * @return La meilleure solution de la population
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
     * Cette fonction va permettre d'inverser les probalités de chaque solution
     * En effet, ici, on veut que les solutions ayant une plus grande distance parcourue aient une plus
     * grande probabilité d'être sélectionnées
     * @param solutionsWProbabilities : Solutions avec leurs probabilités
     * @return Les solutions avec leurs probabilités inversées
     */
    private static LinkedHashMap<Solution,Float> reverseProbabilities(LinkedHashMap<Solution,Float> solutionsWProbabilities)
    {
        // On va trier les solutions par ordre croissant de probabilité
        LinkedHashMap<Solution,Float> solutionsWProbabilitiesSorted =
                (LinkedHashMap<Solution, Float>) MapUtil.sortByValue(solutionsWProbabilities);


        LinkedHashMap<Solution,Float> reversedSolutionsWProbabilities = new LinkedHashMap<>();

        Set<Solution> keySet = solutionsWProbabilitiesSorted.keySet();

        // On va inverser les probabilités
        for(int i=0; i<keySet.size(); i++)
        {
            reversedSolutionsWProbabilities.put((Solution) keySet.toArray()[i],
                    solutionsWProbabilitiesSorted.get(keySet.toArray()[keySet.size()-1-i]));
        }

        return reversedSolutionsWProbabilities;
    }

    /**
     * Cette fonction va permettre de calculer la somme des fitness de la population
     * @param population : Population de solutions
     * @return La somme des fitness de la population
     */
    public static int getSumFitness(ArrayList<Solution> population)
    {
        int sumFitness = 0;
        for (Solution solution : population) {
            sumFitness += solution.getTotalDistanceCovered();
        }
        return sumFitness;
    }


    /**
     * Fonction qui va sélectionner les solutions de la prochaine population en tournant la roulette
     * @param population : Population de solutions
     * @param totalDistance : Somme des fitness de la population
     * @param populationSize : Taille de la population
     * @return La nouvelle population
     */
    private static LinkedHashMap<Solution, Integer> rouletteWheelSelection(ArrayList<Solution> population,
                                                                           int totalDistance, int populationSize) {

        ArrayList<Solution> solutionSelected = new ArrayList<>();
        LinkedHashMap<Solution, Integer> newPopulation = new LinkedHashMap<>();
        LinkedHashMap<Solution, Float> solutionsWProbabilities = new LinkedHashMap<>();

        //Créer un tableau de probabilités basée sur la distance parcourue
        for (Solution solutionPop : population) {

            float probability = solutionPop.getTotalDistanceCovered() / (float) totalDistance;
            probability = Math.round(probability * 100000) / 100000.0f;
            solutionsWProbabilities.put(solutionPop, probability);


        }
        // On inverse les probabilités
        solutionsWProbabilities = reverseProbabilities(solutionsWProbabilities);

        // On trie les solutions par ordre croissant de probabilité
        MapUtil.sortByValue(solutionsWProbabilities);

        // On transforme la map en arraylist
        ArrayList<Solution> solutions = solutionsWProbabilities.keySet().stream().collect(Collectors.toCollection(ArrayList::new));

        // On ajoute 1/3 des meilleures solutions à la nouvelle population pour éviter de perdre les meilleures solutions
        for (int i = 0; i < populationSize / 3; i++) {
            try {
                newPopulation.put(solutions.get(i), solutions.get(i).getTotalDistanceCovered());
            } catch (Exception e) {
                break;
            }
        }

        // On crée une roue virtuelle en se basant sur les probabilités
        // Exemple : Si une solution a une probabilité de 0.5, on va l'ajouter 50 fois à la roue
        for(Map.Entry<Solution, Float> entry : solutionsWProbabilities.entrySet())
        {
            for (float i = 0; i < entry.getValue(); i+=0.01) {
                solutionSelected.add(entry.getKey());
            }
        }

        //On tourne la roue
        for (int i = 0; i < populationSize; i++) {
            int randomIndex = new Random().nextInt(solutionSelected.size());
            newPopulation.put(solutionSelected.get(randomIndex), solutionSelected.get(randomIndex).getTotalDistanceCovered());
        }

        // On trie la nouvelle population par ordre croissant de fitness
        newPopulation = (LinkedHashMap<Solution, Integer>) MapUtil.sortByValue(newPopulation);
        return newPopulation;
    }

    /**
     * Fonction qui va sélectionner les <nbBest> meilleures solutions de la population
     * @param solutionList : Population de solutions
     * @param nbBest : Nombre de meilleures solutions à sélectionner
     * @param bestSolutionReproDrasticSelection : Si true, on ne va pas sélectionner plusieurs fois la même solution
     * @return La nouvelle population avec les <nbBest> meilleures solutions
     */
    private static ArrayList<Solution> bestSolutionsReproduction(ArrayList<Solution> solutionList, int nbBest, boolean
            bestSolutionReproDrasticSelection) {

        ArrayList<Solution> newPopulation = new ArrayList<>();
        HashSet<Solution> solutionsAlreadyChosen = new HashSet<>();

        for (Solution solution : solutionList) {

            // Si on veut éviter de sélectionner plusieurs fois la même solution
            if(bestSolutionReproDrasticSelection)
            {
                if (!solutionsAlreadyChosen.contains(solution))
                {
                    newPopulation.add(solution);
                    solutionsAlreadyChosen.add(solution);
                }

            }
            else
            {
                newPopulation.add(solution);
            }

            if (newPopulation.size() == nbBest)
                break;
        }

        return newPopulation;



    }


    /**
     * Fonction qui va créer les cross-over entre les solutions de la population
     * @param formerPopulation : Population de solutions
     * @param timeConstraint : Si true, on va respecter la contrainte de temps
     * @return Une solution issues d'un croisement de deux solutions de la population
     */
    public static Solution createCrossOver(ArrayList<Solution> formerPopulation, boolean timeConstraint)
    {
        Solution solutionGeneratedByCrossover = null;

        // Selectionner deux solutions au hasard
        int solution1Index = new Random().nextInt(formerPopulation.size());
        Solution solution1 = formerPopulation.get(solution1Index).clone();
        int solution2Index = new Random().nextInt(formerPopulation.size());
        while(solution2Index == solution1Index)
        {
            solution2Index = new Random().nextInt(formerPopulation.size());
        }
        Solution solution2 = formerPopulation.get(solution2Index).clone();

        //Selectionner deux routes au hasard
        int road1 = new Random().nextInt(solution1.getRoads().size());
        int road2 = new Random().nextInt(solution2.getRoads().size());

        // Selectionner deux indices au hasard pour le SBX
        int indexBeginningRoad1 = new Random().nextInt(solution1.getRoads().get(road1).getDestinations().size()-2)+1;
        int indexEndRoad2 = new Random().nextInt(solution2.getRoads().get(road2).getDestinations().size()-2)+1;

        // Solution generee par RBX
        Solution solutionGeneratedByCrossoverRBX = Crossover.crossOverRBX(solution1,solution2, road1, road2, timeConstraint);
        if(solutionGeneratedByCrossoverRBX != null)
        {
            solutionGeneratedByCrossover = solutionGeneratedByCrossoverRBX;
        }

        // Solution generee par SBX
        Solution solutionGeneratedByCrossoverSBX = Crossover.crossOverSBX(solution1,solution2, road1,
                road2, indexBeginningRoad1,
                indexEndRoad2,
                timeConstraint);

        // On compare les deux solutions generees et on garde la meilleure
        if(solutionGeneratedByCrossoverSBX != null)
        {
            if (solutionGeneratedByCrossover == null)
            {
                solutionGeneratedByCrossover = solutionGeneratedByCrossoverSBX;
            }
            else
            {
                if (solutionGeneratedByCrossoverSBX.getTotalDistanceCovered() < solutionGeneratedByCrossover.getTotalDistanceCovered())
                {
                    solutionGeneratedByCrossover = solutionGeneratedByCrossoverSBX;
                }
            }
        }
        return solutionGeneratedByCrossover;
    }


    /**
     * Fonction qui va exécuter la méthode génétique
     * @param solution : Solution initiale
     * @param populationSize : Taille de la population
     * @param maxGeneration : Nombre maximum de générations/itérations
     * @param mutationRate : Taux de mutation
     * @param nbBest : Nombre de meilleures solutions à sélectionner
     * @param mutationHillClimbling : Si true, on va utiliser le Hill Climbing pour la mutation
     * @param useCrossoverHillClimbing : Si true, on va utiliser le Hill Climbing pour le cross-over
     * @param bestSolutionReproDrasticSelection : Si true, on va éviter de sélectionner plusieurs fois la même solution
     * @param timeConstraint : Si true, on va respecter la contrainte de temps
     * @return La meilleure solution trouvée
     */
    public static Solution runGeneticMethod(Solution solution, int populationSize, int maxGeneration, float mutationRate,
                                            int nbBest,boolean mutationHillClimbling,boolean useCrossoverHillClimbing,
                                            boolean bestSolutionReproDrasticSelection, boolean timeConstraint) {

        // Visualisation de la solution
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(solution, "Solution");

        // Générer une population initiale
        ArrayList<Solution> formerPopulation = new GeneticMethod().createInitialPopulation(solution.clone(),
                populationSize, timeConstraint);
        formerPopulation.add(solution);

        // Selectionner la meilleure solution de la population
        Solution bestKnown = selectBestSolution(formerPopulation);

        LinkedHashMap<Solution, Integer> populationSelectedByRoulette;
        ArrayList<Solution> populationk;

        for(int k=0; k<maxGeneration; k++)
        {
            System.out.println("Iteration numero "+k);
            System.out.println("Nombre de solutions dans la population : "+formerPopulation.size());
            int totalFitness = getSumFitness(formerPopulation);

            //Tourner la roulette
            populationSelectedByRoulette = rouletteWheelSelection(formerPopulation,totalFitness, populationSize);

            // Selectionner les <nbBest> meilleures solutions
            formerPopulation = new ArrayList<>(populationSelectedByRoulette.keySet());
            populationk = bestSolutionsReproduction(formerPopulation, nbBest, bestSolutionReproDrasticSelection);

            int nbBestSolutionFound = populationk.size();
            // Nombre de solution à générer
            int numberSolutionToGenerate = populationSize - nbBestSolutionFound;

            int i = 0; //Compteur de solution générée

            while (i != numberSolutionToGenerate)
            {
                // Si on a qu'une solution ou alors que la probabilité de mutation est atteinte, on va faire une mutation
                if (new Random().nextFloat() < mutationRate || populationk.size() <=1)
                {
                    int indexSolutionSelected = new Random().nextInt(populationk.size());
                    Solution solutionSelected = populationk.get(indexSolutionSelected).clone();
                    Solution mutationSolution = null;

                    // Méthode de descente de la solution sélectionnée
                    if (mutationHillClimbling)
                    {
                        Result result = MetaheuristicUtils.hillClimbing(solutionSelected, timeConstraint);
                        if(result != null)
                            mutationSolution = result.getSolution();
                    }
                    else
                    {
                        // On génère un voisin tant que mutationSolution est null
                        while (mutationSolution==null)
                        {
                            mutationSolution = applyMethod(solutionSelected, timeConstraint);
                        }
                    }

                    //On ajoute la solution mutée à la population
                    populationk.add(mutationSolution);
                    i++;


                }
                else
                {
                    // On fait un cross-over

                    Solution solutionGeneratedByCrossover = null;
                    int counter = 0;

                    // On génère un cross-over tant que solutionGeneratedByCrossover est null ou que le compteur
                    // est inférieur à 2*populationSize
                    while(solutionGeneratedByCrossover == null && counter < 2*populationSize)
                    {

                        solutionGeneratedByCrossover = createCrossOver(populationk,timeConstraint);
                        counter++;

                        if(solutionGeneratedByCrossover != null)
                        {
                            // Méthode de descente de la solution générée par le cross-over si useCrossoverHillClimbing
                            // est true
                            if (useCrossoverHillClimbing)
                            {
                                solutionGeneratedByCrossover = MetaheuristicUtils.hillClimbing(solutionGeneratedByCrossover,
                                        timeConstraint).getSolution();
                            }

                            populationk.add(solutionGeneratedByCrossover);

                            i++;
                            break;

                        }
                    }

                }

            }

            // On sélectionne la meilleure solution de la population
            Solution bestKnownAmongPop = selectBestSolution(populationk);

            // Si la meilleure solution de la population est mieux que la meilleure solution connue, on la remplace
            if (bestKnownAmongPop.getTotalDistanceCovered() < bestKnown.getTotalDistanceCovered())
            {
                bestKnown = bestKnownAmongPop;
            }

            // On met à jour la visualisation
            solutionVisualization.updateGraphNode(bestKnown);

            // On affiche les informations
            System.out.println("Fitness initiale : "+solution.getTotalDistanceCovered());
            System.out.println("Nombre de vehicules initiaux : "+solution.getRoads().size());
            System.out.println("Pour l'iteration "+k+" la meilleure solution a une fitness de "+
                    bestKnown.getTotalDistanceCovered());
            System.out.println("Pour l'iteration "+k+" le nombre de vehicules est de "+bestKnown.getRoads().size());
            System.out.println();

            // On met à jour la population
            formerPopulation = populationk;

        }

        // On affiche les informations finales
        System.out.println("Fin du programme");
        System.out.println("Fitness de la meilleure solution : " + bestKnown.getTotalDistanceCovered());
        System.out.println("Nombre de vehicules : " + bestKnown.getRoads().size());

        return bestKnown;

    }



}
