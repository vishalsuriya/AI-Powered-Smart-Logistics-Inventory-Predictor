package com.example.Vishalsuriya.ailogistics.controller;

import com.example.Vishalsuriya.ailogistics.model.Product;
import com.example.Vishalsuriya.ailogistics.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{prodId}")
    public Product getProductById(@PathVariable Long prodId){
        return productService.getProductById(prodId);
    }

    @PostMapping
    public void addProduct(@RequestBody Product product){
        productService.addProduct(product);
    }

    @PutMapping("/{prodId}")
    public void updateProduct(@PathVariable Long prodId, @RequestBody Product product) {
         productService.updateProduct(prodId, product);
    }
    @DeleteMapping("/{prodId}")
    public void deleteProduct(@PathVariable Long prodId){
        productService.deleteProduct(prodId);
    }
}
