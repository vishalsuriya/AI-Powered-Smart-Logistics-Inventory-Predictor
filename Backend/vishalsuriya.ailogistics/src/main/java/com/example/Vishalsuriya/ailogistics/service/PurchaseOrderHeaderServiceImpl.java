package com.example.Vishalsuriya.ailogistics.service;


import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderHeaderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderHeaderServiceImpl implements PurchaseOrderHeaderService {

    private PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;

    public PurchaseOrderHeaderServiceImpl(PurchaseOrderHeaderRepository purchaseOrderHeaderRepository){
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepository;
    }

    @Override
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaders(){
        return purchaseOrderHeaderRepo.findAll();
    }

    @Override
    public PurchaseOrderHeader getPurchaseOrderHeaderById(Long id) {
        return purchaseOrderHeaderRepo.findById(id).
                orElseThrow(()-> new EntityNotFoundException("Purchase order not found with ID." + id));
    }

    @Override
    public PurchaseOrderHeader getPurchaseOrderHeaderByTrxNumber(String trxNumber) {
        return purchaseOrderHeaderRepo.findByTrxNumber(trxNumber).
                orElseThrow(()-> new EntityNotFoundException("Purchase order not found by trxNumber." + trxNumber));
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByVendorId(Long vendorId) {
        return purchaseOrderHeaderRepo.findByVendorId(vendorId);
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByWarehouseId(Long warehouseId) {
        return purchaseOrderHeaderRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByStatus(PurchaseOrderStatus status) {
        return purchaseOrderHeaderRepo.findByStatus(status);
    }

    @Override
    public String generateTrxNumber() {
        long nextId = purchaseOrderHeaderRepo.count() + 1;
        int year = java.time.LocalDate.now().getYear();
        return String.format("PO-%d-%03d", year, nextId);
    }

    @Override
    public void addPurchaseOrderHeader(PurchaseOrderHeader purchaseOrder) {
        String trxNumber = generateTrxNumber();
        purchaseOrder.setTrxNumber(trxNumber);
        purchaseOrderHeaderRepo.save(purchaseOrder);
    }

    @Override
    public void updatePurchaseOrderHeader(Long id, PurchaseOrderHeader purchaseOrder) {
         PurchaseOrderHeader existingPurchaseOrder = purchaseOrderHeaderRepo.findById(id).
                 orElseThrow(()-> new EntityNotFoundException("Purchase order not found by ID." + id));
         existingPurchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount());
         existingPurchaseOrder.setOrderDate(purchaseOrder.getOrderDate());
         existingPurchaseOrder.setStatus(purchaseOrder.getStatus());
        purchaseOrderHeaderRepo.save(existingPurchaseOrder);
    }

    @Override
    public void deletePurchaseOrderHeader(Long id) {
        purchaseOrderHeaderRepo.deleteById(id);
    }
}
