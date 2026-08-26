package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseName(String warehouseName);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
