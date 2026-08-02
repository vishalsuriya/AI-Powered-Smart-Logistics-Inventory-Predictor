package com.example.Vishalsuriya.ailogistics.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false, unique = true, length = 150)
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @NotBlank(message = "Tax number is required")
    @Column(unique = true, nullable = false, length = 20)
    private String taxNumber;

    @NotBlank(message = "Contact number is required")
    @Column(unique = true, length = 20)
    private String contactNumber;

    @NotNull(message = "Currency type is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @NotNull(message = "Vendor status is required")
    @Column(name = "vendor_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VendorStatus vendorStatus;

    @Column(name = "contact_person", length = 50)
    private String contactPerson;

    @NotBlank(message = "Payment terms are required")
    @Column(name = "payment_terms", nullable = false, length = 100)
    private String paymentTerms;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 500)
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
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
        if(vendorStatus == null){
            vendorStatus = VendorStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
