package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByVendorCode(String vendorCode);

    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByContactNumber(String contactNumber);

    Optional<Vendor> findByCompanyName(String companyName);

    List<Vendor> findByVendorStatus(VendorStatus vendorStatus);

    boolean existsByVendorCode(String vendorCode);

    boolean existsByEmail(String email);

    boolean existsByTaxNumber(String taxNumber);

    boolean existsByContactNumber(String contactNumber);

    boolean existsByCompanyName(String companyName);
}