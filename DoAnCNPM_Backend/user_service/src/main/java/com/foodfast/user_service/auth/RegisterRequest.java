package com.foodfast.user_service.auth;

import com.foodfast.user_service.model.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private Role role;
}
