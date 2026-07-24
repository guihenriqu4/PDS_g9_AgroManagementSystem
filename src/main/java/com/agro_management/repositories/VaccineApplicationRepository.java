package com.agro_management.repositories;

import com.agro_management.models.VaccineApplication;
import com.agro_management.models.dtos.MonthlyVaccinationReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VaccineApplicationRepository extends JpaRepository<VaccineApplication, Long> {
    List<VaccineApplication> findByAnimalId(Long animalId);

    @Query(value = "SELECT CAST(EXTRACT(YEAR FROM application_date) AS INTEGER) as year, " +
                   "CAST(EXTRACT(MONTH FROM application_date) AS INTEGER) as month, " +
                   "COUNT(id) as total " +
                   "FROM vaccine_applications " +
                   "GROUP BY year, month " +
                   "ORDER BY year DESC, month DESC", nativeQuery = true)
    List<MonthlyVaccinationReportDTO> countVaccinationsByMonth();

    @Query("SELECT v FROM VaccineApplication v WHERE v.nextDoseDate IS NOT NULL AND v.nextDoseDate <= :endDate ORDER BY v.nextDoseDate ASC")
    List<VaccineApplication> findUpcomingDoses(@Param("endDate") LocalDate endDate);

    // Calcula quantos animais únicos receberam pelo menos uma vacina
    @Query("SELECT COUNT(DISTINCT v.animal.id) FROM VaccineApplication v")
    long countDistinctAnimalsVaccinated();

    // Soma o preço de todas as vacinas que constam no histórico
    @Query("SELECT SUM(v.vaccine.price) FROM VaccineApplication v")
    Double calculateTotalVaccineCost();
}