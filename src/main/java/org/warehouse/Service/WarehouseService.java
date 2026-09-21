package org.warehouse.Service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.warehouse.Dto.WarehousePagedResponse;
import org.warehouse.Dto.WarehouseResponse;
import org.warehouse.Model.WarehouseModel;
import org.warehouse.Repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseService {
    private final WarehouseRepository repo;

    public WarehouseService(WarehouseRepository repo) {
        this.repo = repo;
    }

    @Cacheable(value="warehouse-pages", key="#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public WarehousePagedResponse findAll(Pageable pageable) {
        Page<WarehouseModel> paged = repo.findAll(pageable);
        List<WarehouseResponse> content = new ArrayList<>();
        for (WarehouseModel w : paged.getContent()) {
            content.add(new WarehouseResponse(w.getId(),w.getWarehouseName(),w.getAddress()));
        }
        return new WarehousePagedResponse(content, paged.getNumber(),paged.getSize(),paged.getTotalElements(),paged.getTotalPages());
    }

    @Cacheable(value="warehouses", key="#id")
    public WarehouseModel findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "warehouses", allEntries = true),
            @CacheEvict(value = "warehouse-pages", allEntries = true),
            @CacheEvict(value = "items", allEntries = true)
    })
    public WarehouseModel save(WarehouseModel warehouse) {
        return repo.save(warehouse);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "warehouses", allEntries = true),
            @CacheEvict(value = "warehouse-pages", allEntries = true),
            @CacheEvict(value = "items", allEntries = true)
    })
    public WarehouseModel update(Integer id, WarehouseModel warehouse) {
        if (!repo.existsById(id)) {
            return null;
        }
        warehouse.setId(id);
        return repo.save(warehouse);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "warehouses", allEntries = true),
            @CacheEvict(value = "warehouse-pages", allEntries = true),
            @CacheEvict(value = "items", allEntries = true)
    })
    public boolean delete(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
