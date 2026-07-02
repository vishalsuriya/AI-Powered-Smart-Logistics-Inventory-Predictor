package com.example.Vishalsuriya.ailogistics.controller;


import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptHeader;
import com.example.Vishalsuriya.ailogistics.model.PurchaseReceiptStatus;
import com.example.Vishalsuriya.ailogistics.service.PurchaseReceiptHeaderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-receipts")
public class PurchaseReceiptHeaderController {

    private final PurchaseReceiptHeaderService purchaseReceiptHeaderService;

    public PurchaseReceiptHeaderController(PurchaseReceiptHeaderService purchaseReceiptHeaderService) {
        this.purchaseReceiptHeaderService = purchaseReceiptHeaderService;
    }

    public List<PurchaseReceiptHeader> getAllPurchaseReceiptHeaders(){
        return purchaseReceiptHeaderService.getAllPurchaseReceiptHeaders();
    }

    public PurchaseReceiptHeader getPurchaseReceiptHeaderById(Long id){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeaderById(id);
    }

    public PurchaseReceiptHeader getPurchaseReceiptHeaderByTrxNumber(String trxNumber){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeaderByTrxNumber(trxNumber);
    }

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByPurchaseOrderHeaderId(Long purchaseOrderHeaderId){
       return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByPurchaseOrderHeaderId(purchaseOrderHeaderId);
    }

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByVendorId(Long vendorId){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByVendorId(vendorId);
    }

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByWarehouseId(Long warehouseId){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByWarehouseId(warehouseId);
    }

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByStatus(PurchaseReceiptStatus status){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByStatus(status);
    }

    public List<PurchaseReceiptHeader> getPurchaseReceiptHeadersByReceivedBy(String receivedBy){
        return purchaseReceiptHeaderService.getPurchaseReceiptHeadersByReceivedBy(receivedBy);
    }

    public boolean existsByTrxNumber(String trxNumber){
        return purchaseReceiptHeaderService.existsByTrxNumber(trxNumber);
    }

    public void addPurchaseReceiptHeader(PurchaseReceiptHeader purchaseReceiptHeader){
         purchaseReceiptHeaderService.addPurchaseReceiptHeader(purchaseReceiptHeader);
    }

    public void updatePurchaseReceiptHeader(Long id, PurchaseReceiptHeader purchaseReceiptHeader){
        purchaseReceiptHeaderService.updatePurchaseReceiptHeader(id, purchaseReceiptHeader);
    }

    public void deletePurchaseReceiptHeader(Long id){
        purchaseReceiptHeaderService.deletePurchaseReceiptHeader(id);
    }
}
