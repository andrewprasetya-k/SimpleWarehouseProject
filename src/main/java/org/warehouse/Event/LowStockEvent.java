package org.warehouse.Event;

public record LowStockEvent(
        Integer itemId,
        String itemName,
        Integer remainingQuantity,
        Integer warehouseId
) {
}
