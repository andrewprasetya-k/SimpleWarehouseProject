package org.warehouse.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.warehouse.Model.DigitalItemModel;
import org.warehouse.Model.ItemModel;
import org.warehouse.Model.PhysicalItemModel;
import org.warehouse.Service.ItemModelService;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemModelController {
    private final ItemModelService service;

    public ItemModelController(ItemModelService service){
        this.service = service;
    }

    //get
    @GetMapping
    public List<ItemModel> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemModel> findOne(@PathVariable int id){
        ItemModel item=service.findById(id);
        if (item==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(item);
    }

    //post
    @PostMapping("/physical")
    public PhysicalItemModel createPhysicalItem(@Valid @RequestBody PhysicalItemModel item){
        return (PhysicalItemModel) service.save(item);
    }

    @PostMapping("/digital")
    public DigitalItemModel createDigitalItem(@Valid @RequestBody DigitalItemModel item){
        return (DigitalItemModel) service.save(item);
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

}
