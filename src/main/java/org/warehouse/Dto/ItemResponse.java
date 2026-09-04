package org.warehouse.Dto;

public record ItemResponse(
    Integer id,
    String itemName,
    Double price,
    Integer quantity
) {
}
