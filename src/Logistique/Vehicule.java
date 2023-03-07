package Logistique;

public class Vehicule {

    GPSCoordinates localisation;
    int capacite;
    int nbDeVehiculeTotaux;
    int distanceCoveredByTheTruck;

    public Vehicule(GPSCoordinates localisation, int capacite, int nbDeVehiculeTotaux, int distanceCoveredByTheTruck) {
        this.localisation = localisation;
        this.capacite = capacite;
        this.nbDeVehiculeTotaux = nbDeVehiculeTotaux;
        this.distanceCoveredByTheTruck = distanceCoveredByTheTruck;
    }

    public Vehicule(int capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return "Vehicule{" +
                "localisation=" + localisation +
                ", capacite=" + capacite +
                ", nbDeVehiculeTotaux=" + nbDeVehiculeTotaux +
                ", distanceCoveredByTheTruck=" + distanceCoveredByTheTruck +
                '}';
    }
}
