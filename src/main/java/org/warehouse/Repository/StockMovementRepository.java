package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.warehouse.Model.StockMovementModel;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovementModel, Integer> {
    List<StockMovementModel> findByItemIdOrderByCreatedAtDesc(Integer itemId);
}
