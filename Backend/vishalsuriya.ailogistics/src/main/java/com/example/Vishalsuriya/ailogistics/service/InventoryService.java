package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.Warehouse;

import java.util.List;

public interface InventoryService {

    public List<Inventory> getAllInventories();

    public Inventory getInventoryById(Long inventoryId);

    public Inventory getInventoryByProductIdAndWarehouseId(Long productId, Long warehouseId);

    public List<Inventory> getByProductId(Long productId);

    public List<Inventory> getByWarehouseId(Long warehouseId);

    public boolean existsByProductIdAndWarehouseId(Product productId, Warehouse warehouseId);

    public void addInventory(Inventory inventory);

    public void updateInventory(Long inventoryId, Inventory inventory);

    public void deleteInventory(Long inventoryId);
}
