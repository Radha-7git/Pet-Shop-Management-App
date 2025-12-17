package com.example.petshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PetDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String species;
    private String breed;
    @Min(0)
    private Integer age;
    @NotNull
    private BigDecimal price;
    private String status; // AVAILABLE / SOLD (optional on create)
}
