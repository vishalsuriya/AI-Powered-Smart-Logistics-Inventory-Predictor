package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepo;

    public VendorServiceImpl(final VendorRepository vendorRepository) {
        this.vendorRepo = vendorRepository;
    }

    public String generateVendorCode() {
        final long nextId = vendorRepo.count() + 1;
        return String.format("VEN-%03d", nextId);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepo.findAll();
    }

    @Override
    public Vendor getVendorById(final Long vendorId) {
        return vendorRepo.findById(vendorId).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + vendorId));
    }

    @Override
    public Vendor getByVendorCode(final String vendorCode) {
        return vendorRepo.findByVendorCode(vendorCode).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with vendorCode: " + vendorCode));
    }

    @Override
    public Vendor getByCompanyName(final String companyName) {
        return vendorRepo.findByCompanyName(companyName).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with companyName: " + companyName));
    }

    @Override
    public Vendor getByEmail(final String email) {
        return vendorRepo.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with email: " + email));
    }

    @Override
    public Vendor getByContactNumber(final String contactNumber) {
        return vendorRepo.findByContactNumber(contactNumber).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with contactNumber: " + contactNumber));
    }

    @Override
    public List<Vendor> getByVendorStatus(final VendorStatus vendorStatus) {
        return vendorRepo.findByVendorStatus(vendorStatus);
    }

    @Override
    @Transactional
    public Vendor createVendor(final Vendor vendor) {
        final String vendorCode = generateVendorCode();
        validateForCreate(vendor);
        vendor.setVendorCode(vendorCode);
        return vendorRepo.save(vendor);
    }

    @Override
    @Transactional
    public Vendor updateVendor(final Long vendorId, final Vendor vendor) {
        final Vendor existingVendor = findVendor(vendorId);
        validateForUpdate(existingVendor, vendor);
        existingVendor.setAddress(vendor.getAddress());
        existingVendor.setCity(vendor.getCity());
        existingVendor.setCountry(vendor.getCountry());
        existingVendor.setContactNumber(vendor.getContactNumber());
        existingVendor.setContactPerson(vendor.getContactPerson());
        existingVendor.setCurrencyType(vendor.getCurrencyType());
        existingVendor.setEmail(vendor.getEmail());
        existingVendor.setPaymentTerms(vendor.getPaymentTerms());
        existingVendor.setState(vendor.getState());
        existingVendor.setVendorStatus(vendor.getVendorStatus());
        existingVendor.setCompanyName(vendor.getCompanyName());
        existingVendor.setTaxNumber(vendor.getTaxNumber());
        return existingVendor;
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

    private void validateForCreate(final Vendor vendor) {
        if (vendorRepo.existsByEmail(vendor.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (vendorRepo.existsByCompanyName(vendor.getCompanyName())) {
            throw new IllegalArgumentException("CompanyName already exists.");
        }
        if (vendorRepo.existsByTaxNumber(vendor.getTaxNumber())) {
            throw new IllegalArgumentException("Tax number already exists.");
        }
        if (vendorRepo.existsByContactNumber(vendor.getContactNumber())) {
            throw new IllegalArgumentException("Contact number already exists.");
        }
    }

    private void validateForUpdate(final Vendor existingVendor, final Vendor updatedVendor) {
        final String newEmail = updatedVendor.getEmail();
        final String newContactNumber = updatedVendor.getContactNumber();

        if (newEmail != null && !newEmail.equals(existingVendor.getEmail())
                && vendorRepo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (newContactNumber != null && !newContactNumber.equals(existingVendor.getContactNumber())
                && vendorRepo.existsByContactNumber(newContactNumber)) {
            throw new IllegalArgumentException("Contact number already exists.");
        }
        if (!existingVendor.getCompanyName().equals(updatedVendor.getCompanyName())
                && vendorRepo.existsByCompanyName(updatedVendor.getCompanyName())) {
            throw new IllegalArgumentException("Company name already exists.");
        }
        if (!existingVendor.getTaxNumber().equals(updatedVendor.getTaxNumber())
                && vendorRepo.existsByTaxNumber(updatedVendor.getTaxNumber())) {
            throw new IllegalArgumentException("Tax number already exists.");
        }
    }
}
