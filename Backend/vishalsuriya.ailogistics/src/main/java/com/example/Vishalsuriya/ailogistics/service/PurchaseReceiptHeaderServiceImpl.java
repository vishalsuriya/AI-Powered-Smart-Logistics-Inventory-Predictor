package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderHeaderRepository;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseReceiptHeaderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseReceiptHeaderServiceImpl implements PurchaseReceiptHeaderService{

    private final PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepo;

    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;

    public PurchaseReceiptHeaderServiceImpl(PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepository, PurchaseOrderHeaderRepository purchaseOrderHeaderRepository) {
        this.purchaseReceiptHeaderRepo = purchaseReceiptHeaderRepository;
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepository;
    }

    @Override
    public String generateTrxNumber() {
        long nextId = purchaseReceiptHeaderRepo.count()+1;
        int year = java.time.LocalDate.now().getYear();
        return String.format("PR-%d-%03d", year, nextId);
    }

    @Override
    public List<PurchaseReceiptHeader> getAllPurchaseReceiptHeaders() {
        return purchaseReceiptHeaderRepo.findAll();
    }

    @Override
    public PurchaseReceiptHeader getPurchaseReceiptHeaderById(Long id) {
        return purchaseReceiptHeaderRepo.findById(id).
                orElseThrow(() -> new EntityNotFoundException("purchase receipt header not found with ID." + id));
    }

    @Override
    public PurchaseReceiptHeader getPurchaseReceiptHeaderByTrxNumber(String trxNumber) {
        return purchaseReceiptHeaderRepo.findByTrxNumber(trxNumber).
                orElseThrow(() -> new EntityNotFoundException("purchase receipt header not found with trxNumber." + trxNumber));
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByPurchaseOrderHeaderId(Long purchaseOrderHeaderId) {
        return purchaseReceiptHeaderRepo.findByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByVendorId(Long vendorId) {
        return purchaseReceiptHeaderRepo.findByVendorId(vendorId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByWarehouseId(Long warehouseId) {
        return purchaseReceiptHeaderRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByStatus(PurchaseReceiptStatus status) {
        return purchaseReceiptHeaderRepo.findByStatus(status);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByReceivedBy(String receivedBy) {
        return purchaseReceiptHeaderRepo.findByReceivedBy(receivedBy);
    }

    @Override
    public boolean existsByTrxNumber(String trxNumber) {
        return purchaseReceiptHeaderRepo.existsByTrxNumber(trxNumber);
    }

    @Override
    @Transactional
    public void addPurchaseReceiptHeader(PurchaseReceiptHeader purchaseReceiptHeader) {
        purchaseOrderHeaderRepo.findById(purchaseReceiptHeader.getPurchaseOrderHeader().getId()).
                orElseThrow(() -> new EntityNotFoundException("Purchase Order not found."));

       String trxNum = generateTrxNumber();
       purchaseReceiptHeader.setTrxNumber(trxNum);
        purchaseReceiptHeaderRepo.save(purchaseReceiptHeader);
    }

    @Override
    public void updatePurchaseReceiptHeader(Long id, PurchaseReceiptHeader purchaseReceiptHeader) {
       PurchaseReceiptHeader existingPurchaseReceiptHeader = purchaseReceiptHeaderRepo.findById(id).
               orElseThrow(() -> new EntityNotFoundException("purchase receipt header not found with ID." + id));
       existingPurchaseReceiptHeader.setStatus(purchaseReceiptHeader.getStatus());
       existingPurchaseReceiptHeader.setReceivedBy(purchaseReceiptHeader.getReceivedBy());
       existingPurchaseReceiptHeader.setReceivedDate(purchaseReceiptHeader.getReceivedDate());
        purchaseReceiptHeaderRepo.save(existingPurchaseReceiptHeader);
    }

    @Override
    public void deletePurchaseReceiptHeader(Long id) {
        PurchaseReceiptHeader existingHeader = purchaseReceiptHeaderRepo.findById(id).
                orElseThrow(() -> new EntityNotFoundException("cannot delete purchase receipt not found with Id." + id));
        purchaseReceiptHeaderRepo.delete(existingHeader);
    }
}
