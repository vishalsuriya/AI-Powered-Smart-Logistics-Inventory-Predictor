package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;
import com.example.Vishalsuriya.ailogistics.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(final VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable final Long vendorId) {
        return ResponseEntity.ok(vendorService.getVendorById(vendorId));
    }

    @GetMapping("/code/{vendorCode}")
    public ResponseEntity<Vendor> getByVendorCode(@PathVariable final String vendorCode) {
        return ResponseEntity.ok(vendorService.getByVendorCode(vendorCode));
    }

    @GetMapping("/company-name/{companyName}")
    public ResponseEntity<Vendor> getByCompanyName(@PathVariable final String companyName) {
        return ResponseEntity.ok(vendorService.getByCompanyName(companyName));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Vendor> getByEmail(@PathVariable final String email) {
        return ResponseEntity.ok(vendorService.getByEmail(email));
    }

    @GetMapping("/contact-number/{contactNumber}")
    public ResponseEntity<Vendor> getByContactNumber(@PathVariable final String contactNumber) {
        return ResponseEntity.ok(vendorService.getByContactNumber(contactNumber));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vendor>> getByVendorStatus(@PathVariable final VendorStatus status) {
        return ResponseEntity.ok(vendorService.getByVendorStatus(status));
    }
    
    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody @Valid final Vendor vendor) {
        final Vendor savedVendor = vendorService.createVendor(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVendor);
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<Void> updateVendor(@PathVariable final Long vendorId, @Valid @RequestBody final Vendor vendor) {
        vendorService.updateVendor(vendorId, vendor);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vendorId}/activate")
    public ResponseEntity<Void> activateVendor(@PathVariable final Long vendorId) {
        vendorService.activateVendor(vendorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vendorId}/deactivate")
    public ResponseEntity<Void> deactivateVendor(@PathVariable final Long vendorId) {
        vendorService.deactivateVendor(vendorId);
        return ResponseEntity.ok().build();
    }

}
