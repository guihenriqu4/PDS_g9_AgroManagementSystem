package com.agro_management.models.dtos;

import java.time.LocalDate;

public record VaccineApplicationRequestDTO(
        Long animalId,
        Long vaccineId,
        Long userId,
        LocalDate applicationDate,
        LocalDate nextDoseDate,
        String observation
) {
}