package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.vendor.VendorResponseDTO;
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
    public ResponseEntity<List<VendorResponseDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorResponseDTO> getVendorById(@PathVariable final Long vendorId) {
        return ResponseEntity.ok(vendorService.getVendorById(vendorId));
    }

    @GetMapping("/code/{vendorCode}")
    public ResponseEntity<VendorResponseDTO> getByVendorCode(@PathVariable final String vendorCode) {
        return ResponseEntity.ok(vendorService.getByVendorCode(vendorCode));
    }

    @GetMapping("/company-name/{companyName}")
    public ResponseEntity<VendorResponseDTO> getByCompanyName(@PathVariable final String companyName) {
        return ResponseEntity.ok(vendorService.getByCompanyName(companyName));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<VendorResponseDTO> getByEmail(@PathVariable final String email) {
        return ResponseEntity.ok(vendorService.getByEmail(email));
    }

    @GetMapping("/contact-number/{contactNumber}")
    public ResponseEntity<VendorResponseDTO> getByContactNumber(@PathVariable final String contactNumber) {
        return ResponseEntity.ok(vendorService.getByContactNumber(contactNumber));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VendorResponseDTO>> getByVendorStatus(@PathVariable final VendorStatus status) {
        return ResponseEntity.ok(vendorService.getByVendorStatus(status));
    }
    
    @PostMapping
    public ResponseEntity<VendorResponseDTO> createVendor(@RequestBody @Valid final VendorRequestDTO requestDTO) {
        final VendorResponseDTO savedVendor = vendorService.createVendor(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVendor);
    }

    @PutMapping("/{vendorId}")
    public ResponseEntity<Void> updateVendor(@PathVariable final Long vendorId, @Valid @RequestBody final VendorRequestDTO requestDTO) {
        vendorService.updateVendor(vendorId, requestDTO);
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
