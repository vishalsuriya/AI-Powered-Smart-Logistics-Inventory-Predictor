package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import com.example.Vishalsuriya.ailogistics.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepo;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository){
        this.warehouseRepo = warehouseRepository;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepo.findAll();
    }

    @Override
    public Warehouse getWarehouseById(Long warehouseId) {
        return warehouseRepo.findById(warehouseId).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
    }

    @Override
    public Warehouse getWarehouseByWarehouseCode(String warehouseCode) {
        return warehouseRepo.findByWarehouseCode(warehouseCode).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with code: " + warehouseCode));
    }

    @Override
    public String generateWarehouseCode() {
        long nextId = warehouseRepo.count() + 1;
        return String.format("WH-%03d", nextId);
    }

    @Override
    public void addWarehouse(Warehouse warehouse) {
        String warehouseCode = generateWarehouseCode();
         warehouse.setWarehouseCode(warehouseCode);
         warehouseRepo.save(warehouse);
    }

    @Override
    public void updateWarehouse(Long warehouseId, Warehouse warehouse) {
      Warehouse existingWarehouse = warehouseRepo.findById(warehouseId).
              orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
        existingWarehouse.setWarehouseName(warehouse.getWarehouseName());
        existingWarehouse.setAddress(warehouse.getAddress());
        existingWarehouse.setCity(warehouse.getCity());
        existingWarehouse.setCountry(warehouse.getCountry());
        existingWarehouse.setCapacity(warehouse.getCapacity());
        existingWarehouse.setEmail(warehouse.getEmail());
        existingWarehouse.setPhoneNumber(warehouse.getPhoneNumber());
        existingWarehouse.setState(warehouse.getState());
        existingWarehouse.setManagerName(warehouse.getManagerName());
        existingWarehouse.setWarehouseStatus(warehouse.getWarehouseStatus());
        warehouseRepo.save(existingWarehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
      warehouseRepo.deleteById(id);
    }
}
