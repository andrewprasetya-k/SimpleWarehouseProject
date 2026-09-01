package org.warehouse.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="digital_item", schema="warehouse")
public class DigitalItemModel extends ItemModel{

    @NotNull(message = "Licensed status cannot be empty")
    private Boolean isLicensed;

    protected DigitalItemModel() {}

    public DigitalItemModel(Integer id, String namaItem, Integer quantity, Double price, Boolean isLicensed) {
        super(id, namaItem, quantity, price);
        this.isLicensed = isLicensed;
    }

    public Boolean isLisenced() {
        return isLicensed;
    }

    public void setIsLicensed (Boolean licensed) {
        isLicensed = licensed;
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
