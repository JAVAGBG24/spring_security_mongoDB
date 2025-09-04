package se.java.security.models;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("toyProduct")
public class Toy extends Product{
    private String type; // e.g., ball, chew toy

    public Toy() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}