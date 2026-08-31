package org.warehouse.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PhysicalItemRequest {
    @NotNull(message = "Item name cannot be empty")
    private String itemName;

    @NotNull(message = "Quantity cannot be empty")
    @Positive(message = "Quantity must be > 0")
    private Integer quantity;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be > 0")
    private Double price;

    @Positive(message = "Weight must be > 0")
    private double weight;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
