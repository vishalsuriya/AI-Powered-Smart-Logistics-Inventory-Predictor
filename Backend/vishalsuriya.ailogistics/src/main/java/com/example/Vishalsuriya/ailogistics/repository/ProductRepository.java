package com.example.Vishalsuriya.ailogistics.repository;


import com.example.Vishalsuriya.ailogistics.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {
}
