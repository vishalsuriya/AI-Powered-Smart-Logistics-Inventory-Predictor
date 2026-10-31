package com.example.Vishalsuriya.ailogistics.dto.warehouse;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRequestDTO {

    @NotBlank
    private String warehouseName;
    private String warehouseManager;

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
}
