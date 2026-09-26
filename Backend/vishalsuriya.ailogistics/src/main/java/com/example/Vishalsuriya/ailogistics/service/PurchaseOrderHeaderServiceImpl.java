package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.*;
import com.example.Vishalsuriya.ailogistics.repository.PurchaseOrderHeaderRepository;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import com.example.Vishalsuriya.ailogistics.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PurchaseOrderHeaderServiceImpl implements PurchaseOrderHeaderService {

    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;
    private final VendorRepository vendorRepo;
    private final WarehouseRepository warehouseRepo;


    public PurchaseOrderHeaderServiceImpl(final PurchaseOrderHeaderRepository purchaseOrderHeaderRepository,
                                          final VendorRepository vendorRepository,
                                          final WarehouseRepository warehouseRepository) {
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepository;
        this.vendorRepo = vendorRepository;
        this.warehouseRepo = warehouseRepository;
    }

    @Override
    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaders() {
        return purchaseOrderHeaderRepo.findAll();
    }

    @Override
    public PurchaseOrderHeader getPurchaseOrderHeaderById(final Long id) {
        return purchaseOrderHeaderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found with ID: " + id));
    }

    @Override
    public PurchaseOrderHeader getPurchaseOrderHeaderByTrxNumber(final String trxNumber) {
        return purchaseOrderHeaderRepo.findByTrxNumber(trxNumber)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found by trxNumber: " + trxNumber));
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByVendorId(final Long vendorId) {
        return purchaseOrderHeaderRepo.findByVendorId(vendorId);
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByWarehouseId(final Long warehouseId) {
        return purchaseOrderHeaderRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByStatus(final PurchaseOrderStatus status) {
        return purchaseOrderHeaderRepo.findByStatus(status);
    }

    @Override
    public String generateTrxNumber() {
        final long nextId = purchaseOrderHeaderRepo.count() + 1;
        final int year = java.time.LocalDate.now().getYear();
        return String.format("PO-%d-%03d", year, nextId);
    }

    @Override
    public void addPurchaseOrderHeader(final PurchaseOrderHeader purchaseOrder) {
        validateForCreate(purchaseOrder);
        final Vendor vendor = vendorRepo.findById(purchaseOrder.getVendor().getId()).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID." + purchaseOrder.getVendor().getId()));
        final Warehouse warehouse = warehouseRepo.findById(purchaseOrder.getWarehouse().getId()).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID." + purchaseOrder.getWarehouse().getId()));
        purchaseOrder.setVendor(vendor);
        purchaseOrder.setWarehouse(warehouse);
        final String trxNumber = generateTrxNumber();
        purchaseOrder.setTrxNumber(trxNumber);
        purchaseOrder.setStatus(PurchaseOrderStatus.DRAFT);
        if (purchaseOrder.getPurchaseOrderLines() != null) {
            for (final PurchaseOrderLine line : purchaseOrder.getPurchaseOrderLines()) {
                line.setPurchaseOrderHeader(purchaseOrder);
            }
        }
        purchaseOrderHeaderRepo.save(purchaseOrder);
    }

    @Override
    @Transactional
    public void updatePurchaseOrderHeader(final Long id, final PurchaseOrderHeader purchaseOrder) {
        final PurchaseOrderHeader existingPO = getPurchaseOrderHeaderById(id);
        if (existingPO.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Only purchase orders in DRAFT status can be updated.");
        }
        final Vendor vendor = vendorRepo.findById(purchaseOrder.getVendor().getId()).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID." + purchaseOrder.getVendor().getId()));
        final Warehouse warehouse = warehouseRepo.findById(purchaseOrder.getWarehouse().getId()).
                orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID." + purchaseOrder.getWarehouse().getId()));
        validateForUpdate(purchaseOrder);
        existingPO.setVendor(vendor);
        existingPO.setWarehouse(warehouse);
        existingPO.setOrderDate(purchaseOrder.getOrderDate());
        existingPO.setExpectedDeliveryDate(purchaseOrder.getExpectedDeliveryDate());
        existingPO.setRemarks(purchaseOrder.getRemarks());
    }

    @Override
    @Transactional
    public void approvePurchaseOrder(final Long id) {
        final PurchaseOrderHeader existingPO = getPurchaseOrderHeaderById(id);
        if (existingPO.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Only purchase orders in DRAFT status can be approved.");
        }
        if (existingPO.getPurchaseOrderLines() == null || existingPO.getPurchaseOrderLines().isEmpty()) {
            throw new IllegalStateException("Cannot approve a purchase order without line items.");
        }
        existingPO.setStatus(PurchaseOrderStatus.APPROVED);
    }

    @Override
    @Transactional
    public void cancelPurchaseOrder(final Long id) {
        final PurchaseOrderHeader existingPO = getPurchaseOrderHeaderById(id);
        if (existingPO.getStatus() == PurchaseOrderStatus.RECEIVED ||
                existingPO.getStatus() == PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new IllegalStateException("Cannot cancel a purchase order that has already been partially or fully received.");
        }
        if (existingPO.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new IllegalStateException("Purchase order is already cancelled.");
        }
        existingPO.setStatus(PurchaseOrderStatus.CANCELLED);
    }

    @Override
    public void deletePurchaseOrderHeader(final Long id) {
        final PurchaseOrderHeader existingPO = getPurchaseOrderHeaderById(id);
        if (existingPO.getStatus() != PurchaseOrderStatus.DRAFT) {
            throw new IllegalStateException("Only purchase orders in DRAFT status can be deleted.");
        }
        purchaseOrderHeaderRepo.delete(existingPO);
    }

    @Override
    public void updatePurchaseOrderReceivingStatus(final Long purchaseOrderId) {
        final PurchaseOrderHeader pHeader = getPurchaseOrderHeaderById(purchaseOrderId);
        if (pHeader.getPurchaseOrderLines() == null || pHeader.getPurchaseOrderLines().isEmpty()) {
            return;
        }
        boolean hasReceivedQuantity = false;
        boolean fullyReceived = true;

        for (final PurchaseOrderLine poLine : pHeader.getPurchaseOrderLines()) {
            final int ordered = poLine.getQuantityOrdered();
            final int received = poLine.getQuantityReceived() == null ? 0 : poLine.getQuantityReceived();
            if (received > 0) {
                hasReceivedQuantity = true;
            }
            if (received < ordered) {
                fullyReceived = false;
            }
        }
        if (fullyReceived) {
            pHeader.setStatus(PurchaseOrderStatus.RECEIVED);
        } else if (hasReceivedQuantity) {
            pHeader.setStatus(PurchaseOrderStatus.PARTIALLY_RECEIVED);
        }
    }

    private void validateForCreate(final PurchaseOrderHeader purchaseOrder) {
        validateForeignKeys(purchaseOrder);
        validateDates(purchaseOrder);
    }

    private void validateForUpdate(final PurchaseOrderHeader purchaseOrder) {
        validateForeignKeys(purchaseOrder);
        validateDates(purchaseOrder);
    }

    private void validateForeignKeys(final PurchaseOrderHeader purchaseOrder) {
        System.out.println("Vendor ID received: "
                + purchaseOrder.getVendor().getId());

        System.out.println("Vendor exists: "
                + vendorRepo.existsById(purchaseOrder.getVendor().getId()));
        if (purchaseOrder.getVendor() == null || purchaseOrder.getVendor().getId() == null ||
                !vendorRepo.existsById(purchaseOrder.getVendor().getId())) {
            throw new IllegalArgumentException("Vendor does not exist with the provided ID.");
        }
        if (purchaseOrder.getWarehouse() == null || purchaseOrder.getWarehouse().getId() == null ||
                !warehouseRepo.existsById(purchaseOrder.getWarehouse().getId())) {
            throw new IllegalArgumentException("Warehouse does not exist with the provided ID.");
        }
    }

    private void validateDates(final PurchaseOrderHeader purchaseOrder) {
        if (purchaseOrder.getExpectedDeliveryDate() != null && purchaseOrder.getOrderDate() != null) {
            if (purchaseOrder.getExpectedDeliveryDate().isBefore(purchaseOrder.getOrderDate())) {
                throw new IllegalArgumentException("Expected delivery date cannot be before order date.");
            }
        }
    }
}