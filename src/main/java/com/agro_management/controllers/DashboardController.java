package com.agro_management.controllers;

import com.agro_management.models.VaccineApplication;
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
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private VaccineApplicationRepository applicationRepository;

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        long totalAnimals = animalRepository.count();
        List<VaccineApplication> applications = applicationRepository.findAll();

        // Conta a quantidade de cada status com base na observação automatizada do backend
        long emDia = applications.stream().filter(a -> "Em Dia".equals(a.getObservation())).count();
        long pendentes = applications.stream().filter(a -> "Pendente".equals(a.getObservation())).count();
        long atrasadas = applications.stream().filter(a -> "Atrasada".equals(a.getObservation())).count();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalAnimals", totalAnimals);
        metrics.put("vaccinesUpToDate", emDia);
        metrics.put("pendingVaccines", pendentes);
        metrics.put("delayedVaccines", atrasadas);

        return ResponseEntity.ok(metrics);
    }
}