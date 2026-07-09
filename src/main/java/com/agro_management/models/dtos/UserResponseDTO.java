package com.agro_management.models.dtos;

import com.agro_management.models.User;

public record UserResponseDTO(Long id, String name, String email, String role, String status) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getStatus() != null ? user.getStatus() : "ATIVO");
    }
}