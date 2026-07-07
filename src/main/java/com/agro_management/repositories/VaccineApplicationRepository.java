package com.agro_management.repositories;

import com.agro_management.models.VaccineApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VaccineApplicationRepository extends JpaRepository<VaccineApplication, Long> {
    List<VaccineApplication> findByAnimalId(Long animalId);
}