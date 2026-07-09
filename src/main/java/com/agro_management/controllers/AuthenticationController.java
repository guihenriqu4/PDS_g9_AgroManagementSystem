package com.agro_management.controllers;

import com.agro_management.infra.security.TokenService;
import com.agro_management.models.User;
import com.agro_management.models.dtos.AuthenticationDTO;
import com.agro_management.models.dtos.LoginResponseDTO;
import com.agro_management.models.dtos.RegisterDTO;
import com.agro_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        System.out.println(">>> [LOGIN TRY] Tentando logar com email: " + data.email());

        var userInDb = userRepository.findByEmail(data.email());
        if (userInDb == null) {
            System.out.println(">>> [LOGIN ERROR] Usuário NÃO encontrado no banco pelo e-mail!");
        } else {
            System.out.println(">>> [LOGIN OK] Usuário encontrado: " + userInDb.getUsername());
            System.out.println(">>> [LOGIN HASH] Hash da senha no banco: " + userInDb.getPassword());
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO data) {
        // Verifica se o email já existe no banco
        if(this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Cria o usuário com a senha já criptografada pelo próprio Spring
        User newUser = new User();
        newUser.setName(data.name());
        newUser.setEmail(data.email());
        newUser.setPassword(passwordEncoder.encode(data.password()));
        newUser.setRole(data.role());
        newUser.setCreatedAt(LocalDateTime.now());

        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}