package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.dto.product.ProductRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.product.ProductResponseDTO;
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

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable final Long prodId) {
        return ResponseEntity.ok(productService.getProductById(prodId));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponseDTO> getBySku(@PathVariable final String sku) {
        return ResponseEntity.ok(productService.getBySku(sku));
    }

    @GetMapping("/product-code/{productCode}")
    public ResponseEntity<ProductResponseDTO> getByProductCode(@PathVariable final String productCode) {
        return ResponseEntity.ok(productService.getByProductCode(productCode));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductResponseDTO> getByBarcode(@PathVariable final String barcode) {
        return ResponseEntity.ok(productService.getByBarcode(barcode));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponseDTO>> getByStatus(@PathVariable final ProductStatus status) {
        return ResponseEntity.ok(productService.getByStatus(status));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ProductResponseDTO>> getByVendorId(@PathVariable final Long vendorId) {
        return ResponseEntity.ok(productService.getByVendorId(vendorId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getByCategory(@PathVariable final String category) {
        return ResponseEntity.ok(productService.getByCategory(category));
    }

    @GetMapping("/vendor/{vendorId}/status/{status}")
    public ResponseEntity<List<ProductResponseDTO>> getByVendorIdAndStatus(@PathVariable final Long vendorId, @PathVariable final ProductStatus status) {
        return ResponseEntity.ok(productService.getByVendorIdAndStatus(vendorId, status));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductResponseDTO>> getByBrand(@PathVariable final String brand) {
        return ResponseEntity.ok(productService.getByBrand(brand));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid final ProductRequestDTO requestDTO) {
        final ProductResponseDTO responseDTO = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{prodId}")
    public ResponseEntity<Void> updateProduct(@PathVariable final Long prodId, @RequestBody @Valid final ProductRequestDTO requestDTO) {
        productService.updateProduct(prodId, requestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable final Long productId) {
        productService.activateProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable final Long productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/purchase-price")
    public ResponseEntity<Void> updatePurchasePrice(@PathVariable final Long productId, @RequestParam final BigDecimal purchasePrice) {
        productService.updatePurchasePrice(productId, purchasePrice);
        return ResponseEntity.ok().build();
    }
}