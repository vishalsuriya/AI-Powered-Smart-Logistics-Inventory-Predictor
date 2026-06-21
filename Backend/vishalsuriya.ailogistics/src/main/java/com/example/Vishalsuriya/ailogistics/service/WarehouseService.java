package com.example.Vishalsuriya.ailogistics.service;


import com.example.Vishalsuriya.ailogistics.model.Warehouse;

import java.util.List;

public interface WarehouseService {

    public List<Warehouse> getAllWarehouses();

    public Warehouse getWarehouseById(Long Id);

    public Warehouse getWarehouseByWarehouseCode(String warehouseCode);

   public String generateWarehouseCode();

   public void addWarehouse(Warehouse warehouse);

    public void updateWarehouse(Long warehouseId, Warehouse warehouse);

   public void deleteWarehouse(Long Id);
}
