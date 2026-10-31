package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseResponseDTO;

import java.util.List;

public interface WarehouseService {

    List<WarehouseResponseDTO> getAllWarehouses();

    WarehouseResponseDTO getWarehouseById(Long warehouseId);

    WarehouseResponseDTO getWarehouseByWarehouseCode(String warehouseCode);

    void addWarehouse(WarehouseRequestDTO warehouseRequestDTO);

    void updateWarehouse(Long warehouseId, WarehouseRequestDTO warehouseRequestDTO);

    void deleteWarehouse(Long warehouseId);

    boolean existsByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseName(String warehouseName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}