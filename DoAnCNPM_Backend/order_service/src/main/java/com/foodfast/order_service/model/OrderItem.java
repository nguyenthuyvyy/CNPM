package com.foodfast.order_service.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idProduct;
    private Integer quantity;
    private BigDecimal price;
}
