package org.warehouse.Model;

import jakarta.persistence.*;
import org.warehouse.Enum.MovementType;

import java.time.Instant;

@Entity
@Table(name = "stock_movement", schema = "warehouse")
public class StockMovementModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "previous_quantity", nullable = false)
    private Integer previousQuantity;

    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected StockMovementModel() {}

    public StockMovementModel(Integer id, Integer itemId, Integer warehouseId, MovementType movementType, Integer quantityChange, Integer previousQuantity, Integer currentQuantity, Instant createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.warehouseId = warehouseId;
        this.movementType = movementType;
        this.quantityChange = quantityChange;
        this.previousQuantity = previousQuantity;
        this.currentQuantity = currentQuantity;
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }
    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }
    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }
    public Integer getQuantityChange() { return quantityChange; }
    public void setQuantityDelta(Integer quantityChange) { this.quantityChange = quantityChange; }
    public Integer getPreviousQuantity() { return previousQuantity; }
    public void setPreviousQuantity(Integer previousQuantity) { this.previousQuantity = previousQuantity; }
    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
