package org.warehouse.Dto;

import java.util.List;

public record PhysicalItemPagedResponse(
        List<PhysicalItemResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
