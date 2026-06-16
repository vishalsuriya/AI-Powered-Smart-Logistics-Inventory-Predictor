package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public List<Vendor> getAllVendors(){
        return vendorService.getAllVendors();
    }

    @GetMapping("/{vendorId}")
    public Vendor getVendorById(@PathVariable Long vendorId){
        return vendorService.getVendorById(vendorId);
    }

    @GetMapping("/code/{vendorCode}")
    public Vendor getVendorByVendorCode(@PathVariable String vendorCode){
        return vendorService.getVendorByVendorCode(vendorCode);
    }
    @PostMapping
    public void addVendor(@RequestBody Vendor vendor){
        vendorService.addVendor(vendor);
    }

    @PutMapping("/{vendorId}")
    public void updateVendor(@PathVariable Long vendorId, @RequestBody Vendor vendor){
        vendorService.updateVendor(vendorId, vendor);
    }

    @DeleteMapping("/{vendorId}")
    public  void deleteVendor(@PathVariable Long vendorId){
        vendorService.deleteVendor(vendorId);
    }
}
