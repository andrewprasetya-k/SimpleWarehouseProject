package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Integer> {

    //JPA
    List<ItemModel> findByItemNameStartingWith(String itemName);
    Page<ItemModel> findByItemNameStartingWith(String itemName, Pageable pageable);
    List<ItemModel> findByQuantityGreaterThan(int quantity);
    Page<ItemModel> findByQuantityGreaterThan(int quantity, Pageable pageable);

    //JPQL
    @Query("select i from ItemModel i where i.warehouse.id=:warehouseId")
    List<ItemModel> findByWarehouseId(Integer warehouseId);
    @Query(
            value = "select i from ItemModel i where i.warehouse.id=:warehouseId",
            countQuery = "select count(i) from ItemModel i where i.warehouse.id=:warehouseId"
    )
    Page<ItemModel> findByWarehouseId(Integer warehouseId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from ItemModel i where i.id = :id")
    Optional<ItemModel> findByIdForUpdate(Integer id);

    //native sql
    @Query(value = "SELECT i.*, p.weight FROM warehouse.item i " +
            "JOIN warehouse.physical_item p ON i.id = p.id " +
            "WHERE i.item_name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<PhysicalItemModel> findPhysicalItemsByItemName(String keyword);

    @Query(
            value = "SELECT i.*, p.weight FROM warehouse.item i " +
                    "JOIN warehouse.physical_item p ON i.id = p.id " +
                    "WHERE i.item_name LIKE CONCAT('%', :keyword, '%')",
            countQuery = "SELECT COUNT(*) FROM warehouse.item i " +
                    "JOIN warehouse.physical_item p ON i.id = p.id " +
                    "WHERE i.item_name LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true
    )
    Page<PhysicalItemModel> findPhysicalItemsByItemName(String keyword, Pageable pageable);
}
