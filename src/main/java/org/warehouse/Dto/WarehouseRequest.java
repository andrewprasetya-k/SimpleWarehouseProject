package org.warehouse.Dto;

import jakarta.validation.constraints.NotNull;

public record WarehouseRequest(
    @NotNull(message = "Warehouse name cannot be empty") String warehouseName,
    @NotNull(message = "Address cannot be empty") String address
) {
}
