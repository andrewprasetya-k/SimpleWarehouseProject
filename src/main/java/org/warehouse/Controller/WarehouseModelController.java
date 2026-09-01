package org.warehouse.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.warehouse.Dto.WarehouseRequest;
import org.warehouse.Dto.WarehouseResponse;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Service.WarehouseModelService;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseModelController {
    private final WarehouseModelService service;

    public WarehouseModelController(WarehouseModelService service) {
        this.service = service;
    }

    @GetMapping
    public List<WarehouseResponse> findAll() {
        return service.findAll().stream()
            .map(w -> new WarehouseResponse(w.getId(), w.getWarehouseName(), w.getAddress()))
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> findOne(@PathVariable int id) {
        WarehouseModel warehouse = service.findById(id);
        if (warehouse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new WarehouseResponse(warehouse.getId(), warehouse.getWarehouseName(), warehouse.getAddress()));
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
