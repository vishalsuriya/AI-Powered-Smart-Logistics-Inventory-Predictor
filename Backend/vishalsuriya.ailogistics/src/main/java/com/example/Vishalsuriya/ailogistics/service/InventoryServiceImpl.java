package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.repository.InventoryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepo;

    public InventoryServiceImpl(InventoryRepository inventoryRepository){
        this.inventoryRepo = inventoryRepository;
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepo.findAll();
    }

    @Override
    public Inventory getInventoryById(Long inventoryId) {
        return inventoryRepo.findById(inventoryId).
                orElseThrow(()-> new EntityNotFoundException("Inventory not found with ID: " + inventoryId));
    }

    @Override
    public Inventory getInventoryByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return inventoryRepo.findByProductIdAndWarehouseId(productId,warehouseId).
                orElseThrow(()-> new EntityNotFoundException("Inventory not found " +
                        "with productID: " + productId + "and warehouseID: " + warehouseId));
    }

    @Override
    public List<Inventory> getByProductId(Long productId) {
        return inventoryRepo.findByProductId(productId);
    }

    @Override
    public List<Inventory> getByWarehouseId(Long warehouseId) {
        return inventoryRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return inventoryRepo.existsByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Override
    public void addInventory(Inventory inventory) {
        boolean exists = inventoryRepo.existsByProductIdAndWarehouseId(
                inventory.getProductId(),
                inventory.getWarehouseId()
        );
        if (exists) {
            throw new EntityExistsException(
                    "Inventory already exists for Product ID: " + inventory.getProductId() +
                            " and Warehouse ID: " + inventory.getWarehouseId()
            );
        }

        inventoryRepo.save(inventory);
    }

    @Override
    public void updateInventory(Long inventoryId, Inventory inventory) {
          Inventory existingInventory = inventoryRepo.findById(inventoryId).
                  orElseThrow(()-> new EntityNotFoundException("Inventory not found with ID: " + inventoryId));
        existingInventory.setAvailableStock(inventory.getAvailableStock());
        existingInventory.setDamagedStock(inventory.getDamagedStock());
        existingInventory.setMinimumThreshold(inventory.getMinimumThreshold());
        existingInventory.setReorderQuantity(inventory.getReorderQuantity());
        existingInventory.setReservedStock(inventory.getReservedStock());
        inventoryRepo.save(existingInventory);
    }

    @Override
    public void deleteInventory(Long inventoryId) {
        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory " +
                        "not found with ID: " + inventoryId));
        inventoryRepo.delete(inventory);
    }
}
