package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;
import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.repository.ProductRepository;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepo;

    private final VendorRepository vendorRepo;

    public ProductServiceImpl(ProductRepository productRepo, VendorRepository vendorRepo){
        this.productRepo = productRepo;
        this.vendorRepo = vendorRepo;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    @Override
    public Product getProductById(Long prodId) {
        return productRepo.findById(prodId)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist with ID " + prodId));
    }

    @Override
    public Product getBySku(String sku) {
        return productRepo.findBySku(sku).
                orElseThrow(()-> new EntityNotFoundException("Product does not exists with SKU."+ sku));
    }

    @Override
    public Product getByProductCode(String productCode) {
        return productRepo.findByProductCode(productCode).
                orElseThrow(()-> new EntityNotFoundException("Product does not exists with productCode."+productCode));
    }

    @Override
    public Product getByBarcode(String barcode) {
        return productRepo.findByBarcode(barcode).
                orElseThrow(()-> new EntityNotFoundException("Product does not exists with barcode."+barcode));
    }

    @Override
    public List<Product> getByStatus(ProductStatus status) {
        return productRepo.findByStatus(status);
    }

    @Override
    public List<Product> getByVendorId(Long vendorId) {
        return productRepo.findByVendorId(vendorId);
    }

    @Override
    public List<Product> getByCategory(String category) {
        return productRepo.findByCategory(category);
    }

    @Override
    public List<Product> getByBrand(String brand) {
        return productRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getByVendorIdAndStatus(Long vendorId, ProductStatus status) {
        return productRepo.findByVendorIdAndStatus(vendorId, status);
    }

    @Override
    @Transactional
    public Product createProduct(Product product){
        validateForCreate(product);
        Vendor vendor = vendorRepo.findById(product.getVendor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));

        product.setVendor(vendor);
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(Long prodId, Product product) {
            Product existingProduct = findProduct(prodId);
               validateForUpdate(product,existingProduct);
                existingProduct.setProductName(product.getProductName());
                existingProduct.setPurchasePrice(product.getPurchasePrice());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setCategory(product.getCategory());
                existingProduct.setTaxPercentage(product.getTaxPercentage());
                existingProduct.setUnitOfMeasure(product.getUnitOfMeasure());
                existingProduct.setVendor(product.getVendor());
                existingProduct.setStatus(product.getStatus());
                existingProduct.setBrand(product.getBrand());
                existingProduct.setBarcode(product.getBarcode());
    }

    @Override
    @Transactional
    public void activateProduct(Long productId) {
        Product existingProduct = findProduct(productId);
        if(existingProduct.getStatus() == ProductStatus.ACTIVE){
            throw new IllegalArgumentException("Product already active.");
        }
        existingProduct.setStatus(ProductStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateProduct(Long productId) {
        Product existingProduct = findProduct(productId);
        if(existingProduct.getStatus() == ProductStatus.INACTIVE){
            throw new IllegalArgumentException("Product already InActive.");
        }
        existingProduct.setStatus(ProductStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void updatePurchasePrice(Long productId, BigDecimal purchasePrice) {
        if(purchasePrice.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException(
                    "Purchase price must be greater than zero.");
        }
        Product existingProduct = findProduct(productId);
        existingProduct.setPurchasePrice(purchasePrice);
    }

    @Override
    public boolean existsById(Long productId) {
        return productRepo.existsById(productId);
    }

    @Override
    public boolean existsBySku(String sku) {
        return productRepo.existsBySku(sku);
    }

    @Override
    public boolean existsByProductCode(String productCode) {
        return productRepo.existsByProductCode(productCode);
    }

    @Override
    public boolean existsByBarcode(String barcode) {
        return productRepo.existsByBarcode(barcode);
    }

    private Product findProduct(Long productId){
        return productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID : " + productId));
    }

    private void validateForCreate(Product product){
        if(productRepo.existsBySku(product.getSku())){
            throw new IllegalArgumentException("SKU already exists.");
        }
        if(productRepo.existsByProductCode(product.getProductCode())){
            throw new IllegalArgumentException(("ProductCode already exists."));
        }
        if(product.getBarcode()!= null && productRepo.existsByBarcode(product.getBarcode())){
            throw new IllegalArgumentException("Barcode already exists.");
        }
        if (!vendorRepo.existsById(product.getVendor().getId())) {
            throw new IllegalArgumentException("Vendor does not exist.");
        }
    }
    private void validateForUpdate(Product product, Product existingProduct){
        if (product.getVendor() == null || product.getVendor().getId() == null) {
            throw new IllegalArgumentException("Vendor is required.");
        }
        boolean vendorExists = vendorRepo.existsById(product.getVendor().getId());
        if(!vendorExists) {
            throw new IllegalArgumentException("Vendor does not exists.");
        }
        if (!Objects.equals(existingProduct.getBarcode(), product.getBarcode())
                && product.getBarcode() != null
                && existsByBarcode(product.getBarcode())) {
            throw new IllegalArgumentException("Barcode already exists.");
        }
        if (product.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase price must be greater than zero.");
        }
    }
}
