package org.warehouse.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DigitalItemRequest {
    @NotNull(message = "Item name cannot be empty")
    private String itemName;

    @NotNull(message = "Quantity cannot be empty")
    @Positive(message = "Quantity name must be > 0")
    private Integer quantity;

    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be > 0")
    private Double price;

    @NotNull(message = "Licensed status cannot be empty")
    private Boolean isLicensed;

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

    public Boolean getIsLicensed() {
        return isLicensed;
    }

    public void setIsLicensed(Boolean licensed) {
        isLicensed = licensed;
    }
}
