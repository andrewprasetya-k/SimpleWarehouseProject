package org.warehouse.Controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.warehouse.Dto.StockMovementPagedResponse;
import org.warehouse.Service.StockMovementService;

import java.time.Instant;

@RestController
@RequestMapping("/movements")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @GetMapping
    public StockMovementPagedResponse getAllMovements(
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            Pageable pageable
    ) {
        return stockMovementService.findAllMovements(from, to, pageable);
    }
}
