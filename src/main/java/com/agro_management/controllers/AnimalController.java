package com.agro_management.controllers;

import com.agro_management.models.Animal;
import com.agro_management.models.dtos.AnimalRequestDTO;
import com.agro_management.repositories.AnimalRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    // Tarefas 1 e 2: Criar consulta de animais e Implementar filtros de busca
    @GetMapping
    public ResponseEntity<List<Animal>> searchAnimals(
            @RequestParam(required = false) String earring,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) String sex) {

        List<Animal> animals = animalRepository.findWithFilters(earring, name, race, sex);
        return ResponseEntity.ok(animals);
    }

    // Tarefa 3: Exibir detalhes do animal
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalDetails(@PathVariable Long id) {
        Optional<Animal> animal = animalRepository.findById(id);
        
        if (animal.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o ID não existir
        }
        
        return ResponseEntity.ok(animal.get());
    }

    @PostMapping
    public ResponseEntity<Void> createAnimal(@RequestBody @Valid AnimalRequestDTO data) {
        Animal animal = new Animal();
        animal.setEarring(data.earring());
        animal.setName(data.name());
        animal.setSex(data.sex());
        animal.setWeight(data.weight());
        animal.setRace(data.race());
        animal.setBornIn(data.bornIn());
        animal.setCreatedAt(LocalDateTime.now());
        animalRepository.save(animal);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAnimal(@PathVariable Long id, @RequestBody @Valid AnimalRequestDTO data) {
        Optional<Animal> optionalAnimal = animalRepository.findById(id);
        if (optionalAnimal.isEmpty()) return ResponseEntity.notFound().build();

        Animal animal = optionalAnimal.get();
        animal.setEarring(data.earring());
        animal.setName(data.name());
        animal.setSex(data.sex());
        animal.setWeight(data.weight());
        animal.setRace(data.race());
        animal.setBornIn(data.bornIn());
        animalRepository.save(animal);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        if (!animalRepository.existsById(id)) return ResponseEntity.notFound().build();
        animalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}