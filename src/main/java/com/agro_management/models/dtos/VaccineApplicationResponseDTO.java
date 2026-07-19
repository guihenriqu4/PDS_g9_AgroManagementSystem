package com.agro_management.models.dtos;

import com.agro_management.models.VaccineApplication;
import java.time.LocalDate;

public record VaccineApplicationResponseDTO(
        Long id,
        String animalName,
        String vaccineName,
        String userName,
        LocalDate applicationDate,
        LocalDate nextDoseDate,
        String observation
) {
    public VaccineApplicationResponseDTO(VaccineApplication app) {
        this(
            app.getId(), 
            app.getAnimal().getName(),
            app.getVaccine().getName(), 
            app.getUser().getName(),
            app.getApplicationDate(), 
            app.getNextDoseDate(), 
            app.getObservation()
        );
    }
}