package Logistique;

public class Vehicle {
    int capacity;

    public Vehicle(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "capacity=" + capacity +
                '}';
    }
}
