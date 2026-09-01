package org.warehouse.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PhysicalItemRequest (
    @NotNull(message = "Item name cannot be empty") String itemName,
    @NotNull(message = "Quantity cannot be empty") @Positive(message = "Quantity must be > 0") Integer quantity,
    @NotNull(message = "Price cannot be empty") @Positive(message = "Price must be > 0") Double price,
    @Positive(message = "Weight must be > 0") double weight
) {
}
