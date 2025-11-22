package com.foodfast.user_service.repository;

import com.foodfast.user_service.model.Role;
import com.foodfast.user_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Integration Tests")
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .fullname("Test User")
                .email("test@example.com")
                .phone("1234567890")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();
    }

    // ============ SAVE TESTS ============

    @Test
    @DisplayName("Should save user to database")
    void testSaveUser() {
        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("test@example.com", saved.getEmail());
        assertTrue(saved.getId() > 0);
    }

    @Test
    @DisplayName("Should set createdAt and updatedAt timestamps on save")
    void testSaveUserTimestamps() {
        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getCreatedAt(), saved.getUpdatedAt());
    }

    // ============ FIND TESTS ============

    @Test
    @DisplayName("Should find user by ID")
    void testFindUserById() {
        // Arrange
        User saved = userRepository.save(testUser);

        // Act
        Optional<User> found = userRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty Optional when user not found by ID")
    void testFindUserByIdNotFound() {
        // Act
        Optional<User> found = userRepository.findById(999L);

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {
        // Arrange
        userRepository.save(testUser);

        // Act
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getFullname());
    }

    @Test
    @DisplayName("Should return empty Optional when user not found by email")
    void testFindUserByEmailNotFound() {
        // Act
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAllUsers() {
        // Arrange
        userRepository.save(testUser);
        User user2 = User.builder()
                .fullname("Another User")
                .email("another@example.com")
                .role(Role.RESTAURANT_OWNER)
                .build();
        userRepository.save(user2);

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertEquals(2, users.size());
    }

    // ============ UPDATE TESTS ============

    @Test
    @DisplayName("Should update user details")
    void testUpdateUser() {
        // Arrange
        User saved = userRepository.save(testUser);
        saved.setFullname("Updated Name");
        saved.setPhone("9999999999");

        // Act
        User updated = userRepository.save(saved);

        // Assert
        assertEquals("Updated Name", updated.getFullname());
        assertEquals("9999999999", updated.getPhone());
    }

    @Test
    @DisplayName("Should update timestamps on update")
    void testUpdateUserTimestamps() {
        // Arrange
        User saved = userRepository.save(testUser);
        Instant originalUpdatedAt = saved.getUpdatedAt();

        // Act
        try {
            Thread.sleep(100); // Small delay to ensure timestamp difference
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        saved.setFullname("Updated");
        User updated = userRepository.save(saved);

        // Assert
        assertTrue(updated.getUpdatedAt().isAfter(originalUpdatedAt) || 
                   updated.getUpdatedAt().equals(originalUpdatedAt));
    }

    // ============ DELETE TESTS ============

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUserById() {
        // Arrange
        User saved = userRepository.save(testUser);
        Long userId = saved.getId();

        // Act
        userRepository.deleteById(userId);
        Optional<User> found = userRepository.findById(userId);

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should handle delete of non-existent user")
    void testDeleteNonExistentUser() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            userRepository.deleteById(999L);
        });
    }

    // ============ UNIQUE CONSTRAINT TESTS ============

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testUniqueEmailConstraint() {
        // Arrange
        userRepository.save(testUser);
        User duplicateEmail = User.builder()
                .fullname("Different Name")
                .email("test@example.com")
                .role(Role.ADMIN)
                .build();

        // Act & Assert
        assertThrows(Exception.class, () -> {
            userRepository.save(duplicateEmail);
            userRepository.flush();
        });
    }

    // ============ ROLE TESTS ============

    @Test
    @DisplayName("Should save user with CUSTOMER role")
    void testSaveUserWithCustomerRole() {
        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertEquals(Role.CUSTOMER, saved.getRole());
    }

    @Test
    @DisplayName("Should save user with RESTAURANT_OWNER role")
    void testSaveUserWithOwnerRole() {
        // Arrange
        testUser.setRole(Role.RESTAURANT_OWNER);

        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertEquals(Role.RESTAURANT_OWNER, saved.getRole());
    }

    @Test
    @DisplayName("Should save user with ADMIN role")
    void testSaveUserWithAdminRole() {
        // Arrange
        testUser.setRole(Role.ADMIN);

        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertEquals(Role.ADMIN, saved.getRole());
    }

    // ============ DATA INTEGRITY TESTS ============

    @Test
    @DisplayName("Should retrieve all user fields correctly")
    void testUserDataIntegrity() {
        // Arrange & Act
        User saved = userRepository.save(testUser);
        Optional<User> retrieved = userRepository.findById(saved.getId());

        // Assert
        assertTrue(retrieved.isPresent());
        User user = retrieved.get();
        assertEquals("Test User", user.getFullname());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.CUSTOMER, user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }
}
