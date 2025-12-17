package com.example.petshop.repository;

import com.example.petshop.model.Pet;
import com.example.petshop.model.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findBySpeciesIgnoreCase(String species);
    List<Pet> findByStatus(PetStatus status);
    List<Pet> findBySpeciesIgnoreCaseAndStatus(String species, PetStatus status);
}
