package org.warehouse.Event;

import java.util.List;

public record ItemsMovedEvent(Integer warehouseId, List<Integer> itemsId) {
}
