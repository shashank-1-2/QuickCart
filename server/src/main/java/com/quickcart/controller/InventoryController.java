package com.quickcart.controller;

import com.quickcart.dto.request.InventoryRequest;
import com.quickcart.dto.request.StockAdjustRequest;
import com.quickcart.dto.response.InventoryResponse;
import com.quickcart.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // GET /api/inventory?page=0&size=20&sort=stockQuantity,asc
    @GetMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<Page<InventoryResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAll(pageable));
    }

    // GET /api/inventory/5
    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<InventoryResponse> getOne(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }

    // GET /api/inventory/5/availability?quantity=2
    @GetMapping("/{productId}/availability")
    public ResponseEntity<Map<String, Object>> availability(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        boolean ok = inventoryService.isAvailable(productId, quantity);
        return ResponseEntity.ok(Map.of(
                "productId", productId,
                "requested", quantity,
                "available", ok));
    }

    // POST /api/inventory
    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.create(request));
    }

    // PUT /api/inventory/5  (full replace)
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<InventoryResponse> replace(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.replace(productId, request));
    }

    // PATCH /api/inventory/5/stock   body: {"delta": 25} or {"delta": -3}
    @PatchMapping("/{productId}/stock")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<InventoryResponse> adjustStock(
            @PathVariable Long productId,
            @Valid @RequestBody StockAdjustRequest request) {
        return ResponseEntity.ok(inventoryService.adjustStock(productId, request.delta()));
    }

    // POST /api/inventory/5/reserve?quantity=2
    @PostMapping("/{productId}/reserve")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<Map<String, String>> reserveStock(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        InventoryResponse updated = inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(Map.of(
                "message", "Stock reserved successfully",
                "productId", productId.toString(),
                "quantity", String.valueOf(quantity),
                "remaining", String.valueOf(updated.stockQuantity())));
    }

    // POST /api/inventory/5/release?quantity=2
    @PostMapping("/{productId}/release")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<Map<String, String>> releaseStock(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        InventoryResponse updated = inventoryService.releaseStock(productId, quantity);
        return ResponseEntity.ok(Map.of(
                "message", "Stock released successfully",
                "productId", productId.toString(),
                "quantity", String.valueOf(quantity),
                "remaining", String.valueOf(updated.stockQuantity())));
    }

    // DELETE /api/inventory/5
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long productId) {
        inventoryService.delete(productId);
        return ResponseEntity.noContent().build(); // 204
    }
}