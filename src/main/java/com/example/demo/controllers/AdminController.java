package com.example.demo.controllers;

import com.example.demo.dto.ApiResponse;
import com.example.demo.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    private final UserAccountService userAccountService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADM_VIEW_USERS')")
    public ApiResponse users() {
        var users = userAccountService.getAllUsers();
        return ApiResponse.success("", users);
    }
}
