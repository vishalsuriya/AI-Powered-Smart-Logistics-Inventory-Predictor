package com.example.Vishalsuriya.ailogistics.dto.vendor;

import com.example.Vishalsuriya.ailogistics.model.CurrencyType;
import com.example.Vishalsuriya.ailogistics.model.VendorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponseDTO {

    private String vendorCode;
    private String companyName;
    private String email;
    private String taxNumber;
    private String contactNumber;
    private CurrencyType currencyType;
    private VendorStatus vendorStatus;
    private String contactPerson;
    private String paymentTerms;
    private String address;
    private String city;
    private String state;
    private String country;
}
