package org.warehouse.Kafka.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ItemsMovedKafkaMessage(
        UUID eventId,
        Integer sourceWarehouseId,
        Integer warehouseId,
        List<Integer> itemIds,
        String status
) {
}
