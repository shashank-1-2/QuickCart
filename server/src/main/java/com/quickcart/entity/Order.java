package com.quickcart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    private String idempotencyKey;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
