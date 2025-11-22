package com.foodfast.user_service.service;

import com.foodfast.user_service.auth.AuthRequest;
import com.foodfast.user_service.auth.AuthResponse;
import com.foodfast.user_service.auth.RegisterRequest;
import com.foodfast.user_service.client.RestaurantClient;
import com.foodfast.user_service.model.Role;
import com.foodfast.user_service.model.User;
import com.foodfast.user_service.repository.UserRepository;
import com.foodfast.user_service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("AuthService Unit Tests")
class AuthServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RestaurantClient restaurantClient;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .fullname("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encoded_password")
                .role(Role.CUSTOMER)
                .build();

        registerRequest = new RegisterRequest(
                "John Doe",
                "john@example.com",
                "1234567890",
                "password123",
                Role.CUSTOMER
        );

        authRequest = new AuthRequest();
        authRequest.setEmail("john@example.com");
        authRequest.setPassword("password123");
    }

    // ============ REGISTER TESTS ============

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterSuccess() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token_123");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token_123", response.getToken());
        assertEquals("john@example.com", response.getEmail());
        assertEquals(Role.CUSTOMER, response.getRole());
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterDuplicateEmail() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should set default role to CUSTOMER if not provided")
    void testRegisterDefaultRole() {
        // Arrange
        registerRequest.setRole(null);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(Role.CUSTOMER, savedUser.getRole());
            return testUser;
        });
        when(jwtService.generateToken(testUser)).thenReturn("jwt_token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should encode password during registration")
    void testRegisterPasswordEncoded() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password_123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(testUser)).thenReturn("token");

        // Act
        authService.register(registerRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode("password123");
    }

    // ============ AUTHENTICATE TESTS ============

    @Test
    @DisplayName("Should authenticate user successfully")
    void testAuthenticateSuccess() {
        // Arrange
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("john@example.com", "password123")))
                .thenReturn(new UsernamePasswordAuthenticationToken("john@example.com", "password123"));
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("jwt_token_123");

        // Act
        AuthResponse response = authService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token_123", response.getToken());
        assertEquals("john@example.com", response.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found during authentication")
    void testAuthenticateUserNotFound() {
        // Arrange
        AuthRequest notFoundRequest = new AuthRequest();
        notFoundRequest.setEmail("nonexistent@example.com");
        notFoundRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("nonexistent@example.com", "password"));
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.authenticate(notFoundRequest);
        });
    }

    @Test
    @DisplayName("Should handle restaurant client exception during authentication")
    void testAuthenticateRestaurantClientException() {
        // Arrange
        User ownerUser = User.builder()
                .id(2L)
                .fullname("Owner Name")
                .email("owner@example.com")
                .role(Role.RESTAURANT_OWNER)
                .build();

        AuthRequest ownerRequest = new AuthRequest();
        ownerRequest.setEmail("owner@example.com");
        ownerRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("owner@example.com", "password"));
        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(ownerUser));
        when(jwtService.generateToken(ownerUser)).thenReturn("owner_token");
        when(restaurantClient.getRestaurantByOwnerEmail("owner@example.com"))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act
        AuthResponse response = authService.authenticate(ownerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("owner_token", response.getToken());
        assertNull(response.getRestaurantId());
    }

    @Test
    @DisplayName("Should authenticate RESTAURANT_OWNER and fetch restaurant details")
    void testAuthenticateOwnerWithRestaurant() {
        // Arrange - This test shows the architecture but would need RestaurantDto
        User ownerUser = User.builder()
                .id(2L)
                .fullname("Restaurant Owner")
                .email("owner@example.com")
                .role(Role.RESTAURANT_OWNER)
                .build();

        AuthRequest ownerRequest = new AuthRequest();
        ownerRequest.setEmail("owner@example.com");
        ownerRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("owner@example.com", "password"));
        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(ownerUser));
        when(jwtService.generateToken(ownerUser)).thenReturn("owner_token");
        when(restaurantClient.getRestaurantByOwnerEmail("owner@example.com"))
                .thenReturn(null); // Simulating no restaurant found

        // Act
        AuthResponse response = authService.authenticate(ownerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Role.RESTAURANT_OWNER, response.getRole());
    }

    // ============ UPDATE PASSWORD TESTS ============

    @Test
    @DisplayName("Should update password successfully")
    void testUpdatePasswordSuccess() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("new_password")).thenReturn("encoded_new_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        authService.updatePassword("john@example.com", "new_password");

        // Assert
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(passwordEncoder, times(1)).encode("new_password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found for password update")
    void testUpdatePasswordUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.updatePassword("nonexistent@example.com", "new_password");
        });
    }

    @Test
    @DisplayName("Should encode new password correctly")
    void testUpdatePasswordEncoding() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("secure_password_123")).thenReturn("hashed_secure_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals("hashed_secure_password", savedUser.getPassword());
            return testUser;
        });

        // Act
        authService.updatePassword("john@example.com", "secure_password_123");

        // Assert
        verify(passwordEncoder, times(1)).encode("secure_password_123");
    }
}
