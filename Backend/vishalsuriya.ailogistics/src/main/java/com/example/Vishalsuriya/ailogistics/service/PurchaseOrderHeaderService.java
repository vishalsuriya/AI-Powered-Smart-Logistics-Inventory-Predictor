package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseOrderStatus;

import java.util.List;

public interface PurchaseOrderHeaderService {

    List<PurchaseOrderHeader> getAllPurchaseOrderHeaders();

    PurchaseOrderHeader getPurchaseOrderHeaderById(Long id);

    PurchaseOrderHeader getPurchaseOrderHeaderByTrxNumber(String trxNumber);

    List<PurchaseOrderHeader> getPurchaseOrderHeadersByVendorId(Long vendorId);

    List<PurchaseOrderHeader> getPurchaseOrderHeadersByWarehouseId(Long warehouseId);

    List<PurchaseOrderHeader> getPurchaseOrderHeadersByStatus(
            PurchaseOrderStatus status
    );

    String generateTrxNumber();

    void addPurchaseOrderHeader(PurchaseOrderHeader purchaseOrder);

    void updatePurchaseOrderHeader(
            Long id,
            PurchaseOrderHeader purchaseOrder
    );

    void approvePurchaseOrder(Long id);

    void cancelPurchaseOrder(Long id);

    void deletePurchaseOrderHeader(Long id);
}