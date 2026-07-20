package com.example.Vishalsuriya.ailogistics.service;


import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptLine;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseReceiptLineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseReceiptLineServiceImpl implements PurchaseReceiptLineService{

    private final PurchaseReceiptLineRepository purchaseReceiptLineRepo;

    public PurchaseReceiptLineServiceImpl(PurchaseReceiptLineRepository purchaseReceiptLineRepo) {
        this.purchaseReceiptLineRepo = purchaseReceiptLineRepo;
    }

    @Override
    public List<PurchaseReceiptLine> getAllPurchaseReceiptLines() {
        return purchaseReceiptLineRepo.findAll();
    }

    @Override
    public PurchaseReceiptLine getPurchaseReceiptLineById(Long id) {
        return purchaseReceiptLineRepo.findById(id).
                orElseThrow(()-> new EntityNotFoundException("purchase receipt line not found by ID." + id));
    }

    @Override
    public List<PurchaseReceiptLine> getByProductId(Long productId) {
        return purchaseReceiptLineRepo.findByProductId(productId);
    }

    @Override
    public List<PurchaseReceiptLine> getByPurchaseOrderLineId(Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.findByPurchaseOrderLineId(purchaseOrderLineId);
    }

    @Override
    public List<PurchaseReceiptLine> getByPurchaseReceiptHeaderId(Long purchaseReceiptHeaderId) {
        return purchaseReceiptLineRepo.findByPurchaseReceiptHeaderIdOrderByIdAsc(purchaseReceiptHeaderId);
    }

    @Override
    public PurchaseReceiptLine getByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(Long purchaseReceiptHeaderId, Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.findByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
                        purchaseReceiptHeaderId,
                        purchaseOrderLineId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Receipt Line not found."));
    }

    @Override
    public void addPurchaseReceiptLine(PurchaseReceiptLine purchaseReceiptLine) {
        purchaseReceiptLineRepo.save(purchaseReceiptLine);
    }

    @Override
    public void updatePurchaseReceiptLine(Long id, PurchaseReceiptLine purchaseReceiptLine) {
        PurchaseReceiptLine existingPurchaseReceiptLine = getPurchaseReceiptLineById(id);
        existingPurchaseReceiptLine.setQuantityReceived(purchaseReceiptLine.getQuantityReceived());
        existingPurchaseReceiptLine.setUnitPrice(purchaseReceiptLine.getUnitPrice());
        existingPurchaseReceiptLine.setRemarks(purchaseReceiptLine.getRemarks());
    }

    @Override
    public void deletePurchaseReceiptLine(Long id) {
        PurchaseReceiptLine existingLine = getPurchaseReceiptLineById(id);
        purchaseReceiptLineRepo.delete(existingLine);
    }

    @Override
    public boolean existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(Long purchaseReceiptHeaderId, Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(purchaseReceiptHeaderId, purchaseOrderLineId);
    }
}
