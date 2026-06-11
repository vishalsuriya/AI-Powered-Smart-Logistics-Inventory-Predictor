package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.Product;
import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();

    public Product getProductById(Long prodId);

    public void addProduct(Product product);

    public void updateProduct(Long prodId, Product product);

    public void deleteProduct(Long prodId);

}
