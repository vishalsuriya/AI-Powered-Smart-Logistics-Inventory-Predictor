package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderLine;

import java.util.List;

public interface PurchaseOrderLineService {

    public List<PurchaseOrderLine> getAllPurchaseOrderLines();

    public PurchaseOrderLine getPurchaseOrderLineById(Long id);

    public PurchaseOrderLine getByPurchaseOrderHeaderIdAndProductId(Long purchaseOrderHeaderId, Long productId);

    public List<PurchaseOrderLine> getByProductId(Long productId);

    public List<PurchaseOrderLine> getByPurchaseOrderHeaderId(Long purchaseOrderHeaderId);

    public void addPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine);

    public void updatePurchaseOrderLine(Long id, PurchaseOrderLine purchaseOrderLine);

    public void deletePurchaseOrderLine(Long id);

    public boolean existsByPurchaseOrderHeaderIdAndProductId(Long purchaseOrderHeaderId, Long productId);
}
