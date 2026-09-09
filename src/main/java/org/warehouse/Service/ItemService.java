package org.warehouse.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.warehouse.Dto.ItemResponse;
import org.warehouse.Dto.ItemDetailResponse;
import org.warehouse.Dto.ItemPagedResponse;
import org.warehouse.Dto.WarehouseResponse;
import org.warehouse.Event.ItemsMovedEvent;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Repository.ItemRepository;
import org.warehouse.Repository.WarehouseRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ItemPagedResponse findAll(Pageable pageable) {
        Page<ItemModel> paged = repo.findAll(pageable);
        List<ItemResponse> content = paged.getContent().stream().map(i -> new ItemResponse(i.getId(),i.getItemName(),i.getPrice(),i.getQuantity())).toList();
        return new ItemPagedResponse(content,paged.getNumber(),paged.getSize(), paged.getTotalElements(), paged.getTotalPages());
    }

    @Cacheable(value="items", key="#id")
    public ItemDetailResponse findById(Integer id) {
        ItemModel item = repo.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        WarehouseResponse warehouse = null;
        WarehouseModel itemWarehouse = item.getWarehouse();
        if (itemWarehouse != null) {
            warehouse = new WarehouseResponse(itemWarehouse.getId(), itemWarehouse.getWarehouseName(), itemWarehouse.getAddress());
        }
        return new ItemDetailResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity(), warehouse);
    }

    @CacheEvict(value = "items", allEntries = true)
    public ItemModel save(ItemModel itemModel) {
        return repo.save(itemModel);
    }

    @CacheEvict(value = "items", allEntries = true)
    public ItemModel update(Integer id, ItemModel itemModel) {
        if(!repo.existsById(id)){
            return null;
        }
        itemModel.setId(id);
        return repo.save(itemModel);
    }

    @CacheEvict(value = "items", allEntries = true)
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

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public ItemModel addQuantity(Integer id, Integer quantity) {
        if (quantity == null || quantity <= 0 || quantity > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be between 0 and 100");
        }
        ItemModel item = repo.findByIdForUpdate(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found " + id));
        item.setQuantity(item.getQuantity() + quantity);

        return repo.save(item);
    }

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public ItemModel decreaseQuantity(Integer id, Integer quantity) {
        if (quantity == null || quantity <= 0 || quantity > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be between 0 and 100");
        }
        ItemModel item = repo.findByIdForUpdate(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found " + id));
        if (item.getQuantity() - quantity < 0 ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for item " + id);
        }
        item.setQuantity(item.getQuantity() - quantity);

        return repo.save(item);
    }

    @Transactional
    @CacheEvict(value = "items", allEntries = true)
    public void moveItemsToWarehouse(Integer warehouseId, List<Integer> itemIds) {
        eventPublisher.publishEvent(
                new ItemsMovedEvent(warehouseId, itemIds)
        );

        WarehouseModel warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Warehouse not found " + warehouseId
                        )
                );

        List<ItemModel> items = repo.findAllById(itemIds);

        // cek missing id
        Set<Integer> foundIds = items.stream()
                .map(ItemModel::getId)
                .collect(Collectors.toSet());

        List<Integer> missingIds = itemIds.stream()
                .filter(id -> !foundIds.contains(id))
                .distinct()
                .toList();

        if (!missingIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Item not found " + missingIds
            );
        }

        // cek item sudah di warehouse
        Set<Integer> existingIds = repo.findByWarehouseId(warehouseId).stream()
                .map(ItemModel::getId)
                .collect(Collectors.toSet());

        List<Integer> overlappingIds = itemIds.stream()
                .filter(existingIds::contains)
                .distinct()
                .toList();

        if (!overlappingIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Item already in warehouse " + warehouseId + ": " + overlappingIds
            );
        }

        // Move items
        items.forEach(item -> item.setWarehouse(warehouse));

        repo.saveAll(items);
    }

}
