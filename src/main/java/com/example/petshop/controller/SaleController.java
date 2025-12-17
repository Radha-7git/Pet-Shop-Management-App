package com.example.petshop.controller;

import com.example.petshop.model.*;
import com.example.petshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable("id") Long id) {
        return saleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleRequest request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElse(null);
        if (customer == null) {
            return ResponseEntity.badRequest().body("Customer not found");
        }

        // Validate pet IDs and calculate total
        List<Pet> pets = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Long petId : request.getPetIds()) {
            Pet pet = petRepository.findById(petId).orElse(null);
            if (pet == null) {
                return ResponseEntity.badRequest().body("Pet with ID " + petId + " not found");
            }
            if (pet.getStatus() == PetStatus.SOLD) {
                return ResponseEntity.badRequest().body("Pet with ID " + petId + " is already sold");
            }
            pets.add(pet);
            totalPrice = totalPrice.add(pet.getPrice());
        }

        // Create sale
        Sale sale = Sale.builder()
                .customer(customer)
                .totalPrice(totalPrice)
                .date(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        // Create sale items
        for (Pet pet : pets) {
            SaleItem item = SaleItem.builder()
                    .sale(sale)
                    .pet(pet)
                    .price(pet.getPrice())
                    .build();
            sale.getItems().add(item);
            
            // Mark pet as sold
            pet.setStatus(PetStatus.SOLD);
            petRepository.save(pet);
        }

        Sale savedSale = saleRepository.save(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable("id") Long id) {
        if (!saleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        saleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO for creating a sale
    public static class SaleRequest {
        private Long customerId;
        private List<Long> petIds;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public List<Long> getPetIds() {
            return petIds;
        }

        public void setPetIds(List<Long> petIds) {
            this.petIds = petIds;
        }
    }
}
