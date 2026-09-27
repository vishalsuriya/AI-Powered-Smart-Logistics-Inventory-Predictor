package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.product.ProductRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.product.ProductResponseDTO;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long productId);

    ProductResponseDTO getBySku(String sku);

    ProductResponseDTO getByProductCode(String productCode);

    ProductResponseDTO getByBarcode(String barcode);

    List<ProductResponseDTO> getByStatus(ProductStatus status);

    List<ProductResponseDTO> getByVendorId(Long vendorId);

    List<ProductResponseDTO> getByCategory(String category);

    List<ProductResponseDTO> getByBrand(String brand);

    List<ProductResponseDTO> getByVendorIdAndStatus(Long vendorId, ProductStatus status);

    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

    void updateProduct(Long productId, ProductRequestDTO product);

    void activateProduct(Long productId);

    void deactivateProduct(Long productId);

    void updatePurchasePrice(Long productId, BigDecimal purchasePrice);

    boolean existsById(Long productId);

    boolean existsBySku(String sku);

    boolean existsByProductCode(String productCode);

    boolean existsByBarcode(String barcode);
}
