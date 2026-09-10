package org.warehouse.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.warehouse.Dto.*;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;
import org.warehouse.Model.DigitalItemModel;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Service.ItemService;
import org.warehouse.Kafka.ItemHistoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final ItemHistoryService itemHistoryService;

    public ItemController(ItemService service,  ItemHistoryService itemHistoryService) {
        this.service = service;
        this.itemHistoryService = itemHistoryService;
    }

    //get
    @GetMapping
    public ItemPagedResponse findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/search")
    public List<ItemResponse> findByItemNameStartingWith(@RequestParam String name) {
        return service.findByItemNameStartingWith(name).stream().map(i -> new ItemResponse(i.getId(), i.getItemName(), i.getPrice(), i.getQuantity())).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailResponse> findOne(@PathVariable int id) {
        ItemDetailResponse item = service.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    @GetMapping("/quantity/{quantity}")
    public List<ItemResponse> findByQuantityGreaterThan(@PathVariable int quantity) {
        List<ItemModel> items = service.findByQuantityGreaterThan(quantity);
        List<ItemResponse> response = new ArrayList<>();
        for (ItemModel item : items) {
            response.add(new ItemResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity()));
        }
        return response;
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<ItemResponse> findByWarehouseId(@PathVariable Integer warehouseId) {
        return service.findByWarehouseId(warehouseId)
                .stream()
                .map(item -> new ItemResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity()))
                .toList();
    }

    @GetMapping("/physical/search")
    public List<PhysicalItemModel> findPhysicalItemsByItemName(@RequestParam String name) {
        return service.findPhysicalItemsByItemName(name);
    }

    //kafka endpoint
    @GetMapping("/warehouse/history/{warehouseId}")
    public ItemMoveHistoryPagedResponse<ItemsMovedKafkaMessage> getMoveHistory(
            @PathVariable int warehouseId,
            Pageable pageable
    ) {
        return itemHistoryService.getMoveItemHistoryByWarehouseId(warehouseId, pageable);
    }

    //post
    @PostMapping("/physical")
    public PhysicalItemResponse createPhysicalItem(@Valid @RequestBody PhysicalItemRequest request){
        //dto mapping
        PhysicalItemModel entity=new PhysicalItemModel(null, request.itemName(), request.quantity(), request.price(), request.weight());
        //untuk save ke db
        PhysicalItemModel saved=(PhysicalItemModel) service.save(entity);
        //mapping ke response dto untuk menjadi response
        return new PhysicalItemResponse(saved.getId(),saved.getItemName(), saved.getQuantity(),saved.getPrice(),saved.getWeight());
    }

    @PostMapping("/digital")
    public DigitalItemResponse createDigitalItem(@Valid @RequestBody DigitalItemRequest request){
        //dto mapping
        DigitalItemModel entity=new DigitalItemModel(null, request.itemName(), request.quantity(), request.price(), request.isLicensed());
        //untuk save ke db
        DigitalItemModel saved=(DigitalItemModel) service.save(entity);
        //mapping ke response dto untuk menjadi response
        return new DigitalItemResponse(saved.getId(),saved.getItemName(), saved.getQuantity(),saved.getPrice(),saved.isLisenced());
    }

    //put
    @PutMapping("/physical/{id}")
    public ResponseEntity<PhysicalItemModel> updatePhysicalItem(@PathVariable int id, @RequestBody PhysicalItemModel item){
        ItemModel updated =service.update(id, item);
        if (updated == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((PhysicalItemModel) updated);
    }

    @PutMapping("/digital/{id}")
    public ResponseEntity<DigitalItemModel> updateDigitalItem(@PathVariable int id, @RequestBody DigitalItemModel item){
        ItemModel updated =service.update(id, item);
        if (updated == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok((DigitalItemModel) updated);
    }

    @PutMapping("/add-quantity/{id}")
    public ResponseEntity<ItemResponse> addQuantity(@PathVariable int id, @RequestParam int quantity){
        ItemModel item=service.addQuantity(id, quantity);
        return ResponseEntity.ok(new ItemResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity()));
    }

    @PutMapping("/decrease-quantity/{id}")
    public ResponseEntity<ItemResponse> decreaseQuantity(@PathVariable int id, @RequestParam int quantity){
        ItemModel item=service.decreaseQuantity(id, quantity);
        return ResponseEntity.ok(new ItemResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity()));
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePhysicalItem(@PathVariable int id){
        boolean isDeleted=service.delete(id);
        if (isDeleted){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    //bulk move
    @PutMapping("/warehouse/move/{warehouseId}")
    public void moveItemsToWarehouse(@PathVariable int warehouseId, @RequestBody List<Integer> itemIds){
        service.moveItemsToWarehouse(warehouseId,itemIds);
    }

}
