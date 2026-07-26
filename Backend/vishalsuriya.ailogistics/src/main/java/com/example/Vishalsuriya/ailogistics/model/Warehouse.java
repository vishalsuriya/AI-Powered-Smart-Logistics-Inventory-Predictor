package com.example.Vishalsuriya.ailogistics.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "warehouse_code", nullable = false,unique = true)
    private String warehouseCode;

    @Column(name = "warehouse_name",nullable = false)
    private String warehouseName;

    @Column(name = "warehouse_manager")
    private String warehouseManager;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phn_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    private String city;

    private String state;

    private String country;

    @Min(0)
    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "warehouse_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WarehouseStatus warehouseStatus;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(warehouseStatus == null){
            warehouseStatus = WarehouseStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
