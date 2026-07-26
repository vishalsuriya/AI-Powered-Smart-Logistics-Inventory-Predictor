package com.example.Vishalsuriya.ailogistics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "vendor_code", unique = true, nullable = false)
    private String vendorCode;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column( unique = true, nullable = false)
    private String email;

    @Column(unique = true , nullable = false)
    private String taxNumber;

    @Column(name = "phn_number", unique = true)
    private String contactNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(name = "vendor_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VendorStatus vendorStatus;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "payment_terms", nullable = false)
    private String paymentTerms;

    @Column(nullable = false)
    private String address;

    private String city;

    private String state;

    private String country;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if(vendorStatus == null){
            vendorStatus = VendorStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
