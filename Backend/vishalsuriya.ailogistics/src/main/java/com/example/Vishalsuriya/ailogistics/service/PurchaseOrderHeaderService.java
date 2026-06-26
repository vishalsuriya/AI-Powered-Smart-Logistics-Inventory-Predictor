package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;

import java.util.List;

public interface PurchaseOrderHeaderService {

    public List<PurchaseOrderHeader> getAllPurchaseOrderHeaders();

    public PurchaseOrderHeader getPurchaseOrderHeaderById(Long id);

    public PurchaseOrderHeader getPurchaseOrderHeaderByTrxNumber(String trxNumber);

    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByVendorId(Long vendorId);

    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByWarehouseId(Long warehouseId);

    public List<PurchaseOrderHeader> getPurchaseOrderHeadersByStatus(
            PurchaseOrderStatus status
    );

    public String generateTrxNumber();

   public void addPurchaseOrderHeader(PurchaseOrderHeader purchaseOrder);

    public void updatePurchaseOrderHeader(
            Long id,
            PurchaseOrderHeader purchaseOrder
    );

    public void deletePurchaseOrderHeader(Long id);
}
