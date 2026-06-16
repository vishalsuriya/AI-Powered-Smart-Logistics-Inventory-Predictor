package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepo;

    public VendorServiceImpl(VendorRepository vendorRepository){
        this.vendorRepo = vendorRepository;
    }

    public String generateVendorCode() {
        long nextId = vendorRepo.count() + 1;
        return String.format("VEN-%03d", nextId);
    }
    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepo.findAll();
    }

    @Override
    public Vendor getVendorById(Long vendorId) {
        return vendorRepo.findById(vendorId).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + vendorId));
    }

    @Override
    public Vendor getVendorByVendorCode(String vendorCode) {
        return vendorRepo.findByVendorCode(vendorCode).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with code: " + vendorCode));
    }

    @Override
    public void addVendor(Vendor vendor) {
        String vendorCode = generateVendorCode();
        vendor.setVendorCode(vendorCode);
        vendorRepo.save(vendor);
    }

    @Override
    public void updateVendor(Long vendorId, Vendor vendor) {
        Vendor existingVendor = vendorRepo.findById((vendorId)).
                orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + vendorId));

        existingVendor.setAddress(vendor.getAddress());
        existingVendor.setCity(vendor.getCity());
        existingVendor.setCountry(vendor.getCountry());
        existingVendor.setCompanyName(vendor.getCompanyName());
        existingVendor.setContactNumber(vendor.getContactNumber());
        existingVendor.setContactPerson(vendor.getContactPerson());
        existingVendor.setCurrencyType(vendor.getCurrencyType());
        existingVendor.setEmail(vendor.getEmail());
        existingVendor.setPaymentTerms(vendor.getPaymentTerms());
        existingVendor.setState(vendor.getState());
        existingVendor.setTaxNumber(vendor.getTaxNumber());
        existingVendor.setVendorStatus(vendor.getVendorStatus());
        vendorRepo.save(existingVendor);
    }

    @Override
    public void deleteVendor(Long vendorId) {
        vendorRepo.deleteById(vendorId);
    }
}
