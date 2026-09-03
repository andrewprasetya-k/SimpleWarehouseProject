package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.warehouse.Model.WarehouseModel;

public interface WarehouseRepository extends JpaRepository<WarehouseModel, Integer> {

}
