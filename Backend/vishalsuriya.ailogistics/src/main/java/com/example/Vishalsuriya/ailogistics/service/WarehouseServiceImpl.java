package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseResponseDTO;
import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import com.example.Vishalsuriya.ailogistics.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepo;

    public WarehouseServiceImpl(final WarehouseRepository warehouseRepository) {
        this.warehouseRepo = warehouseRepository;
    }

    private WarehouseResponseDTO mapToResponseDTO(final Warehouse warehouse) {
        final WarehouseResponseDTO warehouseResponseDTO = new WarehouseResponseDTO();
        warehouseResponseDTO.setAddress(warehouse.getAddress());
        warehouseResponseDTO.setCapacity(warehouse.getCapacity());
        warehouseResponseDTO.setCity(warehouse.getCity());
        warehouseResponseDTO.setCountry(warehouse.getCountry());
        warehouseResponseDTO.setEmail(warehouse.getEmail());
        warehouseResponseDTO.setPhoneNumber(warehouse.getPhoneNumber());
        warehouseResponseDTO.setWarehouseCode(warehouse.getWarehouseCode());
        warehouseResponseDTO.setWarehouseManager(warehouse.getWarehouseManager());
        warehouseResponseDTO.setWarehouseName(warehouse.getWarehouseName());
        warehouseResponseDTO.setState(warehouse.getState());
        warehouseResponseDTO.setWarehouseStatus(warehouse.getWarehouseStatus());
        return warehouseResponseDTO;
    }

    @Override
    public List<WarehouseResponseDTO> getAllWarehouses() {
        final List<Warehouse> warehouses = warehouseRepo.findAll();
        final List<WarehouseResponseDTO> warehouseResponseDTOList = new ArrayList<>();
        for (final Warehouse warehouse : warehouses) {
            warehouseResponseDTOList.add(mapToResponseDTO(warehouse));
        }
        return warehouseResponseDTOList;
    }

    @Override
    public WarehouseResponseDTO getWarehouseById(final Long warehouseId) {
        final Warehouse warehouse = warehouseRepo.findById(warehouseId).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
        return mapToResponseDTO(warehouse);
    }

    @Override
    public WarehouseResponseDTO getWarehouseByWarehouseCode(final String warehouseCode) {
        final Warehouse warehouse = warehouseRepo.findByWarehouseCode(warehouseCode).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with code: " + warehouseCode));
        return mapToResponseDTO(warehouse);
    }

    @Override
    @Transactional
    public void addWarehouse(final WarehouseRequestDTO warehouseRequestDTO) {
        validateForCreate(warehouseRequestDTO);
        final Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseManager(warehouseRequestDTO.getWarehouseManager());
        warehouse.setAddress(warehouseRequestDTO.getAddress());
        warehouse.setCapacity(warehouseRequestDTO.getCapacity());
        warehouse.setCity(warehouseRequestDTO.getCity());
        warehouse.setEmail(warehouseRequestDTO.getEmail());
        warehouse.setPhoneNumber(warehouseRequestDTO.getPhoneNumber());
        warehouse.setState(warehouseRequestDTO.getState());
        warehouse.setWarehouseName(warehouseRequestDTO.getWarehouseName());
        warehouse.setCountry(warehouseRequestDTO.getCountry());
        warehouse.setWarehouseCode("TEMP-" + UUID.randomUUID());
        final Warehouse savedWarehouse = warehouseRepo.save(warehouse);
        final String warehouseCode = String.format("WH-%03d", savedWarehouse.getId());
        savedWarehouse.setWarehouseCode(warehouseCode);
        warehouseRepo.save(savedWarehouse);
    }

    @Override
    @Transactional
    public void updateWarehouse(final Long warehouseId, final WarehouseRequestDTO warehouseRequestDTO) {
        final Warehouse existingWarehouse = getExistingWarehouseById(warehouseId);
        validateForUpdate(existingWarehouse, warehouseRequestDTO);
        existingWarehouse.setWarehouseName(warehouseRequestDTO.getWarehouseName());
        existingWarehouse.setAddress(warehouseRequestDTO.getAddress());
        existingWarehouse.setCity(warehouseRequestDTO.getCity());
        existingWarehouse.setCountry(warehouseRequestDTO.getCountry());
        existingWarehouse.setCapacity(warehouseRequestDTO.getCapacity());
        existingWarehouse.setEmail(warehouseRequestDTO.getEmail());
        existingWarehouse.setPhoneNumber(warehouseRequestDTO.getPhoneNumber());
        existingWarehouse.setState(warehouseRequestDTO.getState());
        existingWarehouse.setWarehouseManager(warehouseRequestDTO.getWarehouseManager());
    }

    @Override
    @Transactional
    public void deleteWarehouse(final Long warehouseId) {
        final Warehouse warehouse = getExistingWarehouseById(warehouseId);
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

    private void validateForCreate(final WarehouseRequestDTO warehouseRequestDTO) {
        if (warehouseRepo.existsByEmail(warehouseRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (warehouseRepo.existsByPhoneNumber(warehouseRequestDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }
        if (warehouseRepo.existsByWarehouseName(warehouseRequestDTO.getWarehouseName())) {
            throw new IllegalArgumentException("Warehouse name already exists.");
        }
    }

    private void validateForUpdate(final Warehouse existingWarehouse,
                                   final WarehouseRequestDTO updatedWarehouse) {

        if (!Objects.equals(existingWarehouse.getEmail(), updatedWarehouse.getEmail())
                && warehouseRepo.existsByEmail(updatedWarehouse.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (!Objects.equals(existingWarehouse.getPhoneNumber(), updatedWarehouse.getPhoneNumber())
                && warehouseRepo.existsByPhoneNumber(updatedWarehouse.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        if (!Objects.equals(existingWarehouse.getWarehouseName(), updatedWarehouse.getWarehouseName())
                && warehouseRepo.existsByWarehouseName(updatedWarehouse.getWarehouseName())) {
            throw new IllegalArgumentException("Warehouse name already exists.");
        }
    }

    private Warehouse getExistingWarehouseById(final Long warehouseId) {
        return warehouseRepo.findById(warehouseId).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + warehouseId));
    }
}
