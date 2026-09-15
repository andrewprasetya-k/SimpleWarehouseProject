package org.warehouse.Event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ItemsMovedEvent(
        UUID eventId,
        Integer sourceWarehouseId,
        Integer warehouseId,
        List<Integer> itemsId,
        Instant occurredAt
) {
}
