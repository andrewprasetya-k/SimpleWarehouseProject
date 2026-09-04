package org.warehouse.Dto;

import java.util.List;

public record WarehouseDetailResponse(
        Integer id, String warehouseName, String address, List<ItemDetailResponse> item
) {
}