package com.agro_management.controllers;

import com.agro_management.models.User;
import com.agro_management.models.dtos.EditUserDTO;
import com.agro_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agro_management.models.dtos.UserResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {
        List<User> users = userRepository.findAll();
        
        List<UserResponseDTO> usersDTO = users.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(usersDTO);
    }

    // Rota para EDITAR colaborador
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody EditUserDTO data) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        if (data.name() != null) user.setName(data.name());
        if (data.email() != null) user.setEmail(data.email());
        if (data.role() != null) user.setRole(data.role());
        if (data.status() != null) user.setStatus(data.status());

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // Rota para REMOVER colaborador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}