package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Warehouse;

import java.util.List;

public interface WarehouseService {

    List<Warehouse> getAllWarehouses();

    Warehouse getWarehouseById(Long warehouseId);

    Warehouse getWarehouseByWarehouseCode(String warehouseCode);

    String generateWarehouseCode();

    void addWarehouse(Warehouse warehouse);

    void updateWarehouse(Long warehouseId, Warehouse warehouse);

    void deleteWarehouse(Long warehouseId);

    boolean existsByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseName(String warehouseName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}