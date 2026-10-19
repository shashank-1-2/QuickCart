package com.quickcart.dto.response;

import com.quickcart.entity.Inventory;

public record InventoryResponse(Long id, Long productId, String productName, Integer stockQuantity) {

    public static InventoryResponse from(Inventory i) {
        return new InventoryResponse(i.getId(), i.getProductId(), i.getProductName(), i.getStockQuantity());
    }
}