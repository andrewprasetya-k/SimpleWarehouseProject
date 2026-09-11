package org.warehouse.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Integer> {

    //JPA
    Page<ItemModel> findByItemNameStartingWith(String itemName, Pageable pageable);
    Page<ItemModel> findByQuantityGreaterThan(int quantity, Pageable pageable);

    //JPQL
    @Query("select i from ItemModel i where i.warehouse.id=:warehouseId")
    Page<ItemModel> findByWarehouseId(Integer warehouseId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from ItemModel i where i.id = :id")
    Optional<ItemModel> findByIdForUpdate(Integer id);

    // JPQL – prefix search on physical items (avoids native-SQL pagination complexity)
    @Query("select p from PhysicalItemModel p where p.itemName like concat(:keyword, '%')")
    Page<PhysicalItemModel> findPhysicalItemsByItemName(String keyword, Pageable pageable);
}
