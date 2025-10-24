package com.foodfast.user_service.controller;

import com.foodfast.user_service.dto.UserDTO;
import com.foodfast.user_service.model.User;
import com.foodfast.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(u -> new UserDTO(u.getId(), u.getFullname(), u.getEmail(), u.getPhone(), u.getRole()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(u -> ResponseEntity.ok(new UserDTO(u.getId(), u.getFullname(), u.getEmail(), u.getPhone(), u.getRole())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        User saved = userService.createUser(user);
        return new UserDTO(saved.getId(), saved.getFullname(), saved.getEmail(), saved.getPhone(), saved.getRole());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(new UserDTO(updated.getId(), updated.getFullname(), updated.getEmail(), updated.getPhone(), updated.getRole()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
