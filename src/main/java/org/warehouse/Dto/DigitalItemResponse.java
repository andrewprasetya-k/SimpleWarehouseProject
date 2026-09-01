package org.warehouse.Dto;

public record DigitalItemResponse(Integer id, String itemName, Integer quantity, Double price, Boolean isLicensed) {
}
