package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.warehouse.Model.StockMovementModel;
import org.warehouse.Repository.StockMovementRepository;

import java.util.List;

@Service
public class StockMovementService {
    private final StockMovementRepository repo;

    public StockMovementService(StockMovementRepository repo) {
        this.repo = repo;
    }

    public StockMovementModel record(Integer itemId, Integer warehouseId, String movementType, Integer quantityChange, Integer previousQuantity, Integer currentQuantity) {
        StockMovementModel movement = new StockMovementModel(null, itemId, warehouseId, movementType, quantityChange, previousQuantity, currentQuantity, null);
        return repo.save(movement);
    }

    public List<StockMovementModel> findByItemId(Integer itemId) {
        return repo.findByItemIdOrderByCreatedAtDesc(itemId);
    }
}
