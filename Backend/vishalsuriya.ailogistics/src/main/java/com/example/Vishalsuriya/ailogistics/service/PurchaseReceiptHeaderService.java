package com.example.Vishalsuriya.ailogistics.service;

import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;

import java.util.List;

public interface PurchaseReceiptHeaderService {

    public String generateTrxNumber();

    public List<PurchaseReceiptHeader> getAllPurchaseReceiptHeaders();

    public PurchaseReceiptHeader getPurchaseReceiptHeaderById(Long id);

    public PurchaseReceiptHeader getPurchaseReceiptHeaderByTrxNumber(String trxNumber);

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByPurchaseOrderHeaderId(Long purchaseOrderHeaderId);

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByVendorId(Long vendorId);

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByWarehouseId(Long warehouseId);

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByStatus(PurchaseReceiptStatus status);

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByReceivedBy(String receivedBy);

    public boolean existsByTrxNumber(String trxNumber);

    public void addPurchaseReceiptHeader(PurchaseReceiptHeader purchaseReceiptHeader);

    public void updatePurchaseReceiptHeader(Long id, PurchaseReceiptHeader purchaseReceiptHeader);

    public void deletePurchaseReceiptHeader(Long id);
}