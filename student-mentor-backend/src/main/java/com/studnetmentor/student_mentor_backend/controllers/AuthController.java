package com.studnetmentor.student_mentor_backend.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.studnetmentor.student_mentor_backend.dto.AuthResponse;
import com.studnetmentor.student_mentor_backend.dto.LoginRequest;
import com.studnetmentor.student_mentor_backend.dto.RegisterRequest;
import com.studnetmentor.student_mentor_backend.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}

