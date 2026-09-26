package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.*;
import com.example.Vishalsuriya.ailogistics.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseReceiptHeaderServiceImpl implements PurchaseReceiptHeaderService {

    private final PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepo;
    private final PurchaseOrderHeaderRepository purchaseOrderHeaderRepo;
    private final VendorRepository vendorRepo;
    private final WarehouseRepository warehouseRepo;
    private final PurchaseOrderLineRepository purchaseOrderLineRepo;
    private final ProductRepository productRepo;
    private final InventoryRepository inventoryRepo;
    private final PurchaseOrderHeaderService purchaseOrderHeaderService;

    public PurchaseReceiptHeaderServiceImpl(
            final PurchaseReceiptHeaderRepository purchaseReceiptHeaderRepository,
            final PurchaseOrderHeaderRepository purchaseOrderHeaderRepository,
            final VendorRepository vendorRepository,
            final WarehouseRepository warehouseRepository, final PurchaseOrderLineRepository purchaseOrderLineRepo, final ProductRepository productRepo, final InventoryRepository inventoryRepo, final PurchaseOrderHeaderService purchaseOrderHeaderService) {
        this.purchaseReceiptHeaderRepo = purchaseReceiptHeaderRepository;
        this.purchaseOrderHeaderRepo = purchaseOrderHeaderRepository;
        this.vendorRepo = vendorRepository;
        this.warehouseRepo = warehouseRepository;
        this.purchaseOrderLineRepo = purchaseOrderLineRepo;
        this.productRepo = productRepo;
        this.inventoryRepo = inventoryRepo;
        this.purchaseOrderHeaderService = purchaseOrderHeaderService;
    }

    @Override
    public String generateTrxNumber() {
        final long nextId = purchaseReceiptHeaderRepo.count() + 1;
        final int year = java.time.LocalDate.now().getYear();
        return String.format("PR-%d-%03d", year, nextId);
    }

    @Override
    public List<PurchaseReceiptHeader> getAllPurchaseReceiptHeaders() {
        return purchaseReceiptHeaderRepo.findAll();
    }

    @Override
    public PurchaseReceiptHeader getPurchaseReceiptHeaderById(final Long id) {
        return purchaseReceiptHeaderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase receipt header not found with ID: " + id));
    }

    @Override
    public PurchaseReceiptHeader getPurchaseReceiptHeaderByTrxNumber(final String trxNumber) {
        return purchaseReceiptHeaderRepo.findByTrxNumber(trxNumber)
                .orElseThrow(() -> new EntityNotFoundException("Purchase receipt header not found with trxNumber: " + trxNumber));
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByPurchaseOrderHeaderId(final Long purchaseOrderHeaderId) {
        return purchaseReceiptHeaderRepo.findByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByVendorId(final Long vendorId) {
        return purchaseReceiptHeaderRepo.findByVendorId(vendorId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByWarehouseId(final Long warehouseId) {
        return purchaseReceiptHeaderRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByStatus(final PurchaseReceiptStatus status) {
        return purchaseReceiptHeaderRepo.findByStatus(status);
    }

    @Override
    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByReceivedBy(final String receivedBy) {
        return purchaseReceiptHeaderRepo.findByReceivedBy(receivedBy);
    }

    @Override
    public boolean existsByTrxNumber(final String trxNumber) {
        return purchaseReceiptHeaderRepo.existsByTrxNumber(trxNumber);
    }

    @Override
    @Transactional
    public void addPurchaseReceiptHeader(final PurchaseReceiptHeader purchaseReceiptHeader) {
        validateForCreate(purchaseReceiptHeader);
        final PurchaseOrderHeader poHeader = purchaseOrderHeaderRepo.findById(purchaseReceiptHeader.getPurchaseOrderHeader().getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase Order not found with ID: " + purchaseReceiptHeader.getPurchaseOrderHeader().getId()));

        final Vendor vendor = vendorRepo.findById(purchaseReceiptHeader.getVendor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + purchaseReceiptHeader.getVendor().getId()));

        final Warehouse warehouse = warehouseRepo.findById(purchaseReceiptHeader.getWarehouse().getId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: " + purchaseReceiptHeader.getWarehouse().getId()));

        if (poHeader.getStatus() != PurchaseOrderStatus.APPROVED &&
                poHeader.getStatus() != PurchaseOrderStatus.PARTIALLY_RECEIVED) {
            throw new IllegalArgumentException("PR can be created only if the PO is in Approved or Partially Received status.");
        }
        if (poHeader.getVendor() != null && !poHeader.getVendor().getId().equals(vendor.getId())) {
            throw new IllegalArgumentException("Selected Vendor does not match the Vendor on the Purchase Order.");
        }
        if (poHeader.getWarehouse() != null && !poHeader.getWarehouse().getId().equals(warehouse.getId())) {
            throw new IllegalArgumentException("Selected Warehouse does not match the warehouse on the Purchase Order.");
        }
        purchaseReceiptHeader.setPurchaseOrderHeader(poHeader);
        purchaseReceiptHeader.setVendor(vendor);
        purchaseReceiptHeader.setWarehouse(warehouse);
        purchaseReceiptHeader.setTrxNumber(generateTrxNumber());
        purchaseReceiptHeader.setStatus(PurchaseReceiptStatus.DRAFT);
        purchaseReceiptHeaderRepo.save(purchaseReceiptHeader);
    }

    @Override
    @Transactional
    public void updatePurchaseReceiptHeader(final Long id, final PurchaseReceiptHeader purchaseReceiptHeader) {
        final PurchaseReceiptHeader existingHeader = purchaseReceiptHeaderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase receipt header not found with ID: " + id));
        if (existingHeader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalArgumentException("Only DRAFT Purchase Receipt can be updated.");
        }
        if (purchaseReceiptHeader.getReceivedBy() != null && !purchaseReceiptHeader.getReceivedBy().trim().isEmpty()) {
            existingHeader.setReceivedBy(purchaseReceiptHeader.getReceivedBy());
        }
        if (purchaseReceiptHeader.getReceivedDate() != null) {
            existingHeader.setReceivedDate(purchaseReceiptHeader.getReceivedDate());
        }
        if (purchaseReceiptHeader.getRemarks() != null) {
            existingHeader.setRemarks(purchaseReceiptHeader.getRemarks());
        }
    }

    @Override
    @Transactional
    public void deletePurchaseReceiptHeader(final Long id) {
        final PurchaseReceiptHeader existingHeader = purchaseReceiptHeaderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot delete: Purchase receipt not found with ID: " + id));
        if (existingHeader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalArgumentException(
                    "Only DRAFT Purchase Receipt can be deleted."
            );
        }

        if (!existingHeader.getPurchaseReceiptLines().isEmpty()) {
            throw new IllegalArgumentException(
                    "Purchase Receipt cannot be deleted because it contains receipt lines."
            );
        }
        purchaseReceiptHeaderRepo.delete(existingHeader);
    }

    private void validateForCreate(final PurchaseReceiptHeader purchaseReceiptHeader) {
        if (purchaseReceiptHeader == null) {
            throw new IllegalArgumentException("Purchase receipt header cannot be null.");
        }
        if (purchaseReceiptHeader.getPurchaseOrderHeader() == null || purchaseReceiptHeader.getPurchaseOrderHeader().getId() == null) {
            throw new IllegalArgumentException("Purchase Order reference is required.");
        }
        if (purchaseReceiptHeader.getVendor() == null || purchaseReceiptHeader.getVendor().getId() == null) {
            throw new IllegalArgumentException("Vendor reference is required.");
        }
        if (purchaseReceiptHeader.getWarehouse() == null || purchaseReceiptHeader.getWarehouse().getId() == null) {
            throw new IllegalArgumentException("Warehouse reference is required.");
        }
        if (purchaseReceiptHeader.getReceivedBy() == null || purchaseReceiptHeader.getReceivedBy().trim().isEmpty()) {
            throw new IllegalArgumentException("ReceivedBy field is required.");
        }
    }

    @Transactional
    @Override
    public void commitPurchaseReceipt(final Long id) {
        final PurchaseReceiptHeader prheader = purchaseReceiptHeaderRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase Receipt not found with ID: " + id));
        if (prheader.getCommitted() == 1) {
            throw new IllegalStateException("Purchase Receipt '" + prheader.getTrxNumber() + "' is already committed.");
        }

        if (prheader.getStatus() != PurchaseReceiptStatus.DRAFT) {
            throw new IllegalStateException(
                    "Purchase Receipt '" + prheader.getTrxNumber()
                            + "' cannot be committed because its status is "
                            + prheader.getStatus() + ". Only DRAFT Purchase Receipts can be committed.");
        }
        if (prheader.getPurchaseReceiptLines() == null
                || prheader.getPurchaseReceiptLines().isEmpty()) {
            throw new IllegalStateException("Purchase Receipt '" + prheader.getTrxNumber()
                    + "' cannot be committed because it contains no receipt lines.");
        }
        if (prheader.getPurchaseOrderHeader() == null || prheader.getPurchaseOrderHeader().getId() == null) {
            throw new IllegalStateException("Purchase Receipt '" + prheader.getTrxNumber()
                    + "' is not associated with a Purchase Order.");
        }
        final List<PurchaseReceiptLine> prLinesList = prheader.getPurchaseReceiptLines();
        for (final PurchaseReceiptLine prLine : prLinesList) {
            if (prLine.getPurchaseOrderLine() == null || prLine.getPurchaseOrderLine().getId() == null) {
                throw new IllegalStateException("Purchase Receipt Line ID " + prLine.getId()
                        + " is not associated with a Purchase Order Line.");
            }
            final Long poLineId = prLine.getPurchaseOrderLine().getId();
            final PurchaseOrderLine poLine = purchaseOrderLineRepo.findById(poLineId)
                    .orElseThrow(() -> new EntityNotFoundException("Purchase Order Line not found with ID: " + poLineId));
            if (poLine.getPurchaseOrderHeader() == null || poLine.getPurchaseOrderHeader().getId() == null) {
                throw new IllegalStateException("Purchase Order Line ID " + poLineId + " is not associated with a Purchase Order.");
            }
            if (!poLine.getPurchaseOrderHeader().getId().equals(prheader.getPurchaseOrderHeader().getId())) {
                throw new IllegalArgumentException(
                        "Purchase Receipt Line ID " + prLine.getId()
                                + " references Purchase Order Line ID " + poLineId
                                + ", which does not belong to Purchase Order ID "
                                + prheader.getPurchaseOrderHeader().getId() + ".");
            }
            if (prLine.getProduct() == null || prLine.getProduct().getId() == null) {
                throw new IllegalStateException("Purchase Receipt Line ID " + prLine.getId() + " does not have a valid Product reference.");
            }
            if (poLine.getProduct() == null
                    || poLine.getProduct().getId() == null) {
                throw new IllegalStateException(
                        "Purchase Order Line ID " + poLineId
                                + " does not have a valid Product reference.");
            }
            if (!prLine.getProduct().getId().equals(poLine.getProduct().getId())) {
                throw new IllegalArgumentException(
                        "Product mismatch for Purchase Receipt Line ID " + prLine.getId()
                                + ". The received Product does not match the Product "
                                + "specified in Purchase Order Line ID " + poLineId + ".");
            }
            final Integer newQuantityReceived = prLine.getQuantityReceived();
            if (newQuantityReceived == null || newQuantityReceived <= 0) {
                throw new IllegalArgumentException(
                        "Received quantity must be greater than zero for Purchase Receipt Line ID "
                                + prLine.getId() + ".");
            }
            final int remainingQuantity = poLine.getRemainingQuantity();
            if (newQuantityReceived > remainingQuantity) {
                throw new IllegalArgumentException(
                        "Cannot receive " + newQuantityReceived
                                + " units for Purchase Receipt Line ID " + prLine.getId()
                                + ". Purchase Order Line ID " + poLineId
                                + " has only " + remainingQuantity
                                + " units remaining to be received.");
            }
            poLine.receiveQuantity(newQuantityReceived);
            final Product product = productRepo.findById(prLine.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + prLine.getProduct().getId()));
            final Warehouse warehouse = warehouseRepo.findById(
                            poLine.getPurchaseOrderHeader().getWarehouse().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse not found with ID: "
                            + poLine.getPurchaseOrderHeader().getWarehouse().getId()));

            final Inventory inventory = inventoryRepo.findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Inventory record not found for Product ID "
                            + product.getId()
                            + " and Warehouse ID "
                            + warehouse.getId()
                            + ". Create the inventory record before committing this Purchase Receipt."));

            final int existingQuantityOnHand = inventory.getQuantityOnHand();
            inventory.setQuantityOnHand(existingQuantityOnHand + newQuantityReceived);
            purchaseOrderHeaderService.updatePurchaseOrderReceivingStatus(poLine.getPurchaseOrderHeader().getId());
        }
        prheader.setCommitted(1);
        prheader.setStatus(PurchaseReceiptStatus.COMPLETED);
    }
}