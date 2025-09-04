package se.java.security.models;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("leashProduct")
public class Leash extends Product{
    private double length; // length of the leash in meters
    private String material; // e.g., leather, nylon

    public Leash() {
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}