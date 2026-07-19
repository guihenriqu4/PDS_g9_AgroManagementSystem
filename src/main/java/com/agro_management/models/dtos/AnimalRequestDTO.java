package com.agro_management.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record AnimalRequestDTO(
        @NotBlank(message = "O brinco é obrigatório") String earring,
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "O sexo é obrigatório") String sex,
        @NotNull(message = "O peso é obrigatório") @Positive Float weight,
        @NotBlank(message = "A raça é obrigatória") String race,
        @NotNull(message = "A data de nascimento é obrigatória") LocalDate bornIn
) {}