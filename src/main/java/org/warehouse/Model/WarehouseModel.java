package org.warehouse.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="warehouse_location", schema="warehouse")
public class WarehouseModel {
    @Id
    @GeneratedValue
    private Integer id;

    private String warehouseName;
    private String address;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnore
    private List<ItemModel> items;

    protected WarehouseModel() {}

    public WarehouseModel(Integer id, String warehouseName, String address, List<ItemModel> items) {
        this.id = id;
        this.warehouseName = warehouseName;
        this.address = address;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
