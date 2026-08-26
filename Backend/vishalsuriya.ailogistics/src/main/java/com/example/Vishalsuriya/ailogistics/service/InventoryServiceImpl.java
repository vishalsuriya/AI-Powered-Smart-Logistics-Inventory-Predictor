package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Inventory;
import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import com.example.Vishalsuriya.ailogistics.repository.InventoryRepository;
import com.example.Vishalsuriya.ailogistics.repository.ProductRepository;
import com.example.Vishalsuriya.ailogistics.repository.WarehouseRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;

    public InventoryServiceImpl(final InventoryRepository inventoryRepository,
                                final ProductRepository productRepo,
                                final WarehouseRepository warehouseRepo) {
        this.inventoryRepo = inventoryRepository;
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepo.findAll();
    }

    @Override
    public Inventory getInventoryById(final Long inventoryId) {
        return inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found with ID: " + inventoryId));
    }

    @Override
    public Inventory getInventoryByProductIdAndWarehouseId(final Long productId, final Long warehouseId) {
        return inventoryRepo.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found with productID: "
                        + productId + " and warehouseID: " + warehouseId));
    }

    @Override
    public List<Inventory> getByProductId(final Long productId) {
        return inventoryRepo.findByProductId(productId);
    }

    @Override
    public List<Inventory> getByWarehouseId(final Long warehouseId) {
        return inventoryRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public boolean existsByProductAndWarehouse(final Product product, final Warehouse warehouse) {
        return inventoryRepo.existsByProductAndWarehouse(product, warehouse);
    }

    @Override
    public boolean existsByProductIdAndWarehouseId(final Long productId, final Long warehouseId) {
        return inventoryRepo.existsByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Override
    @Transactional
    public void addInventory(final Inventory inventory) {
        if (inventory.getProduct() == null || inventory.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product ID must be provided");
        }
        if (inventory.getWarehouse() == null || inventory.getWarehouse().getId() == null) {
            throw new IllegalArgumentException("Warehouse ID must be provided");
        }

        final Long productId = inventory.getProduct().getId();
        final Long warehouseId = inventory.getWarehouse().getId();

        validateForCreate(productId, warehouseId);

        final Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        final Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);

        inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public void updateInventory(final Long inventoryId, final Inventory inventory) {
        final Inventory existingInventory = findInventory(inventoryId);
        validateForUpdate(inventory);
        existingInventory.setQuantityOnHand(inventory.getQuantityOnHand());
        existingInventory.setDamagedStock(inventory.getDamagedStock());
        existingInventory.setMinimumThreshold(inventory.getMinimumThreshold());
        existingInventory.setReorderQuantity(inventory.getReorderQuantity());
        existingInventory.setReservedStock(inventory.getReservedStock());
    }

    @Override
    @Transactional
    public void deleteInventory(final Long inventoryId) {
        final Inventory inventory = getInventoryById(inventoryId);
        inventoryRepo.delete(inventory);
    }

    @Override
    public List<Inventory> getLowStockInventories(final Integer threshold) {
        return inventoryRepo.findByQuantityOnHandLessThanEqual(threshold);
    }

    private void validateForCreate(final Long productId, final Long warehouseId) {
        final boolean exists = existsByProductIdAndWarehouseId(productId, warehouseId);
        if (exists) {
            throw new EntityExistsException(
                    "Inventory already exists for Product ID: " + productId + " and Warehouse ID: " + warehouseId
            );
        }
    }

    private void validateForUpdate(final Inventory inventory) {
        if (inventory.getMinimumThreshold() == null) {
            throw new IllegalArgumentException("Minimum threshold is required");
        }

        final int reserved = inventory.getReservedStock() == null ? 0 : inventory.getReservedStock();
        final int damaged = inventory.getDamagedStock() == null ? 0 : inventory.getDamagedStock();
        final int onHand = inventory.getQuantityOnHand() == null ? 0 : inventory.getQuantityOnHand();

        if (reserved + damaged > onHand) {
            throw new IllegalArgumentException(
                    "Reserved stock plus damaged stock (" + (reserved + damaged) +
                            ") cannot exceed quantity on hand (" + onHand + ")"
            );
        }
    }

    private Inventory findInventory(final Long inventoryId) {
        return inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found with ID: " + inventoryId));
    }
}