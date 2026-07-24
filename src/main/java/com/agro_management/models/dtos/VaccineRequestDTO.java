package com.agro_management.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VaccineRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "O fabricante é obrigatório") String manufacturer,
        @NotNull(message = "O estoque é obrigatório") @PositiveOrZero Integer stockQuantity,
        @NotNull(message = "O preço é obrigatório") @PositiveOrZero Double price // Nova propriedade adicionada
) {}