package com.foodfast.user_service.client;

import com.foodfast.user_service.client.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "restaurant-service", url = "http://localhost:8083") 
public interface RestaurantClient {

    @GetMapping("/api/restaurants/owner")
    RestaurantDto getRestaurantByOwnerEmail(@RequestParam("email") String email);
}
