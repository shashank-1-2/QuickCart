package com.quickcart.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass 
@Getter 
@Setter 
public class BaseEntity {
    @Column (nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist 
    void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PrePersist 
    void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
