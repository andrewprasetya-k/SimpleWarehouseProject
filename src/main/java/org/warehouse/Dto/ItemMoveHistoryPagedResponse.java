package org.warehouse.Dto;

import java.util.List;

public record ItemMoveHistoryPagedResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
