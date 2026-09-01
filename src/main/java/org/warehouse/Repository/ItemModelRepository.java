package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;

import java.util.List;

@Repository
public interface ItemModelRepository extends JpaRepository<ItemModel, Integer> {

    //JPA
    List<ItemModel> findByQuantityGreaterThan(int quantity);

    //JPQL
    @Query("select i from ItemModel i where i.warehouse.id=:warehouseId")
    List<ItemModel> findByWarehouseId(Integer warehouseId);

    //native sql
    @Query(value = "SELECT i.*, p.weight FROM warehouse.item i " +
            "JOIN warehouse.physical_item p ON i.id = p.id " +
            "WHERE i.item_name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<PhysicalItemModel> findPhysicalItemsByItemName(String keyword);

}