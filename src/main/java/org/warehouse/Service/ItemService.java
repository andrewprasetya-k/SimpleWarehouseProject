package org.warehouse.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.warehouse.Dto.ItemDetailResponse;
import org.warehouse.Dto.ItemResponse;
import org.warehouse.Event.ItemsMovedEvent;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Repository.ItemRepository;
import org.warehouse.Repository.WarehouseRepository;

import java.util.ArrayList;
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

    @Cacheable(value="items", key="#pageable.pageNumber + '-' + #pageable.pageSize")
    public ItemResponse findAll(Pageable pageable) {
        Page<ItemModel> paged = repo.findAll(pageable);
        List<ItemDetailResponse> content = paged.getContent().stream().map(i -> new ItemDetailResponse(i.getId(),i.getItemName(),i.getPrice(),i.getQuantity())).toList();
        return new ItemResponse(content,paged.getNumber(),paged.getSize(), paged.getTotalElements(), paged.getTotalPages());
    }

    @Cacheable(value="items", key="#id")
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

    public List<ItemModel> findByItemNameStartingWith(String itemName) {
        return repo.findByItemNameStartingWith(itemName);
    }

    //for transaction
    @Transactional
    public void moveItemsToWarehouse(Integer warehouseId, List<Integer> itemId) {
        WarehouseModel warehouse=warehouseRepo.findById(warehouseId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Warehouse not found " + warehouseId));
        eventPublisher.publishEvent(new ItemsMovedEvent(warehouseId,itemId));

        for (Integer itm:itemId){
            ItemModel item=repo.findById(itm).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found " +  itm));
            item.setWarehouse(warehouse);
            this.save(item);
            System.out.println("Moved " + itm + " to warehouse " + warehouse);
        }
    }
}