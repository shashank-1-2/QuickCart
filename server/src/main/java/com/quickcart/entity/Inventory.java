package com.quickcart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer stockQuantity;

    protected Inventory() {

    }

    public Inventory(Long productId, String productName, Integer stockQuantity){
        this.productId = productId;
        this.productName = productName;
        this.stockQuantity = stockQuantity;
    }


}
