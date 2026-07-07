package com.agro_management.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String earring;
    private String name;
    private String sex;
    private Float weight;
    private String race;

    @Column(name = "born_in")
    private LocalDate bornIn;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtor vazio (Exigência do JPA)
    public Animal() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEarring() { return earring; }
    public void setEarring(String earring) { this.earring = earring; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }
    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }
    public LocalDate getBornIn() { return bornIn; }
    public void setBornIn(LocalDate bornIn) { this.bornIn = bornIn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}