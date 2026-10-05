package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.warehouse.Dto.WarehouseSummary;
import org.warehouse.Model.WarehouseModel;

public interface WarehouseRepository extends JpaRepository<WarehouseModel, Integer> {

    @Query("SELECT new org.warehouse.Dto.WarehouseSummary(w.id, w.warehouseName, COUNT(i), COALESCE(SUM(i.price * i.quantity), 0.0)) " +
            "FROM WarehouseModel w LEFT JOIN w.items i WHERE w.id = :warehouseId GROUP BY w.id, w.warehouseName")
    WarehouseSummary getWarehouseSummary(Integer warehouseId);
}
