package com.agro_management.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Arquivos estáticos e páginas públicas
                        .requestMatchers("/", "/index.html", "/login.html", "/*.html", "/*.css", "/*.js", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        // 1. Gestão/Cadastro de Usuários (Apenas GESTOR / ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        // 2. Histórico Sanitário
                        .requestMatchers("/sanitary-history/**").hasRole("ADMIN")

                        // 3. Demais requisições (Animais e Vacinas, incluindo POST, PUT, DELETE) 
                        // exigem apenas que o usuário esteja logado no sistema
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Define que as senhas no banco serão comparadas usando hash BCrypt (essencial para segurança)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}