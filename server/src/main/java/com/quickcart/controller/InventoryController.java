package com.quickcart.controller;

import com.quickcart.repository.InventoryRepository;
import com.quickcart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    public ResponseEntity<Map<String , String>> reserveStock(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity
    ){
        inventoryService.reserveStock(productId, quantity);

        return ResponseEntity.ok(Map.of("message", "Stock reserved successfully",
                "productId", productId.toString(),
                "quantity",String.valueOf(quantity)));
    }

}
