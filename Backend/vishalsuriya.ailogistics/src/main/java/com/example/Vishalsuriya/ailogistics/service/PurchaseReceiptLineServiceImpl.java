
package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptLine;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderLineRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseReceiptHeaderRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseReceiptLineRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseReceiptLineServiceImpl implements PurchaseReceiptLineService {

    private final PurchaseReceiptLineRepository purchaseReceiptLineRepo;
    private final PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepo;
    private final PurchaseOrderLineRepository purchaseOrderLineRepo;

    public PurchaseReceiptLineServiceImpl(
            final PurchaseReceiptLineRepository purchaseReceiptLineRepo,
            final PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepo,
            final PurchaseOrderLineRepository purchaseOrderLineRepo) {

        this.purchaseReceiptLineRepo = purchaseReceiptLineRepo;
        this.purchaseReceiptHeaderRepo = purchaseReceiptHeaderRepo;
        this.purchaseOrderLineRepo = purchaseOrderLineRepo;
    }

    @Override
    public List<PurchaseReceiptLine> getAllPurchaseReceiptLines() {
        return purchaseReceiptLineRepo.findAll();
    }

    @Override
    public PurchaseReceiptLine getPurchaseReceiptLineById(final Long id) {
        return purchaseReceiptLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase receipt line not found with ID: " + id));
    }

    @Override
    public List<PurchaseReceiptLine> getByProductId(final Long productId) {
        return purchaseReceiptLineRepo.findByProductId(productId);
    }

    @Override
    public List<PurchaseReceiptLine> getByPurchaseOrderLineId(final Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.findByPurchaseOrderLineId(purchaseOrderLineId);
    }

    @Override
    public List<PurchaseReceiptLine> getByPurchaseReceiptHeaderId(final Long purchaseReceiptHeaderId) {
        return purchaseReceiptLineRepo.findByPurchaseReceiptHeaderIdOrderByIdAsc(purchaseReceiptHeaderId);
    }

    @Override
    public PurchaseReceiptLine getByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            final Long purchaseReceiptHeaderId,
            final Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.findByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(purchaseReceiptHeaderId,
                purchaseOrderLineId).orElseThrow(() -> new EntityNotFoundException(
                "Purchase receipt line not found for PR Header ID: " + purchaseReceiptHeaderId
                        + " and PO Line ID: " + purchaseOrderLineId));
    }

    @Override
    @Transactional
    public void addPurchaseReceiptLine(final PurchaseReceiptLine purchaseReceiptLine) {
        validateForCreate(purchaseReceiptLine);
        final Long prHeaderId = purchaseReceiptLine.getPurchaseReceiptHeader().getId();
        final Long poLineId = purchaseReceiptLine.getPurchaseOrderLine().getId();
        final PurchaseReceiptHeader prHeader = purchaseReceiptHeaderRepo.findById(prHeaderId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Receipt Header not found with ID: " + prHeaderId));
        final PurchaseOrderLine poLine = purchaseOrderLineRepo.findById(poLineId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order Line not found with ID: " + poLineId));
        if (prHeader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalArgumentException("Receipt lines can be added only to a DRAFT Purchase Receipt.");
        }
        if (purchaseReceiptLineRepo.existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(prHeaderId, poLineId)) {
            throw new EntityExistsException("Purchase Receipt Line already exists for PR Header ID: " + prHeaderId
                    + " and PO Line ID: " + poLineId);
        }
        if (!poLine.getPurchaseOrderHeader().getId().equals(prHeader.getPurchaseOrderHeader().getId())) {
            throw new IllegalArgumentException("Selected Purchase Order Line does not belong to the Purchase Order.");
        }
        if (poLine.getProduct() == null) {
            throw new IllegalArgumentException("Product is not assigned to the Purchase Order Line.");
        }
        if (purchaseReceiptLine.getProduct() == null || purchaseReceiptLine.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product reference is required.");
        }

        if (!poLine.getProduct().getId().equals(purchaseReceiptLine.getProduct().getId())) {
            throw new IllegalArgumentException("Selected Product does not match the Product on the Purchase Order Line.");
        }
        validateQuantity(purchaseReceiptLine.getQuantityReceived(), poLine);
        purchaseReceiptLine.setPurchaseReceiptHeader(prHeader);
        purchaseReceiptLine.setPurchaseOrderLine(poLine);
        purchaseReceiptLine.setProduct(poLine.getProduct());
        purchaseReceiptLineRepo.save(purchaseReceiptLine);
    }

    @Override
    @Transactional
    public void updatePurchaseReceiptLine(final Long id, final PurchaseReceiptLine purchaseReceiptLine) {
        final PurchaseReceiptLine existingLine = getPurchaseReceiptLineById(id);
        final Integer newQuantity = purchaseReceiptLine.getQuantityReceived();
        final PurchaseReceiptHeader prHeader = purchaseReceiptHeaderRepo.findById(existingLine.getPurchaseReceiptHeader().getId()).
                orElseThrow(() -> new EntityNotFoundException("PR header does not exists with ID." + existingLine.getPurchaseReceiptHeader().getId()));
        if (prHeader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalArgumentException("Receipt lines can be updated only for a DRAFT Purchase Receipt.");
        }
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Received quantity must be greater than zero.");
        }
        if (newQuantity > existingLine.getPurchaseOrderLine().getRemainingQuantity()) {
            throw new IllegalArgumentException("Received quantity (" + newQuantity +
                    ") cannot exceed PO remaining quantity (" +
                    existingLine.getPurchaseOrderLine().getRemainingQuantity() + ").");
        }
        existingLine.setQuantityReceived(newQuantity);
        if (purchaseReceiptLine.getUnitPrice() != null) {
            existingLine.setUnitPrice(purchaseReceiptLine.getUnitPrice());
        }
        if (purchaseReceiptLine.getRemarks() != null) {
            existingLine.setRemarks(purchaseReceiptLine.getRemarks());
        }
    }

    @Override
    @Transactional
    public void deletePurchaseReceiptLine(final Long id) {
        final PurchaseReceiptLine existingLine = getPurchaseReceiptLineById(id);
        final PurchaseReceiptHeader prHeader = purchaseReceiptHeaderRepo.findById(existingLine.getPurchaseReceiptHeader().getId()).
                orElseThrow(() -> new EntityNotFoundException("PR header not found with ID."));
        if (prHeader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalArgumentException(
                    "Receipt lines can be deleted only from a DRAFT Purchase Receipt.");
        }
        purchaseReceiptLineRepo.delete(existingLine);
    }

    @Override
    public boolean existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(final Long purchaseReceiptHeaderId,
                                                                         final Long purchaseOrderLineId) {
        return purchaseReceiptLineRepo.existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
                purchaseReceiptHeaderId, purchaseOrderLineId);
    }

    private void validateForCreate(final PurchaseReceiptLine purchaseReceiptLine) {
        if (purchaseReceiptLine == null) {
            throw new IllegalArgumentException("Purchase Receipt Line cannot be null.");
        }
        if (purchaseReceiptLine.getPurchaseReceiptHeader() == null
                || purchaseReceiptLine.getPurchaseReceiptHeader().getId() == null) {
            throw new IllegalArgumentException("Purchase Receipt Header reference is required.");
        }
        if (purchaseReceiptLine.getPurchaseOrderLine() == null
                || purchaseReceiptLine.getPurchaseOrderLine().getId() == null) {
            throw new IllegalArgumentException("Purchase Order Line reference is required.");
        }
        if (purchaseReceiptLine.getProduct() == null
                || purchaseReceiptLine.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product reference is required.");
        }
        if (purchaseReceiptLine.getQuantityReceived() == null
                || purchaseReceiptLine.getQuantityReceived() <= 0) {
            throw new IllegalArgumentException("Quantity received must be greater than zero.");
        }
        if (purchaseReceiptLine.getUnitPrice() == null
                || purchaseReceiptLine.getUnitPrice().signum() <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero.");
        }
    }

    private void validateQuantity(final Integer quantityReceived, final PurchaseOrderLine poLine) {
        if (quantityReceived == null || quantityReceived <= 0) {
            throw new IllegalArgumentException("Quantity received must be greater than zero.");
        }
        final int remainingQuantity = poLine.getRemainingQuantity();
        if (quantityReceived > remainingQuantity) {
            throw new IllegalArgumentException(
                    "Received quantity (" + quantityReceived + ") cannot exceed remaining quantity (" + remainingQuantity + ").");
        }
    }
}

