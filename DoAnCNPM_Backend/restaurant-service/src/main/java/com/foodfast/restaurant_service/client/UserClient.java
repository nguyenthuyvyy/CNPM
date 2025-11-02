package com.foodfast.restaurant_service.client;

import com.foodfast.restaurant_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {
    @GetMapping("/api/users/by-email")
    UserDto getUserByEmail(@RequestParam("email") String email);
}
