package Logistics;

/**
 * Cette classe représente une instance d'un véhicule
 */
public class Vehicle {
    int capacity;

    /**
     * @param capacity Capacité du véhicule
     */
    public Vehicle(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return La capacité du véhicule
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @return Un affichage textuel du véhicule avec ses informations
     */
    @Override
    public String toString() {
        return "Vehicle{" +
                "capacity=" + capacity +
                '}';
    }
}
