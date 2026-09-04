package org.warehouse.Dto;

import java.util.List;

public record ItemResponse (
        List<ItemDetailResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
){
}
