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
@Table(name = "purchase_order_lines",
uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {
                        "purchase_order_header_id",
                        "product_id"
                }
        )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_header_id", nullable = false)
    private PurchaseOrderHeader purchaseOrderHeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(1)
    @Column(name = "quantity_ordered", nullable = false)
    private Integer quantityOrdered;

    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived = 0;

    @Positive
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false)
    private BigDecimal lineTotal;

    @Column(updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (quantityReceived == null) {
            quantityReceived = 0;
        }
        calculateLineTotal();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        calculateLineTotal();
    }

    private void calculateLineTotal(){
        if(quantityOrdered != null && unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0){
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantityOrdered));
        }else{
            this.lineTotal = BigDecimal.ZERO;
        }
    }
    @Transient
    public Integer getRemainingQuantity() {
        int ordered = quantityOrdered == null ? 0 : quantityOrdered;
        int received = quantityReceived == null ? 0 : quantityReceived;
        return ordered - received;
    }

    public void receiveQuantity(Integer quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Received quantity must be greater than zero.");
        }

        if (this.quantityReceived + quantity > this.quantityOrdered) {
            throw new IllegalArgumentException(
                    "Received quantity exceeds ordered quantity.");
        }

        this.quantityReceived += quantity;
    }
    @Transient
    public boolean isCompletelyReceived() {
        return quantityOrdered.equals(quantityReceived);
    }
    @Transient
    public boolean isPartiallyReceived() {
        return quantityReceived > 0 && quantityReceived < quantityOrdered;
    }
}
