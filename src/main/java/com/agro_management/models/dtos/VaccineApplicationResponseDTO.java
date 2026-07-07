package com.agro_management.models.dtos;

import com.agro_management.models.VaccineApplication;
import java.time.LocalDate;

public record VaccineApplicationResponseDTO(
        Long id,
        String vaccineName,
        String userName,
        LocalDate applicationDate,
        LocalDate nextDoseDate,
        String observation
) {
    public VaccineApplicationResponseDTO(VaccineApplication app) {
        this(
            app.getId(), 
            app.getVaccine().getName(), 
            app.getUser().getName(),
            app.getApplicationDate(), 
            app.getNextDoseDate(), 
            app.getObservation()
        );
    }
}