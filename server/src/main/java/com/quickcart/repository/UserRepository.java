package com.quickcart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickcart.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail (String email);
    boolean existsByEmail (String email);
}
