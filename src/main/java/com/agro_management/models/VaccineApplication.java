package com.agro_management.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vaccine_applications")
public class VaccineApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com o Animal
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    // Relacionamento com a Vacina
    @ManyToOne
    @JoinColumn(name = "vaccine_id")
    private Vaccine vaccine;

    // Relacionamento com o Colaborador (Quem aplicou)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "next_dose_date")
    private LocalDate nextDoseDate;

    private String observation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtor vazio
    public VaccineApplication() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public Vaccine getVaccine() { return vaccine; }
    public void setVaccine(Vaccine vaccine) { this.vaccine = vaccine; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }
    public LocalDate getNextDoseDate() { return nextDoseDate; }
    public void setNextDoseDate(LocalDate nextDoseDate) { this.nextDoseDate = nextDoseDate; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}