package se.java.security.dto;

import java.util.List;

public class OrderResponse {
    private String userId;

    private List<String> orderedProductIds;

    public OrderResponse() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderedProductIds(List<String> orderedProductIds) {
        this.orderedProductIds = orderedProductIds;
    }
}
