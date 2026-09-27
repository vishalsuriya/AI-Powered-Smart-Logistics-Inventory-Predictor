package com.example.Vishalsuriya.ailogistics.dto.product;

import com.example.Vishalsuriya.ailogistics.model.ProductStatus;
import com.example.Vishalsuriya.ailogistics.model.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private String productName;
    private String description;
    private String sku;
    private String category;
    private String brand;
    private BigDecimal purchasePrice;
    private Long vendorId;
    private String productCode;
    private ProductStatus status;
    private UnitOfMeasure unitOfMeasure;
    private BigDecimal taxPercentage;
    private String barcode;
}
