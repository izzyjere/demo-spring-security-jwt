package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    private final UserAccountService userAccountService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADM_VIEW_USERS')")
    public ResponseEntity<ApiResponse> users() {
        var users = userAccountService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("", users));
    }
}
