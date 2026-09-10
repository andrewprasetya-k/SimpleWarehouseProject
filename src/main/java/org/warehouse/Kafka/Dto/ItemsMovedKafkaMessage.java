package org.warehouse.Kafka.Dto;

import java.util.List;

public record ItemsMovedKafkaMessage(
        Integer warehouseId,
        List<Integer> itemIds,
        String status
//        Instant occurredAt
) {
}
