package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.warehouse.WarehouseResponseDTO;
import com.example.Vishalsuriya.ailogistics.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(final WarehouseService warehouseService) {
        this.warehouseService  = warehouseService;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDTO>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(@PathVariable final Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseId));
    }

    @GetMapping("/code/{warehouseCode}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseByWarehouseCode(@PathVariable final String warehouseCode) {
        return ResponseEntity.ok(warehouseService.getWarehouseByWarehouseCode(warehouseCode));
    }

    @PostMapping
    public ResponseEntity<Void> addWarehouse(@Valid @RequestBody final WarehouseRequestDTO warehouseRequestDTO) {
        warehouseService.addWarehouse(warehouseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<Void> updateWarehouse(@PathVariable final Long warehouseId, @Valid @RequestBody final WarehouseRequestDTO warehouseRequestDTO) {
        warehouseService.updateWarehouse(warehouseId, warehouseRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable final Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseEntity.noContent().build();
    }

}
