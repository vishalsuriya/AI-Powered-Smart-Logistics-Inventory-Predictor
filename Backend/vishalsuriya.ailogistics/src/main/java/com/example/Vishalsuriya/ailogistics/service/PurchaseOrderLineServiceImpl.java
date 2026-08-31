package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.*;
import com.example.Vishalsuriya.ailogistics.repository.ProductRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderHeaderRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderLineRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private final PurchaseOrderLineRepository purchaseOrderLineRepo;
    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;
    private final ProductRepository productRepo;

    public PurchaseOrderLineServiceImpl(final PurchaseOrderLineRepository purchaseOrderLineRepository,
                                        final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo,
                                        final ProductRepository productRepo) {
        this.purchaseOrderLineRepo = purchaseOrderLineRepository;
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepo.findAll();
    }

    @Override
    public PurchaseOrderLine getPurchaseOrderLineById(final Long id) {
        return purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase line not found with ID: " + id));
    }

    @Override
    public PurchaseOrderLine getByPurchaseOrderHeaderIdAndProductId(final Long purchaseOrderHeaderId, final Long productId) {
        return purchaseOrderLineRepo.findByPurchaseOrderHeaderIdAndProductId(purchaseOrderHeaderId, productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Purchase line not found for Header ID: " + purchaseOrderHeaderId + " and Product ID: " + productId));
    }

    @Override
    public List<PurchaseOrderLine> getByProductId(final Long productId) {
        return purchaseOrderLineRepo.findByProductId(productId);
    }

    @Override
    public List<PurchaseOrderLine> getByPurchaseOrderHeaderId(final Long purchaseOrderHeaderId) {
        return purchaseOrderLineRepo.findByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @Override
    public void addPurchaseOrderLine(final PurchaseOrderLine purchaseOrderLine) {
        final PurchaseOrderHeader header = purchaseOrderHeaderRepo.findById(purchaseOrderLine.getPurchaseOrderHeader().getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order Header not found with ID: " + purchaseOrderLine.getPurchaseOrderHeader().getId()));
        validateDraftStatus(header, "add lines to");
        validateForCreate(purchaseOrderLine, header);
        validateLineQuantitiesAndPrices(purchaseOrderLine.getQuantityOrdered(), purchaseOrderLine.getUnitPrice());
        final Product product = productRepo.findById(purchaseOrderLine.getProduct().getId()).
                orElseThrow(() -> new EntityNotFoundException("Product not found with ID." + purchaseOrderLine.getProduct().getId()));
        purchaseOrderLine.setProduct(product);
        purchaseOrderLine.setPurchaseOrderHeader(header);
        purchaseOrderLineRepo.save(purchaseOrderLine);
        recalculateTotalAndSaveHeader(header);
    }

    @Override
    @Transactional
    public void updatePurchaseOrderLine(final Long id, final PurchaseOrderLine purchaseOrderLine) {
        final PurchaseOrderLine existingLine = purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order line not found with ID: " + id));

        final PurchaseOrderHeader header = existingLine.getPurchaseOrderHeader();
        validateDraftStatus(header, "update lines in");
        validateLineQuantitiesAndPrices(purchaseOrderLine.getQuantityOrdered(), purchaseOrderLine.getUnitPrice());
        validateForUpdate(existingLine, purchaseOrderLine);
        existingLine.setQuantityOrdered(purchaseOrderLine.getQuantityOrdered());
        existingLine.setUnitPrice(purchaseOrderLine.getUnitPrice());
        recalculateTotalAndSaveHeader(header);
    }

    @Override
    public void deletePurchaseOrderLine(final Long id) {
        final PurchaseOrderLine existingLine = purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot delete. Purchase order line not found with ID: " + id));

        final PurchaseOrderHeader header = existingLine.getPurchaseOrderHeader();
        validateDraftStatus(header, "delete lines from");
        purchaseOrderLineRepo.delete(existingLine);
        recalculateTotalAndSaveHeader(header);
    }

    @Override
    public boolean existsByPurchaseOrderHeaderIdAndProductId(final Long purchaseOrderHeaderId, final Long productId) {
        return purchaseOrderLineRepo.existsByPurchaseOrderHeaderIdAndProductId(purchaseOrderHeaderId, productId);
    }

    private void validateForCreate(final PurchaseOrderLine purchaseOrderLine, final PurchaseOrderHeader header) {

        if (purchaseOrderLine.getPurchaseOrderHeader() == null || purchaseOrderLine.getPurchaseOrderHeader().getId() == null) {
            throw new IllegalArgumentException("Purchase Order Header reference is required.");
        }

        final Product product = productRepo.findById(purchaseOrderLine.getProduct().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + purchaseOrderLine.getProduct().getId()
                ));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Product cannot be added to a Purchase Order because its status is: " + product.getStatus()
            );
        }
        if (purchaseOrderLine.getProduct() == null || purchaseOrderLine.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product reference is required.");
        }

        if (!productRepo.existsById(purchaseOrderLine.getProduct().getId())) {
            throw new EntityNotFoundException("Product not found with ID: " + purchaseOrderLine.getProduct().getId());
        }

        final boolean exists = purchaseOrderLineRepo.existsByPurchaseOrderHeaderIdAndProductId(
                header.getId(),
                purchaseOrderLine.getProduct().getId()
        );

        if (exists) {
            throw new EntityExistsException("Product ID " + purchaseOrderLine.getProduct().getId() +
                    " is already added to this Purchase Order. Update the existing line quantity instead.");
        }
    }

    private void validateForUpdate(final PurchaseOrderLine existingLine, final PurchaseOrderLine purchaseOrderLine) {
        if (purchaseOrderLine.getQuantityOrdered() < existingLine.getQuantityReceived()) {
            throw new IllegalArgumentException("Ordered quantity (" + purchaseOrderLine.getQuantityOrdered() +
                    ") cannot be less than already received quantity (" + existingLine.getQuantityReceived() + ").");
        }

    }

    private void validateDraftStatus(final PurchaseOrderHeader header, final String action) {
        if (header.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot " + action + " a Purchase Order with status: " + header.getStatus() + ". Must be in DRAFT status.");
        }
    }

    private void validateLineQuantitiesAndPrices(final Integer quantity, final BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Ordered quantity must be greater than zero.");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero.");
        }
    }

    private void recalculateTotalAndSaveHeader(final PurchaseOrderHeader header) {
        final List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineRepo.findByPurchaseOrderHeaderId(header.getId());
        final BigDecimal calculatedTotal = purchaseOrderLines.stream()
                .map(line -> line.getLineTotal() != null ? line.getLineTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        header.setTotalAmount(calculatedTotal);
        purchaseOrderHeaderRepo.save(header);
    }
}