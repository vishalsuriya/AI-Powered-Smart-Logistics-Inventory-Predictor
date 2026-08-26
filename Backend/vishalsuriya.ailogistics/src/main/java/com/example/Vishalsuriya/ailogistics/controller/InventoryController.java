package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories()
        );
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable final Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId)
        );
    }

    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public ResponseEntity<Inventory> getInventoryByProductIdAndWarehouseId(
            @PathVariable final Long productId,
            @PathVariable final Long warehouseId) {

        return ResponseEntity.ok(inventoryService.getInventoryByProductIdAndWarehouseId(productId, warehouseId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Inventory>> getByProductId(@PathVariable final Long productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Inventory>> getByWarehouseId(@PathVariable final Long warehouseId) {
        return ResponseEntity.ok(inventoryService.getByWarehouseId(warehouseId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockInventories(@RequestParam final Integer threshold) {

        return ResponseEntity.ok(inventoryService.getLowStockInventories(threshold));
    }

    @PostMapping
    public ResponseEntity<Void> addInventory(@RequestBody final Inventory inventory) {

        inventoryService.addInventory(inventory);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<Void> updateInventory(@PathVariable final Long inventoryId, @RequestBody final Inventory inventory) {
        inventoryService.updateInventory(inventoryId, inventory);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable final Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }
}