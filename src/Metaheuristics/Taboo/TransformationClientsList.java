package Metaheuristics.Taboo;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Cette classe représente une transformation pour la méthode Tabou
 *  {[c1, c2, c3], []} -> {[c2, c1, c3], []}
 */
public class TransformationClientsList extends Transformation {
    private ArrayList<String> startingListRoad1;
    private ArrayList<String> generatedListRoad1;

    private ArrayList<String> startingListRoad2;

    private ArrayList<String> generatedListRoad2;

    /**
     * Pour les voisins inter-routes
     * @param name Nom de la transformation
     * @param startingListRoad1 Liste des clients de la route 1 avant la transformation
     * @param generatedListRoad1 Liste des clients de la route 1 après la transformation
     * @param startingListRoad2 Liste des clients de la route 2 avant la transformation
     * @param generatedListRoad2 Liste des clients de la route 2 après la transformation
     */
    public TransformationClientsList(String name, ArrayList<String> startingListRoad1, ArrayList<String> generatedListRoad1,
                          ArrayList<String> startingListRoad2, ArrayList<String> generatedListRoad2) {
        super(name);
        this.startingListRoad1 = startingListRoad1;
        this.generatedListRoad1 = generatedListRoad1;
        this.startingListRoad2 = startingListRoad2;
        this.generatedListRoad2 = generatedListRoad2;

    }

    /**
     * Pour les voisins intra-route
     * @param name Nom de la transformation
     * @param startingListRoad1 Liste des clients de la route 1 avant la transformation
     * @param generatedListRoad1 Liste des clients de la route 1 après la transformation
     */
    public TransformationClientsList(String name,ArrayList<String> startingListRoad1, ArrayList<String> generatedListRoad1) {
        super(name);
        this.startingListRoad1 = startingListRoad1;
        this.generatedListRoad1 = generatedListRoad1;
        this.startingListRoad2 = new ArrayList<>();
        this.generatedListRoad2 = new ArrayList<>();
    }

    /**
     * @return La transformation inverse
     */
    public TransformationClientsList getTransformationInverse()
    {
        return new TransformationClientsList(this.name, this.generatedListRoad1, this.startingListRoad1,
                this.generatedListRoad2, this.startingListRoad2);
    }

    /**
     * On compare deux transformations
     * @param o Transformation à comparer
     * @return Si les transformations sont égales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransformationClientsList)) return false;
        TransformationClientsList that = (TransformationClientsList) o;
        return
                Objects.equals(startingListRoad1, that.startingListRoad1) &&
                        Objects.equals(generatedListRoad1, that.generatedListRoad1) &&
                        Objects.equals(startingListRoad2, that.startingListRoad2) &&
                        Objects.equals(generatedListRoad2, that.generatedListRoad2);
    }

    /**
     * @return La transformation sous format texte
     */
    @Override
    public String toString() {
        return "Transformation{" +
                "name='" + name + '\'' +
                ", startingListRoad1=" + startingListRoad1 +
                ", generatedListRoad1=" + generatedListRoad1 +
                ", startingListRoad2=" + startingListRoad2 +
                ", generatedListRoad2=" + generatedListRoad2 +
                '}';
    }
}
