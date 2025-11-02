package com.foodfast.restaurant_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    private Long ownerId;       // ID của user (nếu bạn lưu theo user id)
    private String ownerEmail;  // Email chủ nhà hàng (dùng cho Feign hoặc login)
}
