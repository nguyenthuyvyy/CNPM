package com.foodfast.drone_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String model;
    private double batteryLevel;
    private String status; // AVAILABLE, DELIVERING, CHARGING, MAINTENANCE
    private double latitude;
    private double longitude;
}
