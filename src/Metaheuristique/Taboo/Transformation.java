package Metaheuristique.Taboo;

import Logistique.Client;
import Logistique.Configuration;
import Metaheuristique.Solution;

import java.util.ArrayList;

public class Transformation {

    int firstClient;
    int secondClient;
    int nbClients;

    public Transformation(int a, int b) {
        firstClient = a;
        secondClient = b;
    }

    public Solution TwoOpt(Solution sol)
    {
        int bestDist = sol.getTotalDistanceCovered();
        int visited = 0;
        Solution voisin = sol;
        while (visited < nbClients) {
            for (int i = 0; i < nbClients - 1; i++) {
                for (int j = i + 1; j < nbClients; j++) {
                    Solution newVoisin = Swap(i, j, voisin);
                    int newDistance = newVoisin.getTotalDistanceCovered();
                    if (newDistance < bestDist) {
                        visited = 0;
                        bestDist = newDistance;
                        voisin = newVoisin;
                    }
                }
            }
            visited++;
        }
        return voisin;
    }

    private Solution Swap(int i, int j, Solution sol) {
        Solution newSolution = sol;
        boolean isSolValid = false;
        while( !isSolValid ) {
            newSolution.setClient(i,j);
            if( newSolution.isValide() ) {
                isSolValid = true;
            }
        }
        return newSolution;
    }

    public ArrayList<Solution> Relocate(Solution sol) {

        ArrayList<Solution> neighbors = new ArrayList<Solution>();

        return neighbors;
    }
}
