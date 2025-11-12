package com.foodfast.drone_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // Endpoint root '/'
    @GetMapping("/")
    public String home() {
        return "Drone Service is running! API is available at /api/drones";
    }

    // Ví dụ endpoint kiểm tra sức khỏe service
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
