package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;
import com.example.Vishalsuriya.ailogistics.service.PurchaseOrderHeaderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderHeaderController {

    private final PurchaseOrderHeaderService purchaseOrderHeaderService;

    public PurchaseOrderHeaderController(
            final PurchaseOrderHeaderService purchaseOrderHeaderService) {
        this.purchaseOrderHeaderService = purchaseOrderHeaderService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderHeader>> getAllPurchaseOrderHeaders() {
        return ResponseEntity.ok(
                purchaseOrderHeaderService.getAllPurchaseOrderHeaders()
        );
    }

    @GetMapping("/{purchaseOrderHeaderId}")
    public ResponseEntity<PurchaseOrderHeader> getPurchaseOrderHeaderById(
            @PathVariable final Long purchaseOrderHeaderId) {

        return ResponseEntity.ok(
                purchaseOrderHeaderService.getPurchaseOrderHeaderById(purchaseOrderHeaderId)
        );
    }

    @GetMapping("/trx/{trxNumber}")
    public ResponseEntity<PurchaseOrderHeader> getPurchaseOrderHeaderByTrxNumber(
            @PathVariable final String trxNumber) {

        return ResponseEntity.ok(
                purchaseOrderHeaderService.getPurchaseOrderHeaderByTrxNumber(trxNumber)
        );
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<PurchaseOrderHeader>> getPurchaseOrderHeadersByVendorId(
            @PathVariable final Long vendorId) {

        return ResponseEntity.ok(
                purchaseOrderHeaderService.getPurchaseOrderHeadersByVendorId(vendorId)
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<PurchaseOrderHeader>> getPurchaseOrderHeadersByWarehouseId(
            @PathVariable final Long warehouseId) {

        return ResponseEntity.ok(
                purchaseOrderHeaderService.getPurchaseOrderHeadersByWarehouseId(warehouseId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseOrderHeader>> getPurchaseOrderHeadersByStatus(
            @PathVariable final PurchaseOrderStatus status) {

        return ResponseEntity.ok(
                purchaseOrderHeaderService.getPurchaseOrderHeadersByStatus(status)
        );
    }

    @PostMapping
    public ResponseEntity<Void> addPurchaseOrderHeader(
            @Valid @RequestBody final PurchaseOrderHeader purchaseOrderHeader) {

        purchaseOrderHeaderService.addPurchaseOrderHeader(purchaseOrderHeader);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{purchaseOrderHeaderId}")
    public ResponseEntity<Void> updatePurchaseOrderHeader(
            @PathVariable final Long purchaseOrderHeaderId,
            @Valid @RequestBody final PurchaseOrderHeader purchaseOrderHeader) {

        purchaseOrderHeaderService.updatePurchaseOrderHeader(
                purchaseOrderHeaderId,
                purchaseOrderHeader
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{purchaseOrderHeaderId}/approve")
    public ResponseEntity<Void> approvePurchaseOrder(
            @PathVariable final Long purchaseOrderHeaderId) {

        purchaseOrderHeaderService.approvePurchaseOrder(purchaseOrderHeaderId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{purchaseOrderHeaderId}/cancel")
    public ResponseEntity<Void> cancelPurchaseOrder(
            @PathVariable final Long purchaseOrderHeaderId) {

        purchaseOrderHeaderService.cancelPurchaseOrder(purchaseOrderHeaderId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{purchaseOrderHeaderId}")
    public ResponseEntity<Void> deletePurchaseOrderHeader(
            @PathVariable final Long purchaseOrderHeaderId) {

        purchaseOrderHeaderService.deletePurchaseOrderHeader(purchaseOrderHeaderId);

        return ResponseEntity.noContent().build();
    }
}
