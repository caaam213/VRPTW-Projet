package Metaheuristics.Taboo;

import java.util.Objects;

/**
 * Cette classe représente une transformation pour la méthode Tabou
 * {1,0,5,6} -> {1,0,6,5}
 */
public class TransformationIndexes extends Transformation{
    private int clientFirstRoad;
    private int clientSecondRoad;
    private int client1Index;
    private int client2Index;

    /**
     * Pour les voisins intra-routes
     * @param name Nom de la transformation
     * @param clientFirstRoad Route 1
     * @param client1Index Index du client 1
     * @param client2Index Index du client 2
     */
    public TransformationIndexes(String name, int clientFirstRoad, int client1Index, int client2Index) {
        super(name);
        this.clientFirstRoad = clientFirstRoad;
        this.clientSecondRoad = clientFirstRoad;
        this.client1Index = client1Index;
        this.client2Index = client2Index;
    }

    /**
     * Pour les voisins inter-routes
     * @param name Nom de la transformation
     * @param clientFirstRoad Route 1
     * @param clientSecondRoad Route 2
     * @param client1Index Index du client 1
     * @param client2Index Index du client 2
     */
    public TransformationIndexes(String name, int clientFirstRoad, int clientSecondRoad, int client1Index,
                                 int client2Index) {
        super(name);
        this.clientFirstRoad = clientFirstRoad;
        this.clientSecondRoad = clientSecondRoad;
        this.client1Index = client1Index;
        this.client2Index = client2Index;
    }

    /**
     * @return La transformation inverse
     */
    public TransformationIndexes getTransformationInverse()
    {
        return new TransformationIndexes(this.name, this.clientSecondRoad, this.clientFirstRoad, this.client2Index, this.client1Index);
    }

    /**
     * @return L'affichage de la transformation
     */
    public String toString() {
        return "Transformation{" +
                "name='" + name + '\'' +
                ", clientFirstRoad=" + clientFirstRoad +
                ", clientSecondRoad=" + clientSecondRoad +
                ", client1Index=" + client1Index +
                ", client2Index=" + client2Index +
                '}';
    }

    /**
     * Compare deux transformations
     * @param o Transformation à comparer
     * @return Si les transformations sont égales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransformationIndexes that = (TransformationIndexes) o;
        return clientFirstRoad == that.clientFirstRoad &&
                clientSecondRoad == that.clientSecondRoad &&
                client1Index == that.client1Index &&
                client2Index == that.client2Index;
    }


}
