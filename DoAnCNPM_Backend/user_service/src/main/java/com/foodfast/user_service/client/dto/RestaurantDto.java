package com.foodfast.user_service.client.dto;

import lombok.Data;

@Data
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String ownerEmail;
}
