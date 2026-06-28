package com.example.Vishalsuriya.ailogistics.controller;


import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;
import com.example.Vishalsuriya.ailogistics.service.PurchaseOrderLineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchaseOrderLines")
public class PurchaseOrderLineController {
    private final PurchaseOrderLineService purchaseOrderLineService;

    public PurchaseOrderLineController(PurchaseOrderLineService purchaseOrderLineService) {
        this.purchaseOrderLineService = purchaseOrderLineService;
    }

    @GetMapping
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineService.getAllPurchaseOrderLines();
    }

    @GetMapping("/{id}")
    public PurchaseOrderLine getPurchaseOrderLineById(@PathVariable Long id) {
        return purchaseOrderLineService.getPurchaseOrderLineById(id);
    }

    @GetMapping("/header/{purchaseOrderHeaderId}/product/{productId}")
    public PurchaseOrderLine getByPurchaseOrderHeaderIdAndProductId(
            @PathVariable Long purchaseOrderHeaderId,
            @PathVariable Long productId) {

        return purchaseOrderLineService.getByPurchaseOrderHeaderIdAndProductId(purchaseOrderHeaderId, productId);
    }

    @GetMapping("/product/{productId}")
    public List<PurchaseOrderLine> getByProductId(@PathVariable Long productId) {
        return purchaseOrderLineService.getByProductId(productId);
    }

    @GetMapping("/header/{purchaseOrderHeaderId}")
    public List<PurchaseOrderLine> getByPurchaseOrderHeaderId(
            @PathVariable Long purchaseOrderHeaderId) {

        return purchaseOrderLineService.getByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @GetMapping("/exists/header/{purchaseOrderHeaderId}/product/{productId}")
    public boolean existsByPurchaseOrderHeaderIdAndProductId(
            @PathVariable Long purchaseOrderHeaderId,
            @PathVariable Long productId) {

        return purchaseOrderLineService.existsByPurchaseOrderHeaderIdAndProductId(
                purchaseOrderHeaderId,
                productId
        );
    }

    @PostMapping
    public void addPurchaseOrderLine(@RequestBody PurchaseOrderLine purchaseOrderLine) {
        purchaseOrderLineService.addPurchaseOrderLine(purchaseOrderLine);
    }

    @PutMapping("/{id}")
    public void updatePurchaseOrderLine(@PathVariable Long id, @RequestBody PurchaseOrderLine purchaseOrderLine) {
        purchaseOrderLineService.updatePurchaseOrderLine(id, purchaseOrderLine);
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseOrderLine(@PathVariable Long id) {
        purchaseOrderLineService.deletePurchaseOrderLine(id);
    }
}
