package org.warehouse.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.warehouse.Model.StockMovementModel;
import org.warehouse.Enum.MovementType;

import java.time.Instant;

public interface StockMovementRepository extends JpaRepository<StockMovementModel, Integer> {
    Page<StockMovementModel> findByItemIdOrderByCreatedAtDesc(Integer itemId, Pageable pageable);
    Page<StockMovementModel> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.createdAt >= :from ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findAllByCreatedAtGreaterThanEqual(@Param("from") Instant from, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.createdAt <= :to ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findAllByCreatedAtLessThanEqual(@Param("to") Instant to, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.createdAt BETWEEN :from AND :to ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findAllByCreatedAtBetween(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.itemId = :itemId AND m.createdAt >= :from ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findByItemIdAndCreatedAtGreaterThanEqual(@Param("itemId") Integer itemId, @Param("from") Instant from, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.itemId = :itemId AND m.createdAt <= :to ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findByItemIdAndCreatedAtLessThanEqual(@Param("itemId") Integer itemId, @Param("to") Instant to, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.itemId = :itemId AND m.createdAt BETWEEN :from AND :to ORDER BY m.createdAt DESC")
    Page<StockMovementModel> findByItemIdAndCreatedAtBetween(@Param("itemId") Integer itemId, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("SELECT m FROM StockMovementModel m WHERE m.createdAt >= :from AND m.createdAt <= :to AND (:itemId IS NULL OR m.itemId = :itemId) AND (:warehouseId IS NULL OR m.warehouseId = :warehouseId) AND (:movementType IS NULL OR m.movementType = :movementType) ORDER BY m.createdAt DESC")
    Page<StockMovementModel> searchByFilters(@Param("from") Instant from, @Param("to") Instant to, @Param("itemId") Integer itemId, @Param("warehouseId") Integer warehouseId, @Param("movementType") MovementType movementType, Pageable pageable);
}
