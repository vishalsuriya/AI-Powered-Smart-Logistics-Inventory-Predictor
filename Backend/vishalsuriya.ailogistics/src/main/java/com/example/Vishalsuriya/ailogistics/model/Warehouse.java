package com.example.Vishalsuriya.ailogistics.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @Column(name = "warehouse_name", nullable = false, unique = true)
    private String warehouseName;

    @Column(name = "warehouse_manager")
    private String warehouseManager;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phn_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    private String city;

    private String state;

    private String country;

    @Min(value = 1, message = "Capacity must be greater than 0")
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
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
        if(warehouseStatus == null){
            warehouseStatus = WarehouseStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
