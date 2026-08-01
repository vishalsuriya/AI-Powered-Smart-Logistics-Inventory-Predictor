package com.example.Vishalsuriya.ailogistics.repository;


import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {

    boolean existsBySku(String sku);

    boolean existsByProductCode(String productCode);

    boolean existsByBarcode(String barcode);

    Optional<Product> findBySku(String sku);

    Optional<Product> findByProductCode(String productCode);

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByVendorId(Long vendorId);

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByVendorIdAndStatus(Long vendorId, ProductStatus status);
}
