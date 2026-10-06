package org.warehouse.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.warehouse.Dto.StockMovementResponse;
import org.warehouse.Model.StockMovementModel;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovementModel, Integer> {
    Page<StockMovementModel> findByItemIdOrderByCreatedAtDesc(Integer itemId, Pageable pageable);
}
