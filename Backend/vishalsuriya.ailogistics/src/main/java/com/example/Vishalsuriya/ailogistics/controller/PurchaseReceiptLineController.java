package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptLine;
import com.example.Vishalsuriya.ailogistics.service.PurchaseReceiptLineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-receipt-lines")
public class PurchaseReceiptLineController {

    private final PurchaseReceiptLineService purchaseReceiptLineService;

    public PurchaseReceiptLineController(PurchaseReceiptLineService purchaseReceiptLineService) {
        this.purchaseReceiptLineService = purchaseReceiptLineService;
    }

    @GetMapping
    public List<PurchaseReceiptLine> getAllPurchaseReceiptLines() {
        return purchaseReceiptLineService.getAllPurchaseReceiptLines();
    }

    @GetMapping("/{id}")
    public PurchaseReceiptLine getPurchaseReceiptLineById(@PathVariable Long id) {
        return purchaseReceiptLineService.getPurchaseReceiptLineById(id);
    }

    @GetMapping("/product/{productId}")
    public List<PurchaseReceiptLine> getByProductId(@PathVariable Long productId) {
        return purchaseReceiptLineService.getByProductId(productId);
    }

    @GetMapping("/purchase-order-line/{purchaseOrderLineId}")
    public List<PurchaseReceiptLine> getByPurchaseOrderLineId(
            @PathVariable Long purchaseOrderLineId) {
        return purchaseReceiptLineService.getByPurchaseOrderLineId(purchaseOrderLineId);
    }

    @GetMapping("/purchase-receipt-header/{purchaseReceiptHeaderId}")
    public List<PurchaseReceiptLine> getByPurchaseReceiptHeaderId(
            @PathVariable Long purchaseReceiptHeaderId) {
        return purchaseReceiptLineService.getByPurchaseReceiptHeaderId(purchaseReceiptHeaderId);
    }

    @GetMapping("/search")
    public PurchaseReceiptLine getByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            @RequestParam Long purchaseReceiptHeaderId,
            @RequestParam Long purchaseOrderLineId) {

        return purchaseReceiptLineService
                .getByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
                        purchaseReceiptHeaderId,
                        purchaseOrderLineId);
    }

    @GetMapping("/exists")
    public boolean existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            @RequestParam Long purchaseReceiptHeaderId,
            @RequestParam Long purchaseOrderLineId) {

        return purchaseReceiptLineService
                .existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
                        purchaseReceiptHeaderId,
                        purchaseOrderLineId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPurchaseReceiptLine(
            @RequestBody PurchaseReceiptLine purchaseReceiptLine) {

        purchaseReceiptLineService.addPurchaseReceiptLine(purchaseReceiptLine);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePurchaseReceiptLine(
            @PathVariable Long id,
            @RequestBody PurchaseReceiptLine purchaseReceiptLine) {

        purchaseReceiptLineService.updatePurchaseReceiptLine(id, purchaseReceiptLine);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePurchaseReceiptLine(@PathVariable Long id) {
        purchaseReceiptLineService.deletePurchaseReceiptLine(id);
    }
}