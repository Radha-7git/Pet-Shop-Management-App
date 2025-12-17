package com.example.petshop.controller;

import com.example.petshop.model.AppUser;
import com.example.petshop.model.Role;
import com.example.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        if (request.getRole() == null) {
            return ResponseEntity.badRequest().body("Role is required");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(request.getPassword())  // Note: In production, hash this password
                .role(request.getRole())
                .build();

        AppUser savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
                        // Check if new email already exists
                        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                            return ResponseEntity.badRequest().body("Email already exists");
                        }
                        user.setEmail(request.getEmail());
                    }
                    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        user.setPassword(request.getPassword());  // Note: In production, hash this
                    }
                    if (request.getRole() != null) {
                        user.setRole(request.getRole());
                    }
                    AppUser updated = userRepository.save(user);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO for user requests
    public static class UserRequest {
        private String email;
        private String password;
        private Role role;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }
}
