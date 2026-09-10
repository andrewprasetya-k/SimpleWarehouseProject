package org.warehouse.Kafka.Dto;

public record LowStockKafkaMessage(
        Integer itemId,
        String itemName,
        Integer remainingQuantity,
        Integer warehouseId
) {
}
