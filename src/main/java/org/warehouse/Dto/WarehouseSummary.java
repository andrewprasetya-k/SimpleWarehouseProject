package org.warehouse.Dto;

import java.math.BigDecimal;

public record WarehouseSummary(Integer id, String warehouseName, Long totalItem, Double totalValue) {
}
