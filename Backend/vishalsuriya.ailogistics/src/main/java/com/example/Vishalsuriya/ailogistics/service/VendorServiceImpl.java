package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorResponseDTO;
import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepo;

    public VendorServiceImpl(final VendorRepository vendorRepository) {
        this.vendorRepo = vendorRepository;
    }

    private VendorResponseDTO mapToResponseDTO(final Vendor vendor) {
        final VendorResponseDTO vendorResponseDTO = new VendorResponseDTO();
        vendorResponseDTO.setAddress(vendor.getAddress());
        vendorResponseDTO.setCity(vendor.getCity());
        vendorResponseDTO.setCompanyName(vendor.getCompanyName());
        vendorResponseDTO.setContactNumber(vendor.getContactNumber());
        vendorResponseDTO.setContactPerson(vendor.getContactPerson());
        vendorResponseDTO.setCountry(vendor.getCountry());
        vendorResponseDTO.setEmail(vendor.getEmail());
        vendorResponseDTO.setPaymentTerms(vendor.getPaymentTerms());
        vendorResponseDTO.setState(vendor.getState());
        vendorResponseDTO.setTaxNumber(vendor.getTaxNumber());
        vendorResponseDTO.setVendorCode(vendor.getVendorCode());
        vendorResponseDTO.setVendorStatus(vendor.getVendorStatus());
        vendorResponseDTO.setCurrencyType(vendor.getCurrencyType());
        return vendorResponseDTO;
    }
    @Override
    public List<VendorResponseDTO> getAllVendors() {
        final List<Vendor> vendors = vendorRepo.findAll();
        final List<VendorResponseDTO> vendorResponseDTOList = new ArrayList<>();
        for (final Vendor vendor : vendors) {
            vendorResponseDTOList.add(mapToResponseDTO(vendor));
        }
        return vendorResponseDTOList;
    }

    @Override
    public VendorResponseDTO getVendorById(final Long vendorId) {
        final Vendor vendor = vendorRepo.findById(vendorId).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + vendorId));
        return mapToResponseDTO(vendor);
    }

    @Override
    public VendorResponseDTO getByVendorCode(final String vendorCode) {
        final Vendor vendor = vendorRepo.findByVendorCode(vendorCode).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with vendorCode: " + vendorCode));
        return mapToResponseDTO(vendor);
    }

    @Override
    public VendorResponseDTO getByCompanyName(final String companyName) {
        final Vendor vendor = vendorRepo.findByCompanyName(companyName).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with companyName: " + companyName));
        return mapToResponseDTO(vendor);
    }

    @Override
    public VendorResponseDTO getByEmail(final String email) {
        final Vendor vendor = vendorRepo.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with email: " + email));
        return mapToResponseDTO(vendor);
    }

    @Override
    public VendorResponseDTO getByContactNumber(final String contactNumber) {
        final Vendor vendor = vendorRepo.findByContactNumber(contactNumber).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with contactNumber: " + contactNumber));
        return mapToResponseDTO(vendor);
    }

    @Override
    public List<VendorResponseDTO> getByVendorStatus(final VendorStatus vendorStatus) {
        final List<Vendor> vendors = vendorRepo.findByVendorStatus(vendorStatus);
        final List<VendorResponseDTO> vendorResponseDTOList = new ArrayList<>();
        for (final Vendor vendor : vendors) {
            vendorResponseDTOList.add(mapToResponseDTO(vendor));
        }
        return vendorResponseDTOList;
    }

    @Override
    @Transactional
    public VendorResponseDTO createVendor(final VendorRequestDTO requestDTO) {
        validateForCreate(requestDTO);
        final Vendor vendor = new Vendor();
        vendor.setVendorCode("TEMP-" + UUID.randomUUID().toString());
        vendor.setTaxNumber(requestDTO.getTaxNumber());
        vendor.setAddress(requestDTO.getAddress());
        vendor.setCity(requestDTO.getCity());
        vendor.setContactNumber(requestDTO.getContactNumber());
        vendor.setContactPerson(requestDTO.getContactPerson());
        vendor.setCompanyName(requestDTO.getCompanyName());
        vendor.setCurrencyType(requestDTO.getCurrencyType());
        vendor.setEmail(requestDTO.getEmail());
        vendor.setCountry(requestDTO.getCountry());
        vendor.setPaymentTerms(requestDTO.getPaymentTerms());
        vendor.setState(requestDTO.getState());
        final Vendor savedVendor = vendorRepo.save(vendor);
        final String vendorCode = String.format("VEN-%03d", savedVendor.getId());
        savedVendor.setVendorCode(vendorCode);
        final Vendor finalizedVendor = vendorRepo.save(savedVendor);
        return mapToResponseDTO(finalizedVendor);
    }

    @Override
    @Transactional
    public void updateVendor(final Long vendorId, final VendorRequestDTO requestDTO) {
        final Vendor existingVendor = findVendor(vendorId);
        validateForUpdate(existingVendor, requestDTO);
        existingVendor.setAddress(requestDTO.getAddress());
        existingVendor.setCity(requestDTO.getCity());
        existingVendor.setCountry(requestDTO.getCountry());
        existingVendor.setContactNumber(requestDTO.getContactNumber());
        existingVendor.setContactPerson(requestDTO.getContactPerson());
        existingVendor.setCurrencyType(requestDTO.getCurrencyType());
        existingVendor.setEmail(requestDTO.getEmail());
        existingVendor.setPaymentTerms(requestDTO.getPaymentTerms());
        existingVendor.setState(requestDTO.getState());
        existingVendor.setCompanyName(requestDTO.getCompanyName());
        existingVendor.setTaxNumber(requestDTO.getTaxNumber());
    }

    @Override
    @Transactional
    public void activateVendor(final Long vendorId) {
        final Vendor existingVendor = findVendor(vendorId);
        if (existingVendor.getVendorStatus() == VendorStatus.ACTIVE) {
            throw new IllegalArgumentException("Vendor already active.");
        }
        existingVendor.setVendorStatus(VendorStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateVendor(final Long vendorId) {
        final Vendor existingVendor = findVendor(vendorId);
        if (existingVendor.getVendorStatus() == VendorStatus.INACTIVE) {
            throw new IllegalArgumentException("Vendor already InActive");
        }
        existingVendor.setVendorStatus(VendorStatus.INACTIVE);
    }

    @Override
    public boolean existsById(final Long vendorId) {
        return vendorRepo.existsById(vendorId);
    }

    @Override
    public boolean existsByVendorCode(final String vendorCode) {
        return vendorRepo.existsByVendorCode(vendorCode);
    }

    @Override
    public boolean existsByCompanyName(final String companyName) {
        return vendorRepo.existsByCompanyName(companyName);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return vendorRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByTaxNumber(final String taxNumber) {
        return vendorRepo.existsByTaxNumber(taxNumber);
    }

    @Override
    public boolean existsByContactNumber(final String contactNumber) {
        return vendorRepo.existsByContactNumber(contactNumber);
    }

    private Vendor findVendor(final Long vendorId) {
        return vendorRepo.findById(vendorId).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found by ID. " + vendorId));
    }

    private void validateForCreate(final VendorRequestDTO requestDTO) {
        if (vendorRepo.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (vendorRepo.existsByCompanyName(requestDTO.getCompanyName())) {
            throw new IllegalArgumentException("CompanyName already exists.");
        }
        if (vendorRepo.existsByTaxNumber(requestDTO.getTaxNumber())) {
            throw new IllegalArgumentException("Tax number already exists.");
        }
        if (vendorRepo.existsByContactNumber(requestDTO.getContactNumber())) {
            throw new IllegalArgumentException("Contact number already exists.");
        }
    }

    private void validateForUpdate(final Vendor existingVendor, final VendorRequestDTO requestDTO) {
        final String newEmail = requestDTO.getEmail();
        final String newContactNumber = requestDTO.getContactNumber();

        if (newEmail != null && !newEmail.equals(existingVendor.getEmail())
                && vendorRepo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (newContactNumber != null && !newContactNumber.equals(existingVendor.getContactNumber())
                && vendorRepo.existsByContactNumber(newContactNumber)) {
            throw new IllegalArgumentException("Contact number already exists.");
        }
        if (!Objects.equals(existingVendor.getCompanyName(), requestDTO.getCompanyName())
                && requestDTO.getCompanyName() != null
                && vendorRepo.existsByCompanyName(requestDTO.getCompanyName())) {
            throw new IllegalArgumentException("Company name already exists.");
        }
        if (!Objects.equals(existingVendor.getTaxNumber(), requestDTO.getTaxNumber())
                && requestDTO.getTaxNumber() != null
                && vendorRepo.existsByTaxNumber(requestDTO.getTaxNumber())) {
            throw new IllegalArgumentException("Tax number already exists.");
        }
    }
}
