package org.warehouse.Dto;

import java.util.List;

public record StockMovementPagedResponse(
        List<StockMovementResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
