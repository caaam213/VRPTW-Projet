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

    public ArrayList<Solution> TwoOpt(Solution sol) {

        ArrayList<Solution> neighbors = new ArrayList<Solution>();

        return neighbors;
    }
}
