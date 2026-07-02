package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_receipt_headers")
public class PurchaseReceiptHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "trx_number", unique = true, nullable = false)
    private String trxNumber;

    @Column(name = "po_header_id", nullable = false)
    private Long purchaseOrderHeaderId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "received_by", nullable = false)
    private String receivedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseReceiptStatus status;

    @Column(nullable = false)
    private LocalDate receivedDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = PurchaseReceiptStatus.DRAFT;
        }

        if (receivedDate == null) {
            receivedDate = LocalDate.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getPurchaseOrderHeaderId() {
        return purchaseOrderHeaderId;
    }

    public void setPurchaseOrderHeaderId(Long purchaseOrderHeaderId) {
        this.purchaseOrderHeaderId = purchaseOrderHeaderId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public PurchaseReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseReceiptStatus status) {
        this.status = status;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getTrxNumber() {
        return trxNumber;
    }

    public void setTrxNumber(String trxNumber) {
        this.trxNumber = trxNumber;
    }
}
