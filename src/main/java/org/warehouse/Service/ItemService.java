package org.warehouse.Service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.warehouse.Event.ItemsMovedEvent;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Repository.ItemRepository;
import org.warehouse.Repository.WarehouseRepository;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository repo;
    private final WarehouseRepository warehouseRepo;
    private final ApplicationEventPublisher eventPublisher;

    public ItemService(ItemRepository repo, WarehouseRepository warehouseRepo,  ApplicationEventPublisher eventPublisher) {
        this.repo = repo;
        this.warehouseRepo = warehouseRepo;
        this.eventPublisher = eventPublisher;
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

    //for transaction
    @Transactional
    public void moveItemsToWarehouse(Integer warehouseId, List<Integer> itemId) {
        WarehouseModel warehouse=warehouseRepo.findById(warehouseId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Warehouse not found " + warehouseId));

        for (Integer itm:itemId){
            ItemModel item=repo.findById(itm).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found " +  itm));
            item.setWarehouse(warehouse);
            this.save(item);
        }

        eventPublisher.publishEvent(new ItemsMovedEvent(warehouseId,itemId));
    }
}