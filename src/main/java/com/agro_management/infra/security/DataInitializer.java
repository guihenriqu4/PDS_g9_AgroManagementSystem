package com.agro_management.infra.security;

import com.agro_management.models.User;
import com.agro_management.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String emailGestor = "gestor@agro.com";
        String senhaPura = "123456";
        String hashSenha = passwordEncoder.encode(senhaPura);

        var existingUser = userRepository.findByEmail(emailGestor);

        if (existingUser == null) {
            // Cria novo gestor se não existir
            User gestor = new User();
            gestor.setName("Gestor da Propriedade");
            gestor.setEmail(emailGestor);
            gestor.setPassword(hashSenha);
            gestor.setRole("GESTOR");
            gestor.setCreatedAt(LocalDateTime.now());

            userRepository.save(gestor);
            System.out.println(">>> [LOG] Novo Usuário Gestor cadastrado no banco de dados!");
        } else {
            // Se já existir, força a atualização da senha no formato BCrypt
            User gestor = (User) existingUser;
            gestor.setPassword(hashSenha);
            gestor.setRole("GESTOR");
            userRepository.save(gestor);
            System.out.println(">>> [LOG] Senha do Gestor atualizada com sucesso!");
        }

        System.out.println("======================================================================");
        System.out.println(">>> DADOS DE LOGIN DO GESTOR:");
        System.out.println(">>> Email: gestor@agro.com");
        System.out.println(">>> Senha: 123456");
        System.out.println("======================================================================");
    }
}