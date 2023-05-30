package Metaheuristics.Taboo;

/**
 * Cette classe représente une transformation pour la méthode Tabou
 * Ex : c1 -> c2
 */
public class TransformationClientsId extends Transformation{
    private String clientInitialId;
    private String clientNewId;

    /**
     * @return L'identifiant du client initial
     */
    public String getClientInitialId() {
        return clientInitialId;
    }

    /**
     * @return L'identifiant du nouveau client
     */
    public String getClientNewId() {
        return clientNewId;
    }

    /**
     * @param name Nom de la transformation
     * @param clientFirstI Identifiant du client initial
     * @param clientNewI Identifiant du nouveau client
     */
    public TransformationClientsId(String name, String clientFirstI, String clientNewI) {
        super(name);
        clientInitialId = clientFirstI;
        clientNewId = clientNewI;
    }


    /**
     * @return La transformation inverse
     */
    public TransformationClientsId getTransformationInverse()
    {
        return new TransformationClientsId(this.name,  this.clientNewId, this.clientInitialId);
    }

    /**
     * @return La transformation sous format texte
     */
    public String toString()
    {
        return name + " : " + clientInitialId + " -> " + clientNewId;
    }

    /**
     * Compare deux transformations
     * @param obj Transformation à comparer
     * @return Si les transformations sont égales
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        TransformationClientsId other = (TransformationClientsId) obj;
        if (!this.name.equals(other.getName())) {
            return false;
        }
        if (!this.clientInitialId.equals(other.getClientInitialId())) {
            return false;
        }
        if (!this.clientNewId.equals(other.getClientNewId())) {
            return false;
        }
        return true;
    }
}
