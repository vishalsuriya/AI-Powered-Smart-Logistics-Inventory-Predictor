package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseReceiptHeaderRepository extends JpaRepository<PurchaseReceiptHeader, Long> {

    Optional<PurchaseReceiptHeader> findByTrxNumber(String trxNumber);

    List<PurchaseReceiptHeader> findByPurchaseOrderHeaderId(Long purchaseOrderHeaderId);

    List<PurchaseReceiptHeader> findByVendorId(Long vendorId);

    List<PurchaseReceiptHeader> findByWarehouseId(Long warehouseId);

    List<PurchaseReceiptHeader> findByStatus(PurchaseReceiptStatus status);

    List<PurchaseReceiptHeader> findByReceivedBy(String receivedBy);

    boolean existsByTrxNumber(String trxNumber);

}
