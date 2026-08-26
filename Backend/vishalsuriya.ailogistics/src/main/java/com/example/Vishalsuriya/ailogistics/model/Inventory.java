package com.example.Vishalsuriya.ailogistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Entity
@Table(name = "inventory",
uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"product_id","warehouse_id"}
        )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Min(0)
    @Column(nullable = false)
    private Integer quantityOnHand = 0;

    @Min(0)
    @Column(nullable = false)
    private Integer reservedStock = 0;

    @Min(0)
    @Column(nullable = false)
    private Integer damagedStock = 0;

    @Min(1)
    @Column(nullable = false)
    private Integer minimumThreshold;

    @Min(0)
    @Column(nullable = false)
    private Integer reorderQuantity = 0;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Transient
    public Integer getAvailableStock() {
        return Math.max(0, quantityOnHand - reservedStock - damagedStock);
    }
}
