package com.example.Vishalsuriya.ailogistics.repository;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseReceiptLineRepository extends JpaRepository<PurchaseReceiptLine,Long> {

    Optional<PurchaseReceiptLine>findByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            Long purchaseReceiptHeaderId, Long purchaseOrderLineId);

    List<PurchaseReceiptLine> findByPurchaseReceiptHeaderIdOrderByIdAsc(Long purchaseReceiptHeaderId);

    List<PurchaseReceiptLine> findByProductId(Long productId);

    boolean existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            Long purchaseReceiptHeaderId, Long purchaseOrderLineId);

    List<PurchaseReceiptLine> findByPurchaseOrderLineId(Long purchaseOrderLineId);

}
