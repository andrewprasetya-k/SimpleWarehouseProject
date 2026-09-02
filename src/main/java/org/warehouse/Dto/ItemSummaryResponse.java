package org.warehouse.Dto;

public record ItemSummaryResponse(
    Integer id,
    String itemName,
    Double price,
    Integer quantity
) {
}
