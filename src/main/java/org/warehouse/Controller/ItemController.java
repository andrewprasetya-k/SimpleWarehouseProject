package org.warehouse.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.warehouse.Dto.*;
import org.warehouse.Model.DigitalItemModel;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service){
        this.service = service;
    }

    //get
    @GetMapping
    public List<ItemModel> findAll(){
        return service.findAll();
    }

    @GetMapping("/search")
    public List<ItemModel> findByItemNameStartingWith(@RequestParam String name) {
        return service.findByItemNameStartingWith(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemModel> findOne(@PathVariable int id) {
        ItemModel item = service.findById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(item);
    }

    @GetMapping("/quantity/{quantity}")
    public List<ItemModel> findByQuantityGreaterThan(@PathVariable int quantity) {
        return service.findByQuantityGreaterThan(quantity);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<ItemSummaryResponse> findByWarehouseId(@PathVariable Integer warehouseId) {
        return service.findByWarehouseId(warehouseId)
                .stream()
                .map(item -> new ItemSummaryResponse(item.getId(), item.getItemName(), item.getPrice(), item.getQuantity()))
                .toList();
    }

    @GetMapping("/physical/search")
    public List<PhysicalItemModel> findPhysicalItemsByItemName(@RequestParam String name) {
        return service.findPhysicalItemsByItemName(name);
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
