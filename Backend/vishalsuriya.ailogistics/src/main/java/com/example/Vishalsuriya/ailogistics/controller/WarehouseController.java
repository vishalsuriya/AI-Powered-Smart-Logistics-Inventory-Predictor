package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import com.example.Vishalsuriya.ailogistics.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService){
        this.warehouseService  = warehouseService;
    }

    @GetMapping
    public List<Warehouse> getAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }

    @GetMapping("/{warehouseId}")
    public Warehouse getWarehouseById(@PathVariable Long warehouseId){
        return warehouseService.getWarehouseById(warehouseId);
    }

    @GetMapping("/code/{warehouseCode}")
    public Warehouse getWarehouseByWarehouseCode(@PathVariable String warehouseCode){
        return warehouseService.getWarehouseByWarehouseCode(warehouseCode);
    }

    @PostMapping
    public void addWarehouse(@RequestBody Warehouse warehouse){
        warehouseService.addWarehouse(warehouse);
    }

    @PutMapping("/{warehouseId}")
    public void updateWarehouse(@PathVariable Long warehouseId, @RequestBody Warehouse warehouse){
        warehouseService.updateWarehouse(warehouseId,warehouse);
    }

    @DeleteMapping("/{warehouseId}")
    public void deleteWarehouse(@PathVariable Long warehouseId){
        warehouseService.deleteWarehouse(warehouseId);
    }
}
