package com.foodfast.user_service.dto;

import com.foodfast.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private Role role;
}
