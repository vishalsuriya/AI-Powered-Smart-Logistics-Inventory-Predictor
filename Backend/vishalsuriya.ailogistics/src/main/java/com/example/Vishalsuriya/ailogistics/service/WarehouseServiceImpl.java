package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import com.example.Vishalsuriya.ailogistics.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepo;

    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository) {
        this.warehouseRepo = warehouseRepository;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepo.findAll();
    }

    @Override
    public Warehouse getWarehouseById(final Long warehouseId) {
        return warehouseRepo.findById(warehouseId).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
    }

    @Override
    public Warehouse getWarehouseByWarehouseCode(final String warehouseCode) {
        return warehouseRepo.findByWarehouseCode(warehouseCode).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with code: " + warehouseCode));
    }

    @Override
    public String generateWarehouseCode() {
        final long nextId = warehouseRepo.count() + 1;
        return String.format("WH-%03d", nextId);
    }

    @Override
    @Transactional
    public void addWarehouse(final Warehouse warehouse) {
        validateForCreate(warehouse);
        final String warehouseCode = generateWarehouseCode();
         warehouse.setWarehouseCode(warehouseCode);
         warehouseRepo.save(warehouse);
    }

    @Override
    @Transactional
    public void updateWarehouse(final Long warehouseId, final Warehouse warehouse) {
        final Warehouse existingWarehouse = getWarehouseById(warehouseId);
        validateForUpdate(existingWarehouse, warehouse);
        existingWarehouse.setWarehouseName(warehouse.getWarehouseName());
        existingWarehouse.setAddress(warehouse.getAddress());
        existingWarehouse.setCity(warehouse.getCity());
        existingWarehouse.setCountry(warehouse.getCountry());
        existingWarehouse.setCapacity(warehouse.getCapacity());
        existingWarehouse.setEmail(warehouse.getEmail());
        existingWarehouse.setPhoneNumber(warehouse.getPhoneNumber());
        existingWarehouse.setState(warehouse.getState());
        existingWarehouse.setWarehouseManager(warehouse.getWarehouseManager());
        existingWarehouse.setWarehouseStatus(warehouse.getWarehouseStatus());
    }

    @Override
    @Transactional
    public void deleteWarehouse(final Long warehouseId) {
        final Warehouse warehouse = getWarehouseById(warehouseId);
        warehouseRepo.delete(warehouse);
    }

    @Override
    public boolean existsByWarehouseCode(final String warehouseCode) {
        return warehouseRepo.existsByWarehouseCode(warehouseCode);
    }

    @Override
    public boolean existsByWarehouseName(final String warehouseName) {
        return warehouseRepo.existsByWarehouseName(warehouseName);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return warehouseRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(final String phoneNumber) {
        return warehouseRepo.existsByPhoneNumber(phoneNumber);
    }

    private void validateForCreate(final Warehouse warehouse) {
        if (warehouseRepo.existsByEmail(warehouse.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (warehouseRepo.existsByPhoneNumber(warehouse.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        if (warehouseRepo.existsByWarehouseName(warehouse.getWarehouseName())) {
            throw new IllegalArgumentException("Warehouse name already exists.");
        }
    }

    private void validateForUpdate(final Warehouse existingWarehouse, final Warehouse updatedWarehouse) {

        if (!existingWarehouse.getEmail().equals(updatedWarehouse.getEmail())
                && warehouseRepo.existsByEmail(updatedWarehouse.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (!existingWarehouse.getPhoneNumber().equals(updatedWarehouse.getPhoneNumber())
                && warehouseRepo.existsByPhoneNumber(updatedWarehouse.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        if (!existingWarehouse.getWarehouseName().equals(updatedWarehouse.getWarehouseName())
                && warehouseRepo.existsByWarehouseName(updatedWarehouse.getWarehouseName())) {
            throw new IllegalArgumentException("Warehouse name already exists.");
        }
    }
}
