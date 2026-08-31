package org.warehouse.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name="digital_item", schema="warehouse")
public class DigitalItemModel extends ItemModel{

    private boolean isLicensed;

    protected DigitalItemModel() {}

    public DigitalItemModel(Integer id, String namaItem, Integer quantity, Double price, boolean isLicensed) {
        super(id, namaItem, quantity, price);
        this.isLicensed = isLicensed;
    }

    public boolean isLisenced() {
        return isLicensed;
    }

    public void setLisenced(boolean lisenced) {
        isLicensed = lisenced;
    }

    @Override
    public double calculateTotalValue() {
        double total=getQuantity()*getPrice();
        if(isLicensed) {
            total += total * 0.1;
        }
        return total;
    }
}
