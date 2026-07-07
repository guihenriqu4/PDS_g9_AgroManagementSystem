package com.agro_management.controllers;

import com.agro_management.models.Animal;
import com.agro_management.models.User;
import com.agro_management.models.Vaccine;
import com.agro_management.models.VaccineApplication;
import com.agro_management.models.dtos.VaccineApplicationRequestDTO;
import com.agro_management.models.dtos.VaccineApplicationResponseDTO;
import com.agro_management.repositories.AnimalRepository;
import com.agro_management.repositories.UserRepository;
import com.agro_management.repositories.VaccineApplicationRepository;
import com.agro_management.repositories.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sanitary-history")
public class SanitaryHistoryController {

    @Autowired private VaccineApplicationRepository applicationRepository;
    @Autowired private AnimalRepository animalRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private UserRepository userRepository;

    // Rota POST: Registrar nova aplicação de vacina
    @PostMapping
    public ResponseEntity<Void> registerApplication(@RequestBody VaccineApplicationRequestDTO data) {
        Optional<Animal> animal = animalRepository.findById(data.animalId());
        Optional<Vaccine> vaccine = vaccineRepository.findById(data.vaccineId());
        Optional<User> user = userRepository.findById(data.userId());

        // Valida se as três chaves estrangeiras realmente existem no banco
        if (animal.isEmpty() || vaccine.isEmpty() || user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        VaccineApplication app = new VaccineApplication();
        app.setAnimal(animal.get());
        app.setVaccine(vaccine.get());
        app.setUser(user.get());
        app.setApplicationDate(data.applicationDate());
        app.setNextDoseDate(data.nextDoseDate());
        app.setObservation(data.observation());
        app.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(app);
        return ResponseEntity.ok().build();
    }

    // Rota GET: Buscar histórico de um animal específico
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<VaccineApplicationResponseDTO>> getHistory(@PathVariable Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            return ResponseEntity.notFound().build();
        }

        List<VaccineApplication> applications = applicationRepository.findByAnimalId(animalId);
        
        List<VaccineApplicationResponseDTO> response = applications.stream()
                .map(VaccineApplicationResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}