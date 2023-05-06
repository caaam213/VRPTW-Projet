package Metaheuristique.Taboo;
import Metaheuristique.NeighborOperators.Exchange;
import Metaheuristique.NeighborOperators.Relocate;
import Metaheuristique.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabooMethod {

    static int tabuSize = 10;

    public static int fitness(Solution solution){
        return solution.getTotalDistanceCovered();
    }

    public static Result methodeDeDesecente(Solution x0, ArrayList<Transformation> tabooList)
    {
        int i = -1;
        Solution xi = x0.clone();
        Solution xi1 = new Solution();
        ArrayList<HashMap<Solution, Transformation>> voisinsXi1 = new ArrayList<HashMap<Solution, Transformation>>();
        while( fitness(xi1) < fitness(xi))
        {
            ArrayList<HashMap<Solution, Transformation>> voisins = AllNeighbors(xi);
            ArrayList<Solution> candidats = SolutionWithoutForbidenTransformation(voisins, tabooList);
            for(Solution candidat : candidats)
            {
                if (fitness(candidat) < fitness(xi1))
                {
                    xi1 = candidat.clone();
                    voisinsXi1 = voisins;
                }
            }
            xi = xi1;
            i = i + 1;
        }
        Transformation t = null;
        for( HashMap<Solution, Transformation> voisin : voisinsXi1)
        {
            for (Map.Entry mapentry  : voisin.entrySet())
            {
                if( xi1 == mapentry.getKey())
                    t = (Transformation) mapentry.getValue();
            }
        }
        Result result = new Result(xi1, t);
        return result;
    }

    public static HashMap<Solution, Transformation> getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient) {
        HashMap<Solution, Transformation> sol = new HashMap<>();
        switch(method) {
            case 1:
                sol = Exchange.Exchange(solution,firstClientRoad, newIndexClient, indexClient);
                break;
            case 2:
                sol = Exchange.ExchangeInter(solution,firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 3:
                sol = Relocate.RelocateIntra(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 4:
                sol = Relocate.RelocateInter(solution, firstClientRoad, secondClientRoad, newIndexClient, indexClient);
                break;
            case 5:
                //sol = TwoOpt.runTwoOptInter(solution, firstClientRoad, newIndexClient, indexClient);
                break;
            case 6:
                //sol = TwoOpt.runTwoOptIntra();
                break;
            default:
                sol = null;
                break;
        }
        return sol;
    }

    private static ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesIntra(Solution initialSol) {
        ArrayList<HashMap<Solution, Transformation>> neighbors = new ArrayList<>();
        int[] intraMethods = {1, 3, 5};
        // Pour chaque méthode de voisinage possible
        for(int k : intraMethods) {
            // Pour chaque route
            for(int i = 0; i < initialSol.getRoads().size() ; i++) {
                // Pour une destination j sauf dépot départ et arrivée
                for (int j = 1; j < initialSol.getARoad(i).getDestinations().size()-1; j++)
                {
                    if(initialSol.getARoad(i).getDestinations().size()-2 > 1) {
                        // Pour chaque destination l sauf dépot départ et arrivée
                        for (int l = j; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                            if(l == j)
                                continue;
                            else {
                                // getNeighbor(int method, Solution solution, int firstClientRoad, int secondClientRoad, int newIndexClient, int indexClient)
                                HashMap<Solution, Transformation> candidats = (HashMap<Solution, Transformation>) getNeighbor(k, initialSol, i, i, l, j);
                                HashMap<Solution, Transformation> candidatValid = new HashMap<>();
                                    if(candidats != null)
                                    {
                                        candidatValid = (HashMap<Solution, Transformation>) getNeighbor(k, initialSol, i, i, l, j).clone();
                                        neighbors.add(candidatValid);
                                    }
                                    else
                                        continue;
                            }
                        }
                    }
                    else
                        continue;
                }
            }
        }
        return neighbors;
    }

    private static ArrayList<HashMap<Solution, Transformation> > searchAllCandidatesInter(Solution initialSol) {
        ArrayList<HashMap<Solution, Transformation> > neighbors = new ArrayList<>();
        int[] interMethods = {2, 4, 6};
        // Pour chaque méthode de voisinage possible
        for(int k : interMethods) {
            // Pour chaque route i
            for(int i = 0; i < initialSol.getRoads().size() ; i++) {
                for(int m = 0; m < initialSol.getRoads().size() ; m++) {
                    // Pour chaque destination
                    for (int j = 0; j < initialSol.getARoad(i).getDestinations().size()-1; j++) {
                        for (int l = 0; l < initialSol.getARoad(i).getDestinations().size()-1; l++) {
                            neighbors.add(getNeighbor(k, initialSol, i, i, j,l));
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    private static ArrayList<HashMap<Solution, Transformation> > AllNeighbors(Solution initialSol)
    {
        ArrayList<HashMap<Solution, Transformation> > list1 = searchAllCandidatesIntra(initialSol);
        //ArrayList<HashMap<Solution, Transformation> > list2 = searchAllCandidatesIntra(initialSol);
        //list1.addAll(list2);
        return list1;
    }

    private static ArrayList<Solution> SolutionWithoutForbidenTransformation(ArrayList<HashMap<Solution, Transformation>> voisins, ArrayList<Transformation> tabooList )
    {
        ArrayList<Solution> cleanVoisins = new ArrayList<>();
        for( HashMap<Solution, Transformation> voisin : voisins)
        {
            for (Map.Entry mapentry  : voisin.entrySet())
            {
                if(!tabooList.contains((Transformation)mapentry.getValue()))
                    cleanVoisins.add((Solution)mapentry.getKey());
            }
        }
        return cleanVoisins;
    }


    public static Solution TabouSearch(Solution x0){
        Solution xmin = x0.clone();
        int fmin = fitness(xmin);
        int maxIter = 1000;
        ArrayList<Transformation> tabooList = new ArrayList<>();
        Solution xi = x0.clone();
        Result xi1 = new Result();
        for ( int i =0; i< maxIter; i++)
        {
            xi1 = methodeDeDesecente(xi, tabooList);
            int lambdaF = fitness(xi1.solution) - fitness(xi);
            if(lambdaF>=0){
                tabooList.add(xi1.transformation);
            }

            if(fitness(xi1.solution) < fitness(xmin))
            {
                xmin = xi1.solution.clone();
                fmin = fitness(xmin);
            }

            // On retire le dernier élément ajouté si la liste est pleine
            if (tabooList.size() > tabuSize) {
                tabooList.remove(0);
            }

            xi = xi1.solution.clone();
        }
        return xmin;
    }
}
