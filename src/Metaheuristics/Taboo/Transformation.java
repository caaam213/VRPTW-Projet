package Metaheuristics.Taboo;

/**
 * Cette classe représente une transformation pour la méthode Tabou
 */
public abstract class Transformation {

    protected String name;

    /**
     * @return Le nom de la transformation
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Le nom de la transformation à modifier
     */
    public Transformation(String name) {
        this.name = name;
    }

    /**
     * @return La transformation inverse
     */
    public abstract Transformation getTransformationInverse();


}

