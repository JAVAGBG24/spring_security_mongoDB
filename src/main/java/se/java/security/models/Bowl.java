package se.java.security.models;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("bowlProduct")
public class Bowl extends Product{
    private double capacity; // in liters

    public Bowl() {
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}