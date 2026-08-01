package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderHeaderRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderLineRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private final PurchaseOrderLineRepository purchaseOrderLineRepo;

    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;

    public PurchaseOrderLineServiceImpl(PurchaseOrderLineRepository purchaseOrderLineRepository, PurchaseOrderHeaderRepository purchaseOrderHeaderRepo) {
        this.purchaseOrderLineRepo = purchaseOrderLineRepository;
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepo;
    }

    @Override
    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepo.findAll();
    }

    @Override
    public PurchaseOrderLine getPurchaseOrderLineById(Long id) {
        return purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase line not found with ID: " + id));
    }

    @Override
    public PurchaseOrderLine getByPurchaseOrderHeaderIdAndProductId(Long purchaseOrderHeaderId, Long productId) {
        return purchaseOrderLineRepo.findByPurchaseOrderHeaderIdAndProductId(purchaseOrderHeaderId, productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Purchase line not found for Header ID: " + purchaseOrderHeaderId + " and Product ID: " + productId));
    }

    @Override
    public List<PurchaseOrderLine> getByProductId(Long productId) {
        return purchaseOrderLineRepo.findByProductId(productId);
    }

    @Override
    public List<PurchaseOrderLine> getByPurchaseOrderHeaderId(Long purchaseOrderHeaderId) {
        return purchaseOrderLineRepo.findByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @Override
    @Transactional
    public void addPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        purchaseOrderHeaderRepo.findById(purchaseOrderLine.getPurchaseOrderHeader().getId()).
                orElseThrow(() ->
                new EntityNotFoundException(
                        "Purchase Order Header not found."
                )
        );
        boolean exists = purchaseOrderLineRepo.existsByPurchaseOrderHeaderIdAndProductId(
                purchaseOrderLine.getPurchaseOrderHeader(),
                purchaseOrderLine.getProduct()
        );

        if (exists) {
            throw new EntityExistsException("Product ID " + purchaseOrderLine.getProduct() +
                    " is already added to this Purchase Order. Update the existing line quantity instead.");
        }
        purchaseOrderLineRepo.save(purchaseOrderLine);
       recalculateTotalAndSaveHeader(purchaseOrderLine.getPurchaseOrderHeader());
    }

    @Override
    @Transactional
    public void updatePurchaseOrderLine(Long id, PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrderLine existingPurchaseOrderLine = purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order line not found with ID: " + id));
        existingPurchaseOrderLine.setQuantityOrdered(purchaseOrderLine.getQuantityOrdered());
        existingPurchaseOrderLine.setUnitPrice(purchaseOrderLine.getUnitPrice());
        existingPurchaseOrderLine.setQuantityReceived(purchaseOrderLine.getQuantityReceived());
        purchaseOrderLineRepo.save(existingPurchaseOrderLine);
        recalculateTotalAndSaveHeader(existingPurchaseOrderLine.getPurchaseOrderHeader());
    }

    @Override
    @Transactional
    public void deletePurchaseOrderLine(Long id) {
        PurchaseOrderLine existingPurchaseOrderLine = purchaseOrderLineRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot delete. Purchase order line not found with ID: " + id));

        PurchaseOrderHeader header = existingPurchaseOrderLine.getPurchaseOrderHeader();
        purchaseOrderLineRepo.delete(existingPurchaseOrderLine);
        recalculateTotalAndSaveHeader(header);
    }

    @Override
    public boolean existsByPurchaseOrderHeaderIdAndProductId(PurchaseOrderHeader purchaseOrderHeaderId, Product productId) {
        return purchaseOrderLineRepo.existsByPurchaseOrderHeaderIdAndProductId(purchaseOrderHeaderId, productId);
    }

    private void recalculateTotalAndSaveHeader(PurchaseOrderHeader header){
        PurchaseOrderHeader existingHeader =  purchaseOrderHeaderRepo.findById(header.getId()).
                orElseThrow(() -> new EntityNotFoundException("purchase header not found with ID." + header.getId()));
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineRepo.findByPurchaseOrderHeaderId(header.getId());
        BigDecimal calculatedTotal = purchaseOrderLines.stream()
                        .map(PurchaseOrderLine :: getLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal :: add);
        existingHeader.setTotalAmount(calculatedTotal);
        purchaseOrderHeaderRepo.save(existingHeader);
    }
}