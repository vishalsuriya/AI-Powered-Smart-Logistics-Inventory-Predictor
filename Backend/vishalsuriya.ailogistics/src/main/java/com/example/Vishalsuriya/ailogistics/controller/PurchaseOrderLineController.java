package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;
import com.example.Vishalsuriya.ailogistics.service.PurchaseOrderLineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchaseOrderLines")
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService purchaseOrderLineService;

    public PurchaseOrderLineController(
            final PurchaseOrderLineService purchaseOrderLineService) {

        this.purchaseOrderLineService = purchaseOrderLineService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderLine>> getAllPurchaseOrderLines() {

        return ResponseEntity.ok(
                purchaseOrderLineService.getAllPurchaseOrderLines()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderLine> getPurchaseOrderLineById(
            @PathVariable final Long id) {

        return ResponseEntity.ok(
                purchaseOrderLineService.getPurchaseOrderLineById(id)
        );
    }

    @GetMapping("/header/{purchaseOrderHeaderId}/product/{productId}")
    public ResponseEntity<PurchaseOrderLine>
    getByPurchaseOrderHeaderIdAndProductId(
            @PathVariable final Long purchaseOrderHeaderId,
            @PathVariable final Long productId) {

        return ResponseEntity.ok(
                purchaseOrderLineService
                        .getByPurchaseOrderHeaderIdAndProductId(
                                purchaseOrderHeaderId,
                                productId
                        )
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PurchaseOrderLine>> getByProductId(
            @PathVariable final Long productId) {

        return ResponseEntity.ok(
                purchaseOrderLineService.getByProductId(productId)
        );
    }

    @GetMapping("/header/{purchaseOrderHeaderId}")
    public ResponseEntity<List<PurchaseOrderLine>>
    getByPurchaseOrderHeaderId(
            @PathVariable final Long purchaseOrderHeaderId) {

        return ResponseEntity.ok(
                purchaseOrderLineService
                        .getByPurchaseOrderHeaderId(purchaseOrderHeaderId)
        );
    }

    @GetMapping("/exists/header/{purchaseOrderHeaderId}/product/{productId}")
    public ResponseEntity<Boolean>
    existsByPurchaseOrderHeaderIdAndProductId(
            @PathVariable final Long purchaseOrderHeaderId,
            @PathVariable final Long productId) {

        return ResponseEntity.ok(
                purchaseOrderLineService
                        .existsByPurchaseOrderHeaderIdAndProductId(
                                purchaseOrderHeaderId,
                                productId
                        )
        );
    }

    @PostMapping
    public ResponseEntity<Void> addPurchaseOrderLine(
            @Valid @RequestBody final PurchaseOrderLine purchaseOrderLine) {

        purchaseOrderLineService.addPurchaseOrderLine(purchaseOrderLine);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePurchaseOrderLine(
            @PathVariable final Long id,
            @Valid @RequestBody final PurchaseOrderLine purchaseOrderLine) {

        purchaseOrderLineService.updatePurchaseOrderLine(
                id,
                purchaseOrderLine
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderLine(
            @PathVariable final Long id) {

        purchaseOrderLineService.deletePurchaseOrderLine(id);

        return ResponseEntity.noContent().build();
    }

}
