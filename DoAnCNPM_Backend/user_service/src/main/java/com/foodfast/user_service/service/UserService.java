package com.foodfast.user_service.service;

import com.foodfast.user_service.model.User;
import com.foodfast.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            if (updatedUser.getFullname() != null) user.setFullname(updatedUser.getFullname());
            if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getRole() != null) user.setRole(updatedUser.getRole());

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
