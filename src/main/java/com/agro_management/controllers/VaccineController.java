package com.agro_management.controllers;

import com.agro_management.models.Vaccine;
import com.agro_management.models.dtos.VaccineRequestDTO;
import com.agro_management.repositories.VaccineRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vaccines")
public class VaccineController {

    @Autowired private VaccineRepository vaccineRepository;

    @GetMapping
    public ResponseEntity<List<Vaccine>> listAll() {
        return ResponseEntity.ok(vaccineRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createVaccine(@RequestBody @Valid VaccineRequestDTO data) {
        Vaccine vaccine = new Vaccine();
        vaccine.setName(data.name());
        vaccine.setManufacturer(data.manufacturer());
        vaccine.setStockQuantity(data.stockQuantity());
        vaccine.setCreatedAt(LocalDateTime.now());
        vaccineRepository.save(vaccine);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVaccine(@PathVariable Long id, @RequestBody @Valid VaccineRequestDTO data) {
        Optional<Vaccine> optionalVaccine = vaccineRepository.findById(id);
        if (optionalVaccine.isEmpty()) return ResponseEntity.notFound().build();

        Vaccine vaccine = optionalVaccine.get();
        vaccine.setName(data.name());
        vaccine.setManufacturer(data.manufacturer());
        vaccine.setStockQuantity(data.stockQuantity());
        vaccineRepository.save(vaccine);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable Long id) {
        if (!vaccineRepository.existsById(id)) return ResponseEntity.notFound().build();
        vaccineRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}