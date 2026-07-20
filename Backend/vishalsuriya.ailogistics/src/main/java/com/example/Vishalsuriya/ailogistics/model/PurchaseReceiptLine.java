package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "purchase_receipt_lines",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "pr_header_id",
                                "po_line_id"
                        }
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReceiptLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "pr_header_id", nullable = false)
    private Long purchaseReceiptHeaderId;

    @Column(name = "po_line_id", nullable = false)
    private Long purchaseOrderLineId;

    @Column(name = "product_id",nullable = false)
    private Long productId;

    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Column(length = 500)
    private String remarks;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(unitPrice != null && quantityReceived != null){
            lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantityReceived));
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if(unitPrice != null && quantityReceived != null){
            lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantityReceived));
        }
    }
}
