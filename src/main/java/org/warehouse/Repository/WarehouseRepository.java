package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.warehouse.Dto.WarehouseSummary;
import org.warehouse.Model.WarehouseModel;

public interface WarehouseRepository extends JpaRepository<WarehouseModel, Integer> {

    @Query("SELECT new org.warehouse.Dto.WarehouseSummary(i.warehouse.id, i.warehouse.warehouseName, COUNT(i), SUM(i.price * i.quantity)) " +
            "FROM ItemModel i WHERE i.warehouse.id = :warehouseId GROUP BY i.warehouse.id, i.warehouse.warehouseName")
    WarehouseSummary getWarehouseSummary(Integer warehouseId);
}
