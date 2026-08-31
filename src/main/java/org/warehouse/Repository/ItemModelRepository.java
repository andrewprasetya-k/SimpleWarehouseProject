package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemModelRepository extends JpaRepository<org.warehouse.Model.ItemModel, Integer> {

}
