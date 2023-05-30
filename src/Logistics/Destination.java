package Logistics;

/**
 * Cette classe représente une instance d'une destination
 * Une destination peut être un client ou un dépôt
 */
public abstract class Destination {

    protected GPSCoordinates localisation;
    protected String idName;
    protected int readyTime;
    protected int dueTime;

    /**
     * @param localisation Coordonnées GPS de la destination
     * @param idName      Nom de la destination
     * @param readyTime  Heure à laquelle le livreur peut arriver chez le client
     * @param dueTime   Heure à laquelle le livreur doit être chez le client
     */
    public Destination(GPSCoordinates localisation, String idName, int readyTime, int dueTime) {
        this.localisation = localisation;
        this.idName = idName;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
    }

    /**
     * @return L'heure à laquelle le livreur peut arriver chez le client
     */
    public int getReadyTime() {
        return readyTime;
    }

    /**
     * @return L'heure à laquelle le livreur doit être chez le client
     */
    public int getDueTime() {
        return dueTime;
    }

    /**
     * @return Le nom de la destination
     */
    public String getIdName() {
        return idName;
    }

    /**
     * @return Les coordonnées GPS de la destination
     */
    public GPSCoordinates getLocalisation() {
        return localisation;
    }

    /**
     * @return Un double de l'objet
     */
    public Destination clone() {
        return new Destination(localisation, idName, readyTime, dueTime) {
        };
    }
}
