package org.warehouse.Dto;

import org.warehouse.Enum.MovementType;

import java.time.Instant;

public record StockMovementResponse(Integer id, Integer itemId, Integer warehouseId, MovementType movementType, Integer quantityChange, Integer previousQuantity, Integer currentQuantity, Instant createdAt) {
}
