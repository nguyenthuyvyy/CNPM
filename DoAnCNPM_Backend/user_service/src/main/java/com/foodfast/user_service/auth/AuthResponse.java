package com.foodfast.user_service.auth;

import com.foodfast.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String fullname;
    private String email;
    private String phone;
    private Role role;
}
