package com.agro_management.controllers;

import com.agro_management.models.dtos.MonthlyVaccinationReportDTO;
import com.agro_management.repositories.AnimalRepository;
import com.agro_management.repositories.VaccineApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private VaccineApplicationRepository applicationRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping("/vaccinations-by-month")
    public ResponseEntity<List<MonthlyVaccinationReportDTO>> getVaccinationsByMonth() {
        return ResponseEntity.ok(applicationRepository.countVaccinationsByMonth());
    }

    @GetMapping("/general-metrics")
    public ResponseEntity<Map<String, Object>> getGeneralMetrics() {
        long totalAnimals = animalRepository.count();
        long vaccinatedAnimals = applicationRepository.countDistinctAnimalsVaccinated();
        
        // Regra de 3 para a porcentagem (Taxa de Vacinação)
        double taxa = totalAnimals > 0 ? ((double) vaccinatedAnimals / totalAnimals) * 100 : 0.0;
        
        long animaisMes = animalRepository.countAnimalsRegisteredThisMonth();
        
        // Previne valores nulos caso o histórico esteja vazio
        Double custoDouble = applicationRepository.calculateTotalVaccineCost();
        double custoTotal = custoDouble != null ? custoDouble : 0.0;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("taxaVacinacao", Math.round(taxa));
        metrics.put("animaisMes", animaisMes);
        metrics.put("custoTotal", custoTotal);

        return ResponseEntity.ok(metrics);
    }
}