package com.foodfast.restaurant_service.controller;

import com.foodfast.restaurant_service.model.Restaurant;
import com.foodfast.restaurant_service.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public Restaurant create(@RequestBody Restaurant restaurant) {
        return restaurantService.createRestaurant(restaurant);
    }

    @GetMapping("/owner/{ownerId}")
    public Restaurant getByOwner(@PathVariable Long ownerId) {
        return restaurantService.getRestaurantByOwner(ownerId);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantService.getAll();
    }

    // ✅ Endpoint cho user_service hoặc frontend dùng
    @GetMapping("/owner")
    public Restaurant getRestaurantByOwnerEmail(@RequestParam String email) {
        return restaurantService.getRestaurantByOwnerEmail(email);
    }
}
