package com.quickcart.dto.response;

public record AuthResponse(

    String token,
    String tokenType,
    Long useraId,
    String name,
    String email,
    String role
){}