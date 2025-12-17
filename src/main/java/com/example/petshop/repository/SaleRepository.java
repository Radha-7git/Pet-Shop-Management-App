package com.example.petshop.repository;

import com.example.petshop.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByDateBetween(LocalDateTime from, LocalDateTime to);
}
