package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepo;

    public ProductServiceImpl(ProductRepository productRepo){
        this.productRepo = productRepo;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
    @Override
    public Product getProductById(Long prodId) {
        return productRepo.findById(prodId).orElse(new Product());
    }
    @Override
    public void addProduct(Product product){
        productRepo.save(product);
    }
    @Override
    public void updateProduct(Long prodId, Product product) {
        Product existingProduct = productRepo.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product ID " + prodId + " does not exist."));
        existingProduct.setProductName(product.getProductName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        productRepo.save(existingProduct);
    }
    @Override
    public void deleteProduct(Long prodId){
        productRepo.deleteById(prodId);
    }
}
