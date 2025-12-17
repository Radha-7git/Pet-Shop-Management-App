package com.example.petshop.controller;

import com.example.petshop.model.Customer;
import com.example.petshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long id, @RequestBody Customer customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    if (customerDetails.getFullName() != null) {
                        customer.setFullName(customerDetails.getFullName());
                    }
                    if (customerDetails.getPhone() != null) {
                        customer.setPhone(customerDetails.getPhone());
                    }
                    if (customerDetails.getEmail() != null) {
                        customer.setEmail(customerDetails.getEmail());
                    }
                    Customer updated = customerRepository.save(customer);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
