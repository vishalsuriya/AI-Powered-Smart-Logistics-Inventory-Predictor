package com.example.Vishalsuriya.ailogistics.dto.warehouse;

import com.example.Vishalsuriya.ailogistics.model.WarehouseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponseDTO {
    private String warehouseCode;
    private String warehouseName;
    private String warehouseManager;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private Integer capacity;
    private WarehouseStatus warehouseStatus;
}
