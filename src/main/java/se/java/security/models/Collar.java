package se.java.security.models;

import org.springframework.data.annotation.TypeAlias;

/**
 * COLLAR PRODUCT SUBCLASS
 * PURPOSE: Represents dog/cat collars with specific properties like size and material.
 * INHERITANCE: Extends Product.class so it inherits all common properties (name, price, color, etc.)
 * plus gets its own collar-specific properties (size, material).
 * JSON EXAMPLE: When this object is serialized, it looks like:
 * {
 *    "productType": "collar",
 *    "name": "Leather Dog Collar",
 *    "color": "brown",
 *    "price": 25.99,
 *    "size": "M",
 *    "material": "leather"
 * }
 * MONGODB STORAGE: Stored in "products" collection with _class field indicating this is a Collar
 */
@TypeAlias("collarProduct")
public class Collar extends Product{
    private String size; // e.g., S, M, L
    private String material; // e.g., leather, nylon

    public Collar() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
