package com.foodfast.user_service.auth;

import com.foodfast.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String fullname;
    private String email;
    private String phone;
    private Role role;
    private Long restaurantId;
    private String restaurantName;

    public AuthResponse(String token, String fullname, String email, String phone, Role role) {
        this.token = token;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
