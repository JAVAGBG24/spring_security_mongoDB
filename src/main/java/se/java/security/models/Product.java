package se.java.security.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ABSTRACT BASE CLASS for all pet products in the system.
 * PURPOSE: This class enables POLYMORPHIC STORAGE - storing different types of pet products
 *  (collars, leashes, toys, bowls) in a SINGLE MongoDB collection called "products" while
 *  maintaining type safety and specific properties for each subclass.
 *  *
 * JACKSON ANNOTATIONS EXPLAINED:
 * - @JsonTypeInfo: Tells Jackson to include type information when serializing/deserializing JSON
 * - use = JsonTypeInfo.Id.NAME: Uses the "name" attribute from @JsonSubTypes as the type identifier
 * - include = JsonTypeInfo.As.PROPERTY: Adds the type info as a JSON property
 * - property = "productType": The JSON field name that will contain the type (e.g., "collar", "leash")
 *  *
 * - @JsonSubTypes: Maps JSON productType values to specific Java classes
 * - When JSON contains "productType": "collar" → creates Collar.class instance
 * - When JSON contains "productType": "leash" → creates Leash.class instance
 * - When JSON contains "productType": "toy" → creates Toy.class instance
 * - When JSON contains "productType": "bowl" → creates Bowl.class instance
 *  *
 * MONGODB BEHAVIOR:
 * - @Document(collection = "products"): All subclasses are stored in the same "products" collection
 * - MongoDB automatically adds a "_class" field to track the actual Java class for each document
 */
@Document(collection = "products")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "productType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Collar.class, name = "collar"),
        @JsonSubTypes.Type(value = Leash.class, name = "leash"),
        @JsonSubTypes.Type(value = Toy.class, name = "toy"),
        @JsonSubTypes.Type(value = Bowl.class, name = "bowl")
})
public abstract class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private String color;
    private double price;
    private int stockQuantity;

    // Default constructor required by Jackson for JSON deserialization and MongoDB
    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
