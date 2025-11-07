package com.example.demo.controllers;

import com.example.demo.services.UserAccountService;
import com.example.demo.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserAccountService userAccountService;
    private final JwtUtils jwtUtils;

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
            var user = userAccountService.login(username, password);
            Map<String, Object> claims = new HashMap<>();
            claims.put("GivenName", user.getFirstName());
            claims.put("SurName", user.getLastName());
            claims.put("Id", user.getId());
            var token = jwtUtils.generateToken(user, claims);
            return ResponseEntity.ok(ApiResponse.success("Login successful.", token));
        } catch (Exception e) {
            log.error("login failed", e);
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }

    public record CreateUser(String email, String password, String firstName, String lastName) {
    }

    public record ApiResponse(boolean success, String message, Object data) {
        public static ApiResponse success(String message) {
            return new ApiResponse(true, message, null);
        }

        public static ApiResponse success(String message, Object data) {
            return new ApiResponse(true, message, data);
        }

        public static ApiResponse error(String message) {
            return new ApiResponse(false, message, null);
        }
    }
}
