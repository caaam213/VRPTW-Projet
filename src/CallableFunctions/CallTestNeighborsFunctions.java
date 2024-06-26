package CallableFunctions;

import Graphics.SolutionVisualization;
import Logistics.Configuration;
import Metaheuristics.NeighborOperators.Exchange;
import Metaheuristics.NeighborOperators.Relocate;
import Metaheuristics.NeighborOperators.TwoOptAndCrossExchange;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.Result;
import Utils.SolutionUtils;

import java.util.ArrayList;
import java.util.HashSet;

public class CallTestNeighborsFunctions {

    private static Solution fakeSolution = new Solution();
    private static Configuration configuration = new Configuration("101");

    // Ajouter deux routes et ses destinations
    private static Road road1 = new Road(1);
    private static Road road2 = new Road(2);

    private static Road road3 = new Road(3);

    private static void displayResult(int roadIndex, int roadIndex2,
                                      int destinationIndex1, int destinationIndex2, Solution neighborSolution) {
        System.out.println("------------------------------------------");
        String displayRoads = roadIndex == roadIndex2 ? "Route impactee : "+(roadIndex+1) :
                "Echange entre les routes "+(roadIndex+1)+" et "+(roadIndex2+1);
        System.out.println(displayRoads);
        System.out.println("Echange entre les destinations "+(destinationIndex1)+" et "+(destinationIndex2));
        for(int road=0; road<=2;road++)
        {
            try
            {
                System.out.println(neighborSolution.getRoads().get(road).toString());
            }
            catch(Exception e) {
                System.out.println("La route "+(road+1)+" n'existe plus");
            }
        }
    }
    private static void initializeTestVariables(int roadLength1, int roadLength2, int roadLength3)
    {

        // Route 1 c1 c2 c3
        road1.getDestinations().add(configuration.getCentralDepot());
        for(int clientIndex=0; road1.getDestinations().size()<=roadLength1; clientIndex++)
        {
            road1.getDestinations().add(configuration.getClientsList().get(clientIndex));
        }
        road1.getDestinations().add(configuration.getCentralDepot());

        // Route 2 c6 c7
        road2.getDestinations().add(configuration.getCentralDepot());
        for(int clientIndex=5; road2.getDestinations().size()<=roadLength2; clientIndex++)
        {
            road2.getDestinations().add(configuration.getClientsList().get(clientIndex));
        }
        road2.getDestinations().add(configuration.getCentralDepot());

        // Route 3 c30

        road3.getDestinations().add(configuration.getCentralDepot());
        for (int clientIndex = 30; road3.getDestinations().size()<=roadLength3; clientIndex++) {
            road3.getDestinations().add(configuration.getClientsList().get(clientIndex));
        }
        road3.getDestinations().add(configuration.getCentralDepot());

        // Ajouter les routes dans la solution
        fakeSolution = new Solution();
        ArrayList<Road> roads = new ArrayList<Road>(){{
            add(road1);
            add(road2);
            add(road3);
        }};
        fakeSolution.setRoads(roads);
        fakeSolution.setConfig(configuration);
    }

