package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.Warehouse;

import java.util.List;

public interface InventoryService {

    List<Inventory> getAllInventories();

    Inventory getInventoryById(Long inventoryId);

    Inventory getInventoryByProductIdAndWarehouseId(
            Long productId,
            Long warehouseId
    );

    List<Inventory> getByProductId(Long productId);

    List<Inventory> getByWarehouseId(Long warehouseId);

    boolean existsByProductAndWarehouse(
            Product product,
            Warehouse warehouse
    );

    boolean existsByProductIdAndWarehouseId(
            Long productId,
            Long warehouseId
    );

    void addInventory(Inventory inventory);

    void updateInventory(Long inventoryId, Inventory inventory);

    void deleteInventory(Long inventoryId);

    List<Inventory> getLowStockInventories(Integer threshold);
}
