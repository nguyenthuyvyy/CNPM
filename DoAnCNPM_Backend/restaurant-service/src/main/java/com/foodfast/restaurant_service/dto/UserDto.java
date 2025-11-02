package com.foodfast.restaurant_service.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String role;
}
