package org.warehouse.Kafka;

import java.time.Instant;
import java.util.List;

public record ItemsMovedKafkaMessage(
        Integer warehouseId,
        List<Integer> itemIds,
        String status,
        Instant occurredAt
) {
}
