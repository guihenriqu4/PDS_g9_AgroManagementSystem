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
    public ResponseEntity<String> registerApplication(@RequestBody VaccineApplicationRequestDTO data) {
        Optional<Animal> animal = animalRepository.findById(data.animalId());
        Optional<Vaccine> vaccine = vaccineRepository.findById(data.vaccineId());
        Optional<User> user = userRepository.findById(data.userId());

        // Valida se as três chaves estrangeiras realmente existem no banco
        if (animal.isEmpty() || vaccine.isEmpty() || user.isEmpty()) {
            return ResponseEntity.badRequest().body("Dados inválidos: Animal, Vacina ou Usuário não encontrados.");
        }

        Vaccine vac = vaccine.get();
        
        // 1. Validação de Estoque (US 2)
        if (vac.getStockQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Estoque insuficiente para a vacina selecionada.");
        }

        // 2. Baixa no Estoque (US 2)
        vac.setStockQuantity(vac.getStockQuantity() - 1);
        vaccineRepository.save(vac); // Salva a vacina com o novo saldo

        // 3. Registro do Histórico
        VaccineApplication app = new VaccineApplication();
        app.setAnimal(animal.get());
        app.setVaccine(vac);
        app.setUser(user.get());
        app.setApplicationDate(data.applicationDate());
        app.setNextDoseDate(data.nextDoseDate());
        
        // 4. Automação do Status (US 4 - Ignora o que vem do front e calcula no back)
        String statusCalculado = "Em Dia";
        if (data.nextDoseDate() != null) {
            // A data atual (hoje)
            java.time.LocalDate hoje = java.time.LocalDate.now();
            
            if (data.nextDoseDate().isBefore(hoje)) {
                statusCalculado = "Atrasada"; // Se a próxima dose já passou de hoje
            } else if (data.nextDoseDate().isBefore(hoje.plusDays(15))) {
                statusCalculado = "Pendente"; // Se a próxima dose é nos próximos 15 dias
            }
        }
        app.setObservation(statusCalculado);
        
        app.setCreatedAt(LocalDateTime.now());
        applicationRepository.save(app);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<VaccineApplicationResponseDTO>> getUpcomingDoses() {
        // Define o limite como os próximos 30 dias a partir de hoje
        java.time.LocalDate thirtyDaysFromNow = java.time.LocalDate.now().plusDays(30);
        
        List<VaccineApplication> upcoming = applicationRepository.findUpcomingDoses(thirtyDaysFromNow);
        
        List<VaccineApplicationResponseDTO> response = upcoming.stream()
                .map(VaccineApplicationResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VaccineApplicationResponseDTO>> listAll() {
        // Busca todos os registros no banco, converte para DTO e retorna
        List<VaccineApplicationResponseDTO> allApplications = applicationRepository.findAll()
                .stream()
                .map(VaccineApplicationResponseDTO::new)
                .toList();
                
        return ResponseEntity.ok(allApplications);
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