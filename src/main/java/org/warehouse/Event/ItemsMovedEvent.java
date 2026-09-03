package org.warehouse.Event;

import java.util.List;

public class ItemsMovedEvent {
    public final Integer warehouseId;
    public final List<Integer> itemsId;

    public ItemsMovedEvent(Integer warehouseId, List<Integer> itemsId) {
        this.warehouseId = warehouseId;
        this.itemsId = itemsId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public List<Integer> getItemsId() {
        return itemsId;
    }
}
