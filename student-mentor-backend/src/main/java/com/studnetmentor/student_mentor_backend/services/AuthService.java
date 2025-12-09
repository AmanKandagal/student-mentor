package com.studnetmentor.student_mentor_backend.services;

import com.studnetmentor.student_mentor_backend.dto.AuthResponse;
import com.studnetmentor.student_mentor_backend.dto.LoginRequest;
import com.studnetmentor.student_mentor_backend.dto.RegisterRequest;
import com.studnetmentor.student_mentor_backend.models.User;
import com.studnetmentor.student_mentor_backend.repositories.UserRepository;
import com.studnetmentor.student_mentor_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
