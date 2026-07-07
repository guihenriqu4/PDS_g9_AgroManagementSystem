package com.agro_management.repositories;

import com.agro_management.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT a FROM Animal a WHERE " +
           "(:earring IS NULL OR a.earring ILIKE %:earring%) AND " +
           "(:name IS NULL OR a.name ILIKE %:name%) AND " +
           "(:race IS NULL OR a.race ILIKE %:race%) AND " +
           "(:sex IS NULL OR a.sex = :sex)")
    List<Animal> findWithFilters(
            @Param("earring") String earring, 
            @Param("name") String name, 
            @Param("race") String race, 
            @Param("sex") String sex);
}