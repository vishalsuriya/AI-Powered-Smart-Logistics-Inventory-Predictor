package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;

import java.util.List;

public interface VendorService {

    List<Vendor> getAllVendors();

    Vendor getVendorById(Long vendorId);

    Vendor getByVendorCode(String vendorCode);

    Vendor getByCompanyName(String companyName);

    Vendor getByEmail(String email);

    Vendor getByContactNumber(String contactNumber);

    List<Vendor> getByVendorStatus(VendorStatus vendorStatus);

    Vendor createVendor(Vendor vendor);

    Vendor updateVendor(Long vendorId, Vendor vendor);

    void activateVendor(Long vendorId);

    void deactivateVendor(Long vendorId);

    boolean existsById(Long vendorId);

    boolean existsByVendorCode(String vendorCode);

    boolean existsByCompanyName(String companyName);

    boolean existsByEmail(String email);

    boolean existsByTaxNumber(String taxNumber);

    boolean existsByContactNumber(String contactNumber);
}
