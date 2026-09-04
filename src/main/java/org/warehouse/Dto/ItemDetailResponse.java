package org.warehouse.Dto;

public record ItemDetailResponse(
    Integer id,
    String itemName,
    Double price,
    Integer quantity
) {
}
