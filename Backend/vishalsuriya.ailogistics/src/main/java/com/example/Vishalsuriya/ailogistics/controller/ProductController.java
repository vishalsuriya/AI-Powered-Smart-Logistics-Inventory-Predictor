package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;
import com.example.Vishalsuriya.ailogistics.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long prodId) {
        return ResponseEntity.ok(productService.getProductById(prodId));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getBySku(sku));
    }

    @GetMapping("/product-code/{productCode}")
    public ResponseEntity<Product> getByProductCode(@PathVariable String productCode) {
        return ResponseEntity.ok(productService.getByProductCode(productCode));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productService.getByBarcode(barcode));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Product>> getByStatus(@PathVariable ProductStatus status) {
        return ResponseEntity.ok(productService.getByStatus(status));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Product>> getByVendorId(@PathVariable Long vendorId) {
        return ResponseEntity.ok(productService.getByVendorId(vendorId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getByCategory(category));
    }

    @GetMapping("/vendor/{vendorId}/status/{status}")
    public ResponseEntity<List<Product>> getByVendorIdAndStatus(@PathVariable Long vendorId, @PathVariable ProductStatus status) {
        return ResponseEntity.ok(productService.getByVendorIdAndStatus(vendorId, status));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(productService.getByBrand(brand));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{prodId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long prodId, @RequestBody @Valid Product product) {
        productService.updateProduct(prodId, product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable Long productId) {
        productService.activateProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/purchase-price")
    public ResponseEntity<Void> updatePurchasePrice(@PathVariable Long productId, @RequestParam BigDecimal purchasePrice) {
        productService.updatePurchasePrice(productId, purchasePrice);
        return ResponseEntity.ok().build();
    }
}