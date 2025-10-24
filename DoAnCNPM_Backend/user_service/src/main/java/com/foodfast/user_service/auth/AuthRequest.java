package com.foodfast.user_service.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
