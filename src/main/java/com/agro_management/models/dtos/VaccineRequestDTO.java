package com.agro_management.models.dtos;

import jakarta.validation.constraints.NotBlank;

public record VaccineRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "O fabricante é obrigatório") String manufacturer
) {}