    private static void initializeTestsIntra()
    {
        Configuration configuration = new Configuration("3");
        fakeSolution = SolutionUtils.generateRandomSolution(configuration,false,false);
        new SolutionVisualization().DisplayGraph(fakeSolution, "Solution base");
        //Affichage des routes de base :
        System.out.println("Routes de base : ");
        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size(); roadIndex++) {
            System.out.println(fakeSolution.getRoads().get(roadIndex).toString());
        }
    }

    private static void initializeTestsInter()
    {
        Configuration configuration = new Configuration("3");

        int roadLength1 = 1;
        int roadLength2 = 8;
        int roadLength3 = configuration.getClientsList().size();

        Road road1 = new Road(1);
        Road road2 = new Road(2);
        Road road3 = new Road(3);

        road1.getDestinations().add(configuration.getCentralDepot());
        for (int i = 0; i < roadLength1; i++) {
            road1.getDestinations().add(configuration.getClientsList().get(i));
        }
        road1.getDestinations().add(configuration.getCentralDepot());
        System.out.println(road1.toString());

        road2.getDestinations().add(configuration.getCentralDepot());
        for (int i = roadLength1; i < roadLength2; i++) {
            road2.getDestinations().add(configuration.getClientsList().get(i));
        }
        road2.getDestinations().add(configuration.getCentralDepot());
        System.out.println(road2.toString());

        road3.getDestinations().add(configuration.getCentralDepot());
        for (int i = roadLength2; i < roadLength3; i++) {
            road3.getDestinations().add(configuration.getClientsList().get(i));
        }
        road3.getDestinations().add(configuration.getCentralDepot());
        System.out.println(road3.toString());

        fakeSolution = new Solution();
        fakeSolution.getRoads().add(road1);
        fakeSolution.getRoads().add(road2);
        fakeSolution.getRoads().add(road3);
        fakeSolution.setConfig(configuration);
    }

    /**
     * Test de la fonction d'échange intra-route
     */
    public static void generateAllExchangeIntra()
    {
        initializeTestsIntra();
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        //Affichage des routes de base :
        System.out.println("Routes de base : ");
        for (int roadIndex = 0; roadIndex < 1; roadIndex++) {
            System.out.println(fakeSolution.getRoads().get(roadIndex).toString());
        }
        System.out.println();

        for (int roadIndex = 0; roadIndex < 1; roadIndex++) {

            for (int destinationIndex1 = 1; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size() - 1; destinationIndex1++) {
                for (int destinationIndex2 = destinationIndex1 + 1; destinationIndex2 < fakeSolution.getRoads().get(roadIndex).getDestinations().size() - 1; destinationIndex2++) {
                    Solution neighborSolution = Exchange.ExchangeIntra(fakeSolution, roadIndex, destinationIndex1, destinationIndex2, false,1).getSolution();
                    displayResult(roadIndex,roadIndex, destinationIndex1, destinationIndex2, neighborSolution);
                    solutionVisualization.DisplayGraph(neighborSolution, "Solution voisine");
                }

            }
        }
    }

    /**
     * Test de la fonction d'échange inter-route
     */
    public static void generateAllExchangeInter()
    {
        initializeTestsInter();
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(fakeSolution, "Solution de base");

        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size()-1; roadIndex++) {
            for (int roadIndex2 = roadIndex+1; roadIndex2< fakeSolution.getRoads().size(); roadIndex2++)
            {
                for (int destinationIndex1 = 1; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size() - 1; destinationIndex1++) {
                    for (int destinationIndex2 = 1; destinationIndex2 < fakeSolution.getRoads().get(roadIndex2).getDestinations().size() - 1; destinationIndex2++) {
                        Solution neighborSolution;
                        try
                        {
                            neighborSolution = Exchange.ExchangeIntra(fakeSolution, roadIndex,roadIndex2, destinationIndex1, destinationIndex2, false,1).getSolution();
                            solutionVisualization.DisplayGraph(neighborSolution, "Solution voisine");
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

                        displayResult(roadIndex,roadIndex2, destinationIndex1, destinationIndex2, neighborSolution);
                    }

                }
            }
        }
    }

    /**
     * Test de la fonction de relocalisation intra-route
     */
    public static void generateAllRelocateIntra()
    {
        initializeTestsIntra();
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(fakeSolution, "Solution de base");
        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size(); roadIndex++) {

            for (int destinationIndex1 = 0; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size(); destinationIndex1++) {
                for (int destinationIndex2 = 0; destinationIndex2 < fakeSolution.getRoads().get(roadIndex).getDestinations().size(); destinationIndex2++) {
                    Solution neighborSolution;
                    try
                    {
                        neighborSolution = Relocate.RelocateIntra(fakeSolution, roadIndex, destinationIndex1, destinationIndex2, false,1).getSolution();
                        solutionVisualization.DisplayGraph(neighborSolution, "Solution voisine");
                    }
                    catch(Exception e)
                    {
                        continue;
                    }
                    displayResult(roadIndex,roadIndex, destinationIndex1, destinationIndex2, neighborSolution);
                }

            }
        }
    }

    /**
     * Test de la fonction de relocalisation inter-route
     */
    public static void GenerateAllRelocateInter()
    {
        initializeTestsInter();
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(fakeSolution, "Solution de base");
        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size(); roadIndex++) {
            for (int roadIndex2 = 0; roadIndex2< fakeSolution.getRoads().size(); roadIndex2++)
            {

                for (int destinationIndex1 = 0; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size(); destinationIndex1++) {
                    for (int destinationIndex2 = 0; destinationIndex2 < fakeSolution.getRoads().get(roadIndex2).getDestinations().size(); destinationIndex2++) {
                        Solution neighborSolution;
                        try
                        {
                            neighborSolution = Relocate.RelocateInter(fakeSolution, roadIndex,roadIndex2, destinationIndex1, destinationIndex2, false,1).getSolution();
                            solutionVisualization.DisplayGraph(neighborSolution, "Solution voisine");
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

                        displayResult(roadIndex,roadIndex2, destinationIndex1, destinationIndex2, neighborSolution);
                    }

                }
            }
        }
    }

    /**
     * Test de la fonction 2-opt
     */
    public static void generateAllTwoOpt()
    {
        // Si la différence des destinations vaut 2, alors on a un échange
        //initializeTestVariables(roadLength1, roadLength2,roadLength3);
        initializeTestsIntra();
        System.out.println();
        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size(); roadIndex++) {

            for (int destinationIndex1 = 0; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size()-1; destinationIndex1++) {
                for (int destinationIndex2 = destinationIndex1+1; destinationIndex2 < fakeSolution.getRoads().get(roadIndex).getDestinations().size(); destinationIndex2++) {
                    Solution neighborSolution;
                    try
                    {
                        neighborSolution = TwoOptAndCrossExchange.runTwoOpt(fakeSolution, roadIndex, destinationIndex1, destinationIndex2, false,1).getSolution();
                        new SolutionVisualization().DisplayGraph(neighborSolution, "TwoOpt");
                    }
                    catch(Exception e)
                    {
                        continue;
                    }
                    displayResult(roadIndex,roadIndex, destinationIndex1, destinationIndex2, neighborSolution);
                }

            }
        }
    }

    /**
     * Test de la fonction cross-exchange
     */
    public static void generateAllCrossExchange()
    {
        //initializeTestVariables(roadLength1, roadLength2,roadLength3);

        initializeTestsInter();
        SolutionVisualization solutionVisualization = new SolutionVisualization();
        solutionVisualization.DisplayGraph(fakeSolution, "Solution de base");
        new SolutionVisualization().DisplayGraph(fakeSolution, "Solution base");
        int solutionCounter = 1;
        for (int roadIndex = 0; roadIndex < fakeSolution.getRoads().size()-1; roadIndex++) {
            for (int roadIndex2 = roadIndex+1; roadIndex2< fakeSolution.getRoads().size(); roadIndex2++)
            {

                for (int destinationIndex1 = 0; destinationIndex1 < fakeSolution.getRoads().get(roadIndex).getDestinations().size(); destinationIndex1++) {
                    for (int destinationIndex2 = 0; destinationIndex2 < fakeSolution.getRoads().get(roadIndex2).getDestinations().size(); destinationIndex2++) {
                        Solution neighborSolution;
                        try
                        {

                            neighborSolution = TwoOptAndCrossExchange.runCrossExchange(fakeSolution, roadIndex,roadIndex2, destinationIndex1, destinationIndex2, false,1).getSolution();
                            System.out.println("Solution voisine numero "+solutionCounter);
                            solutionVisualization.DisplayGraph(neighborSolution, "Solution voisine numero "+solutionCounter);
                            solutionCounter++;

                        }
                        catch(Exception e)
                        {
                            continue;
                        }

                        displayResult(roadIndex,roadIndex2, destinationIndex1, destinationIndex2, neighborSolution);
                    }

                }
            }
        }
    }

    /**
     * Vérifie si les solutions sont égales et leurs ajouts dans une hashset
     */
    public static void testHashSetCreation()
    {
        initializeTestsIntra();
        HashSet<Integer> hashSet = new HashSet<>();
        //Test de exchange, relocateIntra et twoOpt dans le cas ou ils vont retourner la même solution
        Solution exchange = Exchange.ExchangeIntra(fakeSolution, 0, 1, 2, false,1).getSolution();
        Solution relocateIntra = Relocate.RelocateIntra(fakeSolution, 0, 1, 2, false,1).getSolution();
        Solution twoOpt = TwoOptAndCrossExchange.runTwoOpt(fakeSolution, 0, 0, 3, false,1).getSolution();

        System.out.println("Exchange : ");
        displayResult(0,0,1,2,exchange);
        System.out.println("RelocateIntra : ");
        displayResult(0,0,1,2,relocateIntra);
        System.out.println("TwoOpt : ");
        displayResult(0,0,0,3,twoOpt);

        HashSet<Solution> hashSet2 = new HashSet<>();
        hashSet2.add(exchange);
        hashSet2.add(relocateIntra);
        hashSet2.add(twoOpt);

        System.out.println("Egal ?"+exchange.equals(relocateIntra));
        System.out.println("Egal ?"+exchange.equals(twoOpt));
        System.out.println("Egal ?"+relocateIntra.equals(twoOpt));

        ArrayList<Solution> test = new ArrayList<>();
        test.add(exchange);
        System.out.println("Contient ?"+test.contains(twoOpt));

        System.out.println("Taille de hashSet2 : " + hashSet2.size());

    }


    public static void testTransformations()
    {
        initializeTestVariables(5, 4,3);
        System.out.println("Route 1 de base : " + fakeSolution.getRoads().get(0).toString());

        for (int transformationIndex = 1; transformationIndex <=3; transformationIndex++) {

            System.out.println();
            System.out.println("Transformation " + transformationIndex + " : ");
            // On va faire un relocateIntra sur la solution
            Result result = Relocate.RelocateIntra(fakeSolution, 0, 1, 2, false,
                    transformationIndex);

            System.out.println("Route 1 apres transformation : " + result.getSolution().getRoads().get(0).toString());
            System.out.println("Transformation " + transformationIndex + " : " +
                    result.getTransformation().toString());

            System.out.println();

            // On va faire un relocateInter sur la solution
            System.out.println("Route 1 de base : " + fakeSolution.getRoads().get(0).toString());
            System.out.println("Route 2 de base : " + fakeSolution.getRoads().get(1).toString());
            result = Relocate.RelocateInter(fakeSolution, 0, 1, 2, 1, false,
                    transformationIndex);
            System.out.println("Route 1 apres transformation : " + result.getSolution().getRoads().get(0).toString());
            System.out.println("Route 2 apres transformation : " + result.getSolution().getRoads().get(1).toString());
            System.out.println("Transformation " + transformationIndex + " : " +
                    result.getTransformation().toString());



        }
    }







}
