package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorResponseDTO;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;

import java.util.List;

public interface VendorService {

    List<VendorResponseDTO> getAllVendors();

    VendorResponseDTO getVendorById(Long vendorId);

    VendorResponseDTO getByVendorCode(String vendorCode);

    VendorResponseDTO getByCompanyName(String companyName);

    VendorResponseDTO getByEmail(String email);

    VendorResponseDTO getByContactNumber(String contactNumber);

    List<VendorResponseDTO> getByVendorStatus(VendorStatus vendorStatus);

    VendorResponseDTO createVendor(VendorRequestDTO requestDTO);

    void updateVendor(Long vendorId, VendorRequestDTO requestDTO);

    void activateVendor(Long vendorId);

    void deactivateVendor(Long vendorId);

    boolean existsById(Long vendorId);

    boolean existsByVendorCode(String vendorCode);

    boolean existsByCompanyName(String companyName);

    boolean existsByEmail(String email);

    boolean existsByTaxNumber(String taxNumber);

    boolean existsByContactNumber(String contactNumber);
}
