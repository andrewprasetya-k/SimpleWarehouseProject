package org.warehouse.Dto;

public class DigitalItemResponse {
    private Integer id;
    private String itemName;
    private Integer quantity;
    private Double price;
    private Boolean isLicensed;

    public DigitalItemResponse(Integer id, String itemName, Integer quantity, Double price, Boolean isLicensed) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.isLicensed = isLicensed;
    }

    public Integer getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getIsLicensed() {
        return isLicensed;
    }

}
