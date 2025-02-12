package se.java.security.dto;

public class OrderItemDTO {
    // vilken produkt
    private String productId;

    // hur många an produkten har man köpt
    private int quantity;

    public OrderItemDTO() {
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
