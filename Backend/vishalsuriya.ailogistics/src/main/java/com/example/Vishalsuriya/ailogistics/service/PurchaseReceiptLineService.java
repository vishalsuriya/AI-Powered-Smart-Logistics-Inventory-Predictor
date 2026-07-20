package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptLine;

import java.util.List;

public interface PurchaseReceiptLineService {

    public List<PurchaseReceiptLine> getAllPurchaseReceiptLines();

    public PurchaseReceiptLine getPurchaseReceiptLineById(Long id);

    public List<PurchaseReceiptLine> getByProductId(Long productId);

    public List<PurchaseReceiptLine> getByPurchaseOrderLineId(Long purchaseOrderLineId);

    List<PurchaseReceiptLine> getByPurchaseReceiptHeaderId(Long purchaseReceiptHeaderId);

    public PurchaseReceiptLine getByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(Long purchaseReceiptHeaderId, Long purchaseOrderLineId);

    public void addPurchaseReceiptLine(PurchaseReceiptLine purchaseReceiptLine);

    public void updatePurchaseReceiptLine(Long id , PurchaseReceiptLine purchaseReceiptLine);

    public void deletePurchaseReceiptLine(Long id);

    public boolean existsByPurchaseReceiptHeaderIdAndPurchaseOrderLineId(
            Long purchaseReceiptHeaderId, Long purchaseOrderLineId);
}
