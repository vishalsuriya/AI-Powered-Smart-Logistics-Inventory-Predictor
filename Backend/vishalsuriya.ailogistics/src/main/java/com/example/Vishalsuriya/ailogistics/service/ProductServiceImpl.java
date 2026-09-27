package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.dto.product.ProductRequestDTO;
import com.example.Vishalsuriya.ailogistics.dto.product.ProductResponseDTO;
import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.model.ProductStatus;
import com.example.Vishalsuriya.ailogistics.model.Vendor;
import com.example.Vishalsuriya.ailogistics.repository.ProductRepository;
import com.example.Vishalsuriya.ailogistics.repository.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepo;

    private final VendorRepository vendorRepo;

    public ProductServiceImpl(final ProductRepository productRepo, final VendorRepository vendorRepo) {
        this.productRepo = productRepo;
        this.vendorRepo = vendorRepo;
    }

    private ProductResponseDTO mapToResponseDTO(final Product product) {
        final ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setPurchasePrice(product.getPurchasePrice());
        dto.setProductCode(product.getProductCode());
        dto.setStatus(product.getStatus());
        dto.setUnitOfMeasure(product.getUnitOfMeasure());
        dto.setTaxPercentage(product.getTaxPercentage());
        dto.setBarcode(product.getBarcode());
        dto.setVendorId(product.getVendor().getId());
        return dto;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        final List<Product> products = productRepo.findAll();
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }
    @Override
    public ProductResponseDTO getProductById(final Long prodId) {
        final Product product = productRepo.findById(prodId)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist with ID " + prodId));
        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO getBySku(final String sku) {
        final Product product = productRepo.findBySku(sku).
                orElseThrow(()-> new EntityNotFoundException("Product does not exists with SKU."+ sku));
        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO getByProductCode(final String productCode) {
        final Product product = productRepo.findByProductCode(productCode).
                orElseThrow(()-> new EntityNotFoundException("Product does not exists with productCode."+productCode));
        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO getByBarcode(final String barcode) {
        final Product product = productRepo.findByBarcode(barcode).
                orElseThrow(() -> new EntityNotFoundException("Product does not exists with barcode." + barcode));
        return mapToResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getByStatus(final ProductStatus status) {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        final List<Product> products = productRepo.findByStatus(status);
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }

    @Override
    public List<ProductResponseDTO> getByVendorId(final Long vendorId) {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        final List<Product> products = productRepo.findByVendorId(vendorId);
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }

    @Override
    public List<ProductResponseDTO> getByCategory(final String category) {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        List<Product> products = productRepo.findByCategory(category);
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }

    @Override
    public List<ProductResponseDTO> getByBrand(final String brand) {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        List<Product> products = productRepo.findByBrand(brand);
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }

    @Override
    public List<ProductResponseDTO> getByVendorIdAndStatus(final Long vendorId, final ProductStatus status) {
        final List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        List<Product> products = productRepo.findByVendorIdAndStatus(vendorId, status);
        for (final Product product : products) {
            productResponseDTOList.add(mapToResponseDTO(product));
        }
        return productResponseDTOList;
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(final ProductRequestDTO requestDTO) {
        validateForCreate(requestDTO);
        final Vendor vendor = vendorRepo.findById(requestDTO.getVendorId())
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));
        final Product product = new Product();
        product.setProductName(requestDTO.getProductName());
        product.setProductCode(requestDTO.getProductCode());
        product.setSku(requestDTO.getSku());
        product.setStatus(requestDTO.getStatus());
        product.setUnitOfMeasure(requestDTO.getUnitOfMeasure());
        product.setTaxPercentage(requestDTO.getTaxPercentage());
        product.setPurchasePrice(requestDTO.getPurchasePrice());
        product.setCategory(requestDTO.getCategory());
        product.setBarcode(requestDTO.getBarcode());
        product.setDescription(requestDTO.getDescription());
        product.setBrand(requestDTO.getBrand());
        product.setVendor(vendor);
        final Product savedProduct = productRepo.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Override
    @Transactional
    public void updateProduct(final Long prodId, final ProductRequestDTO requestDTO) {
        final Product existingProduct = findProduct(prodId);
        validateForUpdate(requestDTO, existingProduct);
        final Vendor vendor = vendorRepo.findById(requestDTO.getVendorId()).
                orElseThrow(() -> new EntityNotFoundException("Vendor does not exists with ID." + requestDTO.getVendorId()));
        existingProduct.setProductName(requestDTO.getProductName());
        existingProduct.setPurchasePrice(requestDTO.getPurchasePrice());
        existingProduct.setDescription(requestDTO.getDescription());
        existingProduct.setCategory(requestDTO.getCategory());
        existingProduct.setTaxPercentage(requestDTO.getTaxPercentage());
        existingProduct.setUnitOfMeasure(requestDTO.getUnitOfMeasure());
        existingProduct.setVendor(vendor);
        existingProduct.setStatus(requestDTO.getStatus());
        existingProduct.setBrand(requestDTO.getBrand());
        existingProduct.setBarcode(requestDTO.getBarcode());
    }

    @Override
    @Transactional
    public void activateProduct(final Long productId) {
        final Product existingProduct = findProduct(productId);
        if(existingProduct.getStatus() == ProductStatus.ACTIVE){
            throw new IllegalArgumentException("Product already active.");
        }
        existingProduct.setStatus(ProductStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateProduct(final Long productId) {
        final Product existingProduct = findProduct(productId);
        if(existingProduct.getStatus() == ProductStatus.INACTIVE){
            throw new IllegalArgumentException("Product already InActive.");
        }
        existingProduct.setStatus(ProductStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void updatePurchasePrice(final Long productId, final BigDecimal purchasePrice) {
        if(purchasePrice.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException(
                    "Purchase price must be greater than zero.");
        }
        final Product existingProduct = findProduct(productId);
        existingProduct.setPurchasePrice(purchasePrice);
    }

    @Override
    public boolean existsById(final Long productId) {
        return productRepo.existsById(productId);
    }

    @Override
    public boolean existsBySku(final String sku) {
        return productRepo.existsBySku(sku);
    }

    @Override
    public boolean existsByProductCode(final String productCode) {
        return productRepo.existsByProductCode(productCode);
    }

    @Override
    public boolean existsByBarcode(final String barcode) {
        return productRepo.existsByBarcode(barcode);
    }

    private Product findProduct(final Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID : " + productId));
    }

    private void validateForCreate(final ProductRequestDTO product) {
        if(productRepo.existsBySku(product.getSku())){
            throw new IllegalArgumentException("SKU already exists.");
        }
        if(productRepo.existsByProductCode(product.getProductCode())){
            throw new IllegalArgumentException(("ProductCode already exists."));
        }
        if(product.getBarcode()!= null && productRepo.existsByBarcode(product.getBarcode())){
            throw new IllegalArgumentException("Barcode already exists.");
        }
        if (!vendorRepo.existsById(product.getVendorId())) {
            throw new IllegalArgumentException("Vendor does not exist.");
        }
    }

    private void validateForUpdate(final ProductRequestDTO requestDTO, final Product existingProduct) {
        if (requestDTO.getVendorId() == null) {
            throw new IllegalArgumentException("Vendor is required.");
        }
        final boolean vendorExists = vendorRepo.existsById(requestDTO.getVendorId());
        if(!vendorExists) {
            throw new IllegalArgumentException("Vendor does not exists.");
        }
        if (!Objects.equals(existingProduct.getBarcode(), requestDTO.getBarcode())
                && requestDTO.getBarcode() != null
                && existsByBarcode(requestDTO.getBarcode())) {
            throw new IllegalArgumentException("Barcode already exists.");
        }
        if (requestDTO.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase price must be greater than zero.");
        }
    }
}
