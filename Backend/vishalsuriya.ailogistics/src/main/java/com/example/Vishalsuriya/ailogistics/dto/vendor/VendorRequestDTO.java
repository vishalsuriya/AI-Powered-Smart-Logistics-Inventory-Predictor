package com.example.Vishalsuriya.ailogistics.dto.vendor;

import com.example.Vishalsuriya.ailogistics.model.CurrencyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorRequestDTO {

    @NotBlank
    private String companyName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String taxNumber;

    @NotBlank
    private String contactNumber;

    @NotNull
    private CurrencyType currencyType;
    private String contactPerson;

    @NotBlank
    private String paymentTerms;

    @NotBlank
    private String address;
    private String city;
    private String state;
    private String country;
}
