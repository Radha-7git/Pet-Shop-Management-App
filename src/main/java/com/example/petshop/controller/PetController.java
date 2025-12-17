package com.example.petshop.controller;

import com.example.petshop.model.Pet;
import com.example.petshop.model.PetStatus;
import com.example.petshop.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public List<Pet> list(@RequestParam(value = "species", required = false) String species,
                          @RequestParam(value = "status", required = false) PetStatus status) {
        if (species != null && status != null) {
            return petRepository.findBySpeciesIgnoreCaseAndStatus(species, status);
        } else if (species != null) {
            return petRepository.findBySpeciesIgnoreCase(species);
        } else if (status != null) {
            return petRepository.findByStatus(status);
        }
        return petRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pet> create(@RequestBody Pet pet) {
        if (pet.getStatus() == null) {
            pet.setStatus(PetStatus.AVAILABLE);
        }
        return ResponseEntity.ok(petRepository.save(pet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> get(@PathVariable("id") Long id) {
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!petRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        petRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
