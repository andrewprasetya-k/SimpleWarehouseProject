package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Repository.ItemModelRepository;

import java.util.List;

@Service
public class ItemModelService {

    private final ItemModelRepository repo;

    public ItemModelService(ItemModelRepository repo) {
        this.repo = repo;
    }

    public List<ItemModel> findAll() {
        return repo.findAll();
    }

    public ItemModel findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public ItemModel save(ItemModel itemModel) {
        return repo.save(itemModel);
    }

    public ItemModel update(Integer id, ItemModel itemModel) {
        if(!repo.existsById(id)){
            return null;
        }
        itemModel.setId(id);
        return repo.save(itemModel);
    }

    public boolean delete(Integer id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    //appended repo
    public List<ItemModel> findByQuantityGreaterThan(int quantity) {
        return repo.findByQuantityGreaterThan(quantity);
    }

    public List<ItemModel> findByWarehouseId(Integer warehouseId) {
        return repo.findByWarehouseId(warehouseId);
    }

    public List<PhysicalItemModel> findPhysicalItemsByItemName(String keyword) {
        return repo.findPhysicalItemsByItemName(keyword);
    }
}