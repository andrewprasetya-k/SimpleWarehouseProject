package org.warehouse.Dto;

import java.util.List;

public record WarehousePagedResponse(
        List<WarehouseResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
