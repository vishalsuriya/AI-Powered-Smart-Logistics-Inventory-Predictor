package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Min(0)
    @Column(nullable = false)
    private Integer availableStock = 0;

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
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
