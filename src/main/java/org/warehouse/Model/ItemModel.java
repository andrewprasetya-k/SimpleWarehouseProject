package org.warehouse.Model;

import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="item", schema="warehouse")
public abstract class ItemModel {
    @Id
    @GeneratedValue
    private Integer id;
    private String namaItem;
    private Integer quantity;
    private Double price;

    protected ItemModel() {}

    public ItemModel(Integer id, String namaItem, Integer quantity, Double price) {
        this.id = id;
        this.namaItem = namaItem;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
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

    public abstract double calculateTotalValue();
}
