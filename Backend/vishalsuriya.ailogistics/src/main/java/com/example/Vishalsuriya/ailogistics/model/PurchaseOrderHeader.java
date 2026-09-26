package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order_headers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "trx_number", unique = true, nullable = false)
    private String trxNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOrderStatus status;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(length = 1000)
    private String remarks;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @OneToMany(
            mappedBy = "purchaseOrderHeader",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = PurchaseOrderStatus.DRAFT;
        }

        if (orderDate == null) {
            orderDate = LocalDate.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addPurchaseOrderLine(final PurchaseOrderLine line) {
        purchaseOrderLines.add(line);
        line.setPurchaseOrderHeader(this);
    }

    public void removePurchaseOrderLine(final PurchaseOrderLine line) {
        purchaseOrderLines.remove(line);
        line.setPurchaseOrderHeader(null);
    }
}
