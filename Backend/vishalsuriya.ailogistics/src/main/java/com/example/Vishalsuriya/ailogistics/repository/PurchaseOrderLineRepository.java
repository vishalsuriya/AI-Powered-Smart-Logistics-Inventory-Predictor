package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine,Long> {

    Optional<PurchaseOrderLine> findByPurchaseOrderHeaderIdAndProductId(
            Long purchaseOrderHeaderId,
            Long productId
    );

    List<PurchaseOrderLine> findByPurchaseOrderHeaderId(
            Long purchaseOrderHeaderId
    );

    List<PurchaseOrderLine> findByProductId(
            Long productId
    );

    boolean existsByPurchaseOrderHeaderIdAndProductId(
            Long purchaseOrderHeaderId,
            Long productId
    );
}
