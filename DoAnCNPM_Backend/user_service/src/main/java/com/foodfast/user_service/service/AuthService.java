package com.foodfast.user_service.service;

import com.foodfast.user_service.auth.*;
import com.foodfast.user_service.model.Role;
import com.foodfast.user_service.model.User;
import com.foodfast.user_service.repository.UserRepository;
import com.foodfast.user_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getFullname(), user.getEmail(), user.getPhone(), user.getRole());
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getFullname(), user.getEmail(), user.getPhone(), user.getRole());
    }
}
