package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product getBySku(String sku);

    Product getByProductCode(String productCode);

    Product getByBarcode(String barcode);

    List<Product> getByStatus(ProductStatus status);

    List<Product> getByVendorId(Long vendorId);

    List<Product> getByCategory(String category);

    List<Product> getByBrand(String brand);

    List<Product> getByVendorIdAndStatus(Long vendorId, ProductStatus status);

    Product createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void activateProduct(Long productId);

    void deactivateProduct(Long productId);

    void updatePurchasePrice(Long productId, BigDecimal purchasePrice);

    boolean existsById(Long productId);

    boolean existsBySku(String sku);

    boolean existsByProductCode(String productCode);

    boolean existsByBarcode(String barcode);
}
