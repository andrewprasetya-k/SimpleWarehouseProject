package org.warehouse.Dto;

import java.util.List;

public record ItemPagedResponse(
        List<ItemResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
){
}
