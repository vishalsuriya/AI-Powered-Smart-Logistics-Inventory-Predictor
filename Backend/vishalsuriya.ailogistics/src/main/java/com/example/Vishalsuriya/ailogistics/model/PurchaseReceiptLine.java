package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_header_id", nullable = false)
    private PurchaseReceiptHeader purchaseReceiptHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_line_id", nullable = false)
    private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1)
    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Positive
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Column(length = 500)
    private String remarks;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }
    private void calculateLineTotal() {

        if(unitPrice != null && quantityReceived != null){
            lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantityReceived));
        }else{
            lineTotal = BigDecimal.ZERO;
        }
    }

    public void validateReceipt() {

        if(quantityReceived <= 0){
            throw new IllegalArgumentException("Quantity should be greater than zero.");
        }

        if(unitPrice.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Unit price should be greater than zero.");
        }
    }
}
