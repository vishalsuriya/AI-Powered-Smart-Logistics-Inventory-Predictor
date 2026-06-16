package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Vendor;

import java.util.List;

public interface VendorService {

    public List<Vendor> getAllVendors();

    public Vendor getVendorById(Long vendorId);

    public Vendor getVendorByVendorCode(String vendorCode);

    public void addVendor(Vendor vendor);

    public void updateVendor(Long vendorId, Vendor vendor);

    public void deleteVendor(Long vendorId);
}
