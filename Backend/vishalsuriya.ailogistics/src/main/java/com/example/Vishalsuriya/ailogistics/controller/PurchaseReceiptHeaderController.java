package com.example.Vishalsuriya.ailogistics.controller;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;
import com.example.Vishalsuriya.ailogistics.service.PurchaseReceiptHeaderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-receipts")
public class PurchaseReceiptHeaderController {

    private final PurchaseReceiptHeaderService purchaseReceiptHeaderService;

    public PurchaseReceiptHeaderController(
            final PurchaseReceiptHeaderService purchaseReceiptHeaderService) {
        this.purchaseReceiptHeaderService = purchaseReceiptHeaderService;
    }

    @GetMapping
    public List<PurchaseReceiptHeader> getAllPurchaseReceiptHeaders() {
        return purchaseReceiptHeaderService.getAllPurchaseReceiptHeaders();
    }

    @GetMapping("/{id}")
    public PurchaseReceiptHeader getPurchaseReceiptHeaderById(@PathVariable final Long id) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeaderById(id);
    }

    @GetMapping("/trx-number/{trxNumber}")
    public PurchaseReceiptHeader getPurchaseReceiptHeaderByTrxNumber(@PathVariable final String trxNumber) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeaderByTrxNumber(trxNumber);
    }

    @GetMapping("/purchase-order/{purchaseOrderHeaderId}")
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByPurchaseOrderHeaderId(
            @PathVariable final Long purchaseOrderHeaderId) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByVendorId(@PathVariable final Long vendorId) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByVendorId(vendorId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByWarehouseId(@PathVariable final Long warehouseId) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByWarehouseId(warehouseId);
    }

    @GetMapping("/status/{status}")
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByStatus(
            @PathVariable final PurchaseReceiptStatus status) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByStatus(status);
    }

    @GetMapping("/received-by/{receivedBy}")
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByReceivedBy(
            @PathVariable final String receivedBy) {
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByReceivedBy(receivedBy);
    }

    @GetMapping("/exists/{trxNumber}")
    public boolean existsByTrxNumber(@PathVariable final String trxNumber) {
        return purchaseReceiptHeaderService.existsByTrxNumber(trxNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPurchaseReceiptHeader(
            @RequestBody final PurchaseReceiptHeader purchaseReceiptHeader) {
        purchaseReceiptHeaderService.addPurchaseReceiptHeader(purchaseReceiptHeader);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePurchaseReceiptHeader(
            @PathVariable final Long id,
            @RequestBody final PurchaseReceiptHeader purchaseReceiptHeader) {
        purchaseReceiptHeaderService.updatePurchaseReceiptHeader(id, purchaseReceiptHeader);
    }

    @PatchMapping("/{id}/commit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commitPurchaseReceipt(@PathVariable final Long id) {
        purchaseReceiptHeaderService.commitPurchaseReceipt(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchaseReceiptHeader(@PathVariable final Long id) {
        purchaseReceiptHeaderService.deletePurchaseReceiptHeader(id);
    }
}

