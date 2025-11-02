package com.foodfast.user_service.service;

import com.foodfast.user_service.auth.AuthRequest;
import com.foodfast.user_service.auth.AuthResponse;
import com.foodfast.user_service.auth.RegisterRequest;
import com.foodfast.user_service.client.RestaurantClient;
import com.foodfast.user_service.client.dto.RestaurantDto;
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
    private final RestaurantClient restaurantClient;

    /**
     * Đăng ký
     */
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

    /**
     * Đăng nhập
     */
    public AuthResponse authenticate(AuthRequest request) {
        // ✅ Kiểm tra email/pass
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Sinh token
        String token = jwtService.generateToken(user);

        // ✅ Nếu là OWNER thì gọi restaurant-service
        Long restaurantId = null;
        String restaurantName = null;

        if (user.getRole() == Role.RESTAURANT_OWNER) {
            try {
                RestaurantDto restaurant = restaurantClient.getRestaurantByOwnerEmail(user.getEmail());
                if (restaurant != null) {
                    restaurantId = restaurant.getId();
                    restaurantName = restaurant.getName();
                }
            } catch (Exception e) {
                System.out.println("⚠️ Không thể kết nối restaurant-service: " + e.getMessage());
            }
        }

        return new AuthResponse(
                token,
                user.getFullname(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                restaurantId,
                restaurantName
        );
    }

    /**
     * Quên mật khẩu
     */
    public void updatePassword(String email, String newPassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
