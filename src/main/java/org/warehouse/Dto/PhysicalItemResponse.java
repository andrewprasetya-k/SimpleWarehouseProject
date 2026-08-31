package org.warehouse.Dto;

public class PhysicalItemResponse {
    private Integer id;
    private String itemName;
    private Integer quantity;
    private Double price;
    private double weight;

    public PhysicalItemResponse(Integer id, String itemName, Integer quantity, Double price, double weight) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getId() {
        return id;
    }

}
