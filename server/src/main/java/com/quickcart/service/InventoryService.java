package com.quickcart.service;

import com.quickcart.dto.request.InventoryRequest;
import com.quickcart.dto.response.InventoryResponse;
import com.quickcart.entity.Inventory;
import com.quickcart.exception.DuplicateResourceException;
import com.quickcart.exception.InsufficientStockException;
import com.quickcart.exception.ResourceNotFoundException;
import com.quickcart.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    // ---------- READ ----------

    @Transactional(readOnly = true)
    public Page<InventoryResponse> getAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable).map(InventoryResponse::from);
    }

    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        return InventoryResponse.from(find(productId));
    }

    @Transactional(readOnly = true)
    public boolean isAvailable(Long productId, int quantity) {
        requirePositive(quantity);
        return find(productId).getStockQuantity() >= quantity;
    }

    // ---------- CREATE ----------

    @Transactional
    public InventoryResponse create(InventoryRequest req) {
        if (inventoryRepository.existsByProductId(req.productId())) {
            throw new DuplicateResourceException("Inventory already exists for product " + req.productId());
        }
        Inventory saved = inventoryRepository.save(
                new Inventory(req.productId(), req.productName().trim(), req.stockQuantity()));
        return InventoryResponse.from(saved);
    }

    // ---------- UPDATE ----------

    @Transactional
    public InventoryResponse replace(Long productId, InventoryRequest req) {
        Inventory inv = lock(productId);
        inv.setProductName(req.productName().trim());
        inv.setStockQuantity(req.stockQuantity());
        return InventoryResponse.from(inv); // saved automatically when the transaction commits
    }

    @Transactional
    public InventoryResponse adjustStock(Long productId, int delta) {
        Inventory inv = lock(productId);
        int updated = inv.getStockQuantity() + delta;
        if (updated < 0) {
            throw new InsufficientStockException(
                    "Stock cannot go below zero for product: " + inv.getProductName());
        }
        inv.setStockQuantity(updated);
        return InventoryResponse.from(inv);
    }

    // ---------- ORDER FLOW ----------

    @Transactional
    public InventoryResponse reserveStock(Long productId, int quantity) {
        requirePositive(quantity);
        Inventory inv = lock(productId); // row lock: two buyers cannot take the last item
        if (inv.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + inv.getProductName());
        }
        inv.setStockQuantity(inv.getStockQuantity() - quantity); // the missing subtraction
        return InventoryResponse.from(inv);
    }

    @Transactional
    public InventoryResponse releaseStock(Long productId, int quantity) {
        requirePositive(quantity);
        Inventory inv = lock(productId);
        inv.setStockQuantity(inv.getStockQuantity() + quantity);
        return InventoryResponse.from(inv);
    }

    // ---------- DELETE ----------

    @Transactional
    public void delete(Long productId) {
        inventoryRepository.delete(find(productId));
    }

    // ---------- helpers ----------

    private Inventory find(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product " + productId));
    }

    private Inventory lock(Long productId) {
        return inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product " + productId));
    }

    private void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}