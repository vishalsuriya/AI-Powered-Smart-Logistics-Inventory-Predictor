package com.example.Vishalsuriya.ailogistics.controller;


import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> getAllInventories(){
       return inventoryService.getAllInventories();
    }

    @GetMapping("/{inventoryId}")
    public Inventory getInventoryById(@PathVariable  Long inventoryId){
        return inventoryService.getInventoryById(inventoryId);
    }

    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public Inventory getInventoryByProductIdAndWarehouseId(@PathVariable Long productId, @PathVariable Long warehouseId){
        return inventoryService.getInventoryByProductIdAndWarehouseId(productId, warehouseId);
    }

    @GetMapping("/product/{productId}")
    public List<Inventory> getByProductId(@PathVariable Long productId){
        return inventoryService.getByProductId(productId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<Inventory> getByWarehouseId(@PathVariable Long warehouseId){
        return inventoryService.getByWarehouseId(warehouseId);
    }

    @GetMapping("/exists/{productId}/{warehouseId}")
    public boolean existsByProductIdAndWarehouseId(@PathVariable Long productId, @PathVariable Long warehouseId){
        return inventoryService.existsByProductIdAndWarehouseId(productId, warehouseId);
    }

    @PostMapping
    public void addInventory(@RequestBody  Inventory inventory){
        inventoryService.addInventory(inventory);
    }

    @PutMapping("/{inventoryId}")
    public void updateInventory(@PathVariable Long inventoryId, @RequestBody Inventory inventory){
        inventoryService.updateInventory(inventoryId, inventory);
    }

    @DeleteMapping("/{inventoryId}")
    public void deleteInventory(@PathVariable Long inventoryId){
        inventoryService.deleteInventory(inventoryId);
    }
}
