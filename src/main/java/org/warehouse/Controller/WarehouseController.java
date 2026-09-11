package org.warehouse.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.warehouse.Dto.*;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Service.ItemService;
import org.warehouse.Service.WarehouseService;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
    private final WarehouseService service;
    private final ItemService itemService;

    public WarehouseController(WarehouseService service, ItemService itemService) {
        this.service = service;
        this.itemService = itemService;
    }

    @GetMapping
    public WarehousePagedResponse findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    /** Returns warehouse detail (name, address) without loading its items collection. */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> findOne(@PathVariable int id) {
        WarehouseModel warehouse = service.findById(id);
        if (warehouse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new WarehouseResponse(warehouse.getId(), warehouse.getWarehouseName(), warehouse.getAddress()));
    }

    /**
     * Paginated list of items belonging to this warehouse.
     * Kept as a separate endpoint so warehouse metadata can be fetched cheaply
     * and items are only loaded on demand with pagination.
     * Example: GET /warehouses/1/items?page=0&size=20
     */
    @GetMapping("/{id}/items")
    public ItemPagedResponse findItemsByWarehouse(@PathVariable int id, Pageable pageable) {
        return itemService.findByWarehouseId(id, pageable);
    }

    @PostMapping
    public WarehouseResponse create(@Valid @RequestBody WarehouseRequest request) {
        WarehouseModel entity = new WarehouseModel(null, request.warehouseName(), request.address(), null);
        WarehouseModel saved = service.save(entity);
        return new WarehouseResponse(saved.getId(), saved.getWarehouseName(), saved.getAddress());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> update(@PathVariable int id, @Valid @RequestBody WarehouseRequest request) {
        WarehouseModel entity = new WarehouseModel(null, request.warehouseName(), request.address(), null);
        WarehouseModel updated = service.update(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new WarehouseResponse(updated.getId(), updated.getWarehouseName(), updated.getAddress()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean isDeleted = service.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }
}
