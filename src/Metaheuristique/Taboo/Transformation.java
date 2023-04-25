package Metaheuristique.Taboo;

import Logistique.Client;
import Logistique.Configuration;
import Metaheuristique.Edge;
import Metaheuristique.Solution;

import java.util.ArrayList;

public class Transformation {

    static int clientFirstIndex;
    static int clientNewIndex;
    static int clientFirstRoad;
    static int clientNewRoad;

    public Transformation(int clientFirstI, int clientNewI, int clientFirstR, int clientNewR) {
        clientFirstIndex = clientFirstI;
        clientNewIndex = clientNewI;
        clientFirstRoad = clientFirstR;
        clientNewRoad = clientNewR;
    }
}

