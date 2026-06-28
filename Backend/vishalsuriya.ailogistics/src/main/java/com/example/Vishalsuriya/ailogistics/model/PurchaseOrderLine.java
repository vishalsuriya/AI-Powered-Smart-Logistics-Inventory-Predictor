package com.example.Vishalsuriya.ailogistics.model;


import jakarta.persistence.*;

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
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "purchase_order_header_id", nullable = false)
    private Long purchaseOrderHeaderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity_ordered", nullable = false)
    private Integer quantityOrdered;

    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived = 0;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false)
    private BigDecimal lineTotal;

    @Column(updatable = false)
    private LocalDateTime createdAt;

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

    public Long getPurchaseOrderHeaderId() {
        return purchaseOrderHeaderId;
    }

    public void setPurchaseOrderHeaderId(Long purchaseOrderHeaderId) {
        this.purchaseOrderHeaderId = purchaseOrderHeaderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Integer getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(Integer quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

}
