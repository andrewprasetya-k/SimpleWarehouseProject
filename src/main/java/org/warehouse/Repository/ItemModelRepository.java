package org.warehouse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.warehouse.Model.ItemModel;

@Repository
public interface ItemModelRepository extends JpaRepository<ItemModel, Integer> {
// 1. Fundamental spring
// 2. repository ada joinnya
// 3. extend poin 2 -> join pake 3 konsep (interface tanpa specify db query, query pake jpql (object java), query biasa)
// 4. cara kerja transaksional spesifik
}