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
    static int indexEdge1;
    static int indexEdge2;

    public Transformation(int clientFirstI, int clientNewI, int clientFirstR, int clientNewR, int indexEdge1, int indexEdge2) {
        clientFirstIndex = clientFirstI;
        clientNewIndex = clientNewI;
        clientFirstRoad = clientFirstR;
        clientNewRoad = clientNewR;
        this.indexEdge1 = indexEdge1;
        this.indexEdge2 = indexEdge2;
    }

    public Transformation(int clientFirstI, int clientNewI, int clientFirstR, int clientNewR) {
        clientFirstIndex = clientFirstI;
        clientNewIndex = clientNewI;
        clientFirstRoad = clientFirstR;
        clientNewRoad = clientNewR;
        this.indexEdge1 = -1;
        this.indexEdge2 = -1;
    }

}

