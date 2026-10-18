package com.quickcart.service;

import com.quickcart.entity.Inventory;
import com.quickcart.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void reserveStock(Long productId , int quantity){

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                                                 .orElseThrow(() -> new IllegalArgumentException("Product not found in inventory : " + productId));

        if(inventory.getStockQuantity() < quantity){
            throw new IllegalArgumentException("Insufficient stock for product : " + inventory.getProductName());
        }
    }
}
