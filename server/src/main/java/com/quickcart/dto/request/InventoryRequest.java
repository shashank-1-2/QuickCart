package com.quickcart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryRequest(
        @NotNull @Positive Long productId,
        @NotBlank String productName,
        @NotNull @PositiveOrZero Integer stockQuantity
) {}