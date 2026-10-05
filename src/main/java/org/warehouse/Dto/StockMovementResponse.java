package org.warehouse.Dto;

import java.time.Instant;

public record StockMovementResponse(Integer id, Integer itemId, Integer warehouseId, String movementType, Integer quantityChange, Integer previousQuantity, Integer currentQuantity, Instant createdAt) {
}
