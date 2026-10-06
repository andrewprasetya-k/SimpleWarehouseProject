package org.warehouse.Service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.warehouse.Dto.StockMovementPagedResponse;
import org.springframework.data.domain.Pageable;
import org.warehouse.Dto.StockMovementResponse;
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

    public StockMovementPagedResponse findByItemId(Integer itemId, Pageable pageable) {
        Page<StockMovementModel> paged = repo.findByItemIdOrderByCreatedAtDesc(itemId, pageable);
        List<StockMovementResponse> content = paged.getContent().stream().map(i -> new StockMovementResponse(i.getId(), i.getItemId(), i.getWarehouseId(), i.getMovementType(), i.getQuantityChange(), i.getPreviousQuantity(), i.getCurrentQuantity(), i.getCreatedAt())).toList();
        return new StockMovementPagedResponse(content, paged.getNumber(), paged.getSize(), paged.getTotalElements(), paged.getTotalPages());
    }
}
