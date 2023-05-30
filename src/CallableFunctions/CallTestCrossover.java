package CallableFunctions;

import Graphics.SolutionVisualization;
import Logistics.Configuration;
import Metaheuristics.Genetics.Crossover;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Utils.SolutionUtils;

public class CallTestCrossover {
    public static void testRemoveClientServedMoreThanOnce()
    {
        Solution solution = new Solution();
        Configuration configuration = new Configuration("101");

        Road road = new Road(1);
        road.getDestinations().add(configuration.getCentralDepot());
        for (int i = 0; i < 5; i++) {
            road.getDestinations().add(configuration.getClientsList().get(i));
        }
        road.getDestinations().add(configuration.getCentralDepot());
        solution.addRoads(road);

        Road road2 = new Road(2);
        road2.getDestinations().add(configuration.getCentralDepot());
        for (int i = 5; i < 10; i++) {
            road2.getDestinations().add(configuration.getClientsList().get(i));
        }
        road2.getDestinations().add(configuration.getClientsList().get(0));
        road2.getDestinations().add(configuration.getClientsList().get(1));
        road2.getDestinations().add(configuration.getCentralDepot());

        solution.addRoads(road2);

        solution.setConfig(configuration);

        // Affichage des routes
        System.out.println("Routes de base :");
        System.out.println(road.toString());
        System.out.println(road2.toString());

        // Suppression des clients servis plus d'une fois
        solution = Crossover.removeClientServedMoreThanOnce(solution, 0, false);

        // Affichage des routes
        System.out.println("Routes après suppression des clients servis plus d'une fois :");
        System.out.println(solution.getRoads().get(0).toString());
        System.out.println(solution.getRoads().get(1).toString());

    }

    public static void testCrossoverSBX()
    {

        Configuration configuration = new Configuration("3");

        Solution solution1 = SolutionUtils.generateRandomSolution(configuration, true, true);
        Solution solution2 = SolutionUtils.generateRandomSolution(configuration, false, true);

        // Affichage des routes
        System.out.println("Routes de base :");
        //Solution 1
        System.out.println("Solution 1 :");
        for (int i = 0; i < solution1.getRoads().size(); i++) {
            System.out.println(solution1.getRoads().get(i).toString());
        }

        //Solution 2
        System.out.println("Solution 2 :");
        for (int i = 0; i < solution2.getRoads().size(); i++) {
            System.out.println(solution2.getRoads().get(i).toString());
        }
        int counter = 1;
        // Crossover
        for (int i =0; i<solution1.getRoads().size();i++)
        {
            for (int j= 0; j<solution2.getRoads().size();j++)
            {
                for (int client1=1; client1<solution1.getRoads().get(i).getDestinations().size()-1; client1++)
                {
                    for (int client2=1; client2<solution2.getRoads().get(j).getDestinations().size()-1; client2++)
                    {

                        Solution solutionSBX = Crossover.crossOverSBX(solution1, solution2, i,j,client1,
                                client2, true);
                        if (solutionSBX!=null)
                        {
                            System.out.println("");
                            System.out.println("Solution numero "+counter);
                            System.out.println("Crossover a partir de la route " + i + " de la solution 1 et de la route " + j +
                                    " de la solution 2, en prenant le client " + client1 + " de la route " + i +
                                    " de la solution 1 et le client " + client2 + " de la route " + j + " de la solution 2");
                            System.out.println("Solution apres crossover :");
                            for (int k = 0; k < solutionSBX.getRoads().size(); k++) {
                                System.out.println(solutionSBX.getRoads().get(k).toString());
                            }
                            SolutionVisualization visualization = new SolutionVisualization();
                            visualization.DisplayGraph(solutionSBX, "SBX generee numero "+counter);
                            counter++;
                        }

                    }

                }

            }
        }
    }

    public static void testCrossoverRBX()
    {

        Configuration configuration = new Configuration("3");

        Solution solution1 = SolutionUtils.generateRandomSolution(configuration, true, true);
        Solution solution2 = SolutionUtils.generateRandomSolution(configuration, false, true);

        // Affichage des routes
        System.out.println("Routes de base :");
        //Solution 1
        System.out.println("Solution 1 :");
        for (int i = 0; i < solution1.getRoads().size(); i++) {
            System.out.println(solution1.getRoads().get(i).toString());
        }

        //Solution 2
        System.out.println("Solution 2 :");
        for (int i = 0; i < solution2.getRoads().size(); i++) {
            System.out.println(solution2.getRoads().get(i).toString());
        }
        int counter = 1;
        // Crossover
        for (int i =0; i<solution1.getRoads().size();i++)
        {
            for (int j= 0; j<solution2.getRoads().size();j++) {
                Solution solutionRBX = Crossover.crossOverRBX(solution1, solution2, i, j,true);
                if (solutionRBX != null) {
                    System.out.println("");
                    System.out.println("Solution numero " + counter);
                    System.out.println("Crossover a partir de la route " + i + " de la solution 1 et de la route " + j +
                            " de la solution 2");
                    System.out.println("Solution apres crossover :");
                    for (int k = 0; k < solutionRBX.getRoads().size(); k++) {
                        System.out.println(solutionRBX.getRoads().get(k).toString());
                    }
                    SolutionVisualization visualization = new SolutionVisualization();
                    visualization.DisplayGraph(solutionRBX, "RBX generee numero " + counter);
                    counter++;
                }
            }

        }
    }

}
