package com.example.Vishalsuriya.ailogistics.controller;


import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;
import com.example.Vishalsuriya.ailogistics.service.PurchaseOrderHeaderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderHeaderController {

    private final PurchaseOrderHeaderService purchaseOrderHeaderService;


    public PurchaseOrderHeaderController(PurchaseOrderHeaderService purchaseOrderHeaderService) {
        this.purchaseOrderHeaderService = purchaseOrderHeaderService;
    }

    @GetMapping
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaders(){
        return purchaseOrderHeaderService.getAllPurchaseOrderHeaders();
    }

    @GetMapping("/{purchaseOrderHeaderId}")
    public PurchaseOrderHeader getPurchaseOrderHeaderById(@PathVariable Long purchaseOrderHeaderId) {
        return purchaseOrderHeaderService.getPurchaseOrderHeaderById(purchaseOrderHeaderId);
    }

    @GetMapping("/trx/{trxNumber}")
    public PurchaseOrderHeader getPurchaseOrderHeaderByTrxNumber(@PathVariable String trxNumber) {
        return purchaseOrderHeaderService.getPurchaseOrderHeaderByTrxNumber(trxNumber);
    }

    @GetMapping("/vendor/{vendorId}")
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByVendorId(@PathVariable Long vendorId) {
        return purchaseOrderHeaderService.getPurchaseOrderHeadersByVendorId(vendorId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByWarehouseId(@PathVariable Long warehouseId) {
        return purchaseOrderHeaderService.getPurchaseOrderHeadersByWarehouseId(warehouseId);
    }

    @GetMapping("/status/{status}")
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByStatus(@PathVariable PurchaseOrderStatus status) {
        return purchaseOrderHeaderService.getPurchaseOrderHeadersByStatus(status);
    }

    @PostMapping
    public void addPurchaseOrderHeader(@RequestBody PurchaseOrderHeader purchaseOrderHeader) {
        purchaseOrderHeaderService.addPurchaseOrderHeader(purchaseOrderHeader);
    }

    @PutMapping("/{purchaseOrderHeaderId}")
    public void updatePurchaseOrderHeader(@PathVariable Long purchaseOrderHeaderId, @RequestBody PurchaseOrderHeader purchaseOrderHeader) {
        purchaseOrderHeaderService.updatePurchaseOrderHeader(purchaseOrderHeaderId, purchaseOrderHeader);
    }

    @DeleteMapping("/{purchaseOrderHeaderId}")
    public void deletePurchaseOrderHeader(@PathVariable Long purchaseOrderHeaderId) {
        purchaseOrderHeaderService.deletePurchaseOrderHeader(purchaseOrderHeaderId);
    }
}
