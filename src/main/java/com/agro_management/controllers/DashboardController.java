package com.agro_management.controllers;

import com.agro_management.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private AnimalRepository animalRepository;

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        // Conta a quantidade real de animais cadastrados no PostgreSQL
        long totalAnimals = animalRepository.count();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalAnimals", totalAnimals);
        metrics.put("vaccinesUpToDate", 3); // 3 vacinas aplicadas do histórico (Mimosa, Amora e Raiva)
        metrics.put("pendingVaccines", 0);
        metrics.put("delayedVaccines", 0);

        return ResponseEntity.ok(metrics);
    }
}