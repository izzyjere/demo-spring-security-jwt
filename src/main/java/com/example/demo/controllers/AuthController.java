package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CreateUser;
import com.example.demo.services.UserAccountService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserAccountService userAccountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody CreateUser request) {
        try {
            var user = userAccountService.register(request.firstName(), request.lastName(), request.email(), request.password());
            return ResponseEntity.ok(ApiResponse.success("User registered successfully."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestParam String username, @RequestParam String password) {
        try {
            var token = userAccountService.login(username, password);
            return ResponseEntity.ok(ApiResponse.success("Login successful.", token));
        } catch (Exception e) {
            log.error("login failed", e);
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
}

