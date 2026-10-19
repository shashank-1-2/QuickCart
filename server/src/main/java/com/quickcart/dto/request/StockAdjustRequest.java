package com.quickcart.dto.request;

import jakarta.validation.constraints.NotNull;

// delta > 0 adds stock (new delivery), delta < 0 removes it (damaged, lost)
public record StockAdjustRequest(@NotNull Integer delta) {}