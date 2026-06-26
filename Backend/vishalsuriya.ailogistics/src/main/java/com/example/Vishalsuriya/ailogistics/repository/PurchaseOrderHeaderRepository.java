package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderHeaderRepository extends JpaRepository<PurchaseOrderHeader,Long> {
    Optional<PurchaseOrderHeader> findByTrxNumber(String trxNumber);

    List<PurchaseOrderHeader> findByVendorId(Long vendorId);

    List<PurchaseOrderHeader> findByWarehouseId(Long warehouseId);

    List<PurchaseOrderHeader> findByStatus(PurchaseOrderStatus status);

    boolean existsByTrxNumber(String trxNumber);

}
