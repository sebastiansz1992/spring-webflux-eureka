package com.sebastian.springcloud.msvc.users.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.springcloud.msvc.users.entities.User;
import com.sebastian.springcloud.msvc.users.services.IUserService;

@RestController
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "User not found with id: " + id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "User not found with username: " + username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "User not found with email: " + email));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        
        // Validate username uniqueness
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Username already exists: " + user.getUsername()));
        }
        
        // Validate email uniqueness
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Email already exists: " + user.getEmail()));
        }
        
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found with id: " + id));
        }
        
        User existingUser = userOptional.get();
        
        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(user.getUsername()) && 
            userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Username already exists: " + user.getUsername()));
        }
        
        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(user.getEmail()) && 
            userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Email already exists: " + user.getEmail()));
        }
        
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.getEnabled());
        
        User updatedUser = userService.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found with id: " + id));
        }
        
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
