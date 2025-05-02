package se.java.security.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    private String color;

    private Map<Sizes, Integer> inventory;

    private Category category;

    private boolean inStock;

    private String image;

    private String description;

    @CreatedDate
    private Date createdAt;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotNull Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public Map<Sizes, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Sizes, Integer> inventory) {
        this.inventory = inventory;
    }


    public boolean isInStock() {
        if (inventory == null || inventory.isEmpty()) {
            return false;
        }
        return inventory.values().stream().anyMatch(quantity -> quantity > 0);
    }

    public boolean isInStock(Sizes size) {
        if (inventory == null) {
            return false;
        }
        Integer quantity = inventory.get(size);
        return quantity != null && quantity > 0;
    }

    public Integer getInventoryCount(Sizes size) {
        if (inventory == null) {
            return 0;
        }
        return inventory.getOrDefault(size, 0);
    }

    public void updateInventory(Sizes size, int quantity) {
        if (inventory == null) {
            throw new IllegalStateException("Inventory map is not initialized");
        }
        inventory.put(size, quantity);
    }

    public void decrementInventory(Sizes size, int amount) {
        if (inventory == null) {
            throw new IllegalStateException("Inventory map is not initialized");
        }

        int currentQuantity = inventory.getOrDefault(size, 0);
        if (currentQuantity < amount) {
            throw new IllegalArgumentException("Not enough inventory for size " + size);
        }

        inventory.put(size, currentQuantity - amount);
    }
}
