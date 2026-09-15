package org.warehouse.Kafka.Dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ItemsMovedKafkaMessage(
        UUID eventId,
        Integer sourceWarehouseId,
        Integer warehouseId,
        List<Integer> itemIds,
        String status,
        Instant occurredAt
) {
}
