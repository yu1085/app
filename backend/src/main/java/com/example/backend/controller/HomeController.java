package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("Welcome to the backend application!");
    }
} 