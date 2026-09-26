package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_receipt_headers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReceiptHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "trx_number", unique = true, nullable = false)
    private String trxNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_header_id", nullable = false)
    private PurchaseOrderHeader purchaseOrderHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "received_by", nullable = false)
    private String receivedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseReceiptStatus status;

    @Column(length = 1000)
    private String remarks;

    @Column(nullable = false)
    private LocalDate receivedDate;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "purchaseReceiptHeader",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PurchaseReceiptLine> purchaseReceiptLines = new ArrayList<>();

    @Column(nullable = false)
    private Integer committed = 0;

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
        if (committed == null) {
            committed = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addPurchaseReceiptLine(final PurchaseReceiptLine line) {
        purchaseReceiptLines.add(line);
        line.setPurchaseReceiptHeader(this);
    }

    public void removePurchaseReceiptLine(final PurchaseReceiptLine line) {
        purchaseReceiptLines.remove(line);
        line.setPurchaseReceiptHeader(null);
    }
}
