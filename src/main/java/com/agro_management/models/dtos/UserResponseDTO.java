package com.agro_management.models.dtos;

import com.agro_management.models.User;

public record UserResponseDTO(Long id, String name, String email, String role) {
    // Construtor para converter a Entidade no DTO facilmente
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}