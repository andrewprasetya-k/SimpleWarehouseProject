package org.warehouse.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="physical_item", schema="warehouse")
public class PhysicalItemModel extends ItemModel{

    @Positive(message = "Weight should be > 0")
    private double weight;

    protected PhysicalItemModel() {}

    public PhysicalItemModel(Integer id, String namaItem, Integer quantity, Double price, double weight) {
        super(id, namaItem, quantity, price);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double calculateTotalValue() {
        return getPrice()*getQuantity()*weight;
    }

}
