package com.foodfast.restaurant_service.service;

import com.foodfast.restaurant_service.client.UserClient;
import com.foodfast.restaurant_service.dto.UserDto;
import com.foodfast.restaurant_service.model.Restaurant;
import com.foodfast.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserClient userClient;

    public Restaurant createRestaurant(Restaurant restaurant) {
        // ✅ Kiểm tra email chủ nhà hàng có tồn tại & đúng vai trò
        try {
            UserDto owner = userClient.getUserByEmail(restaurant.getOwnerEmail());

            if (owner == null || !"RESTAURANT_OWNER".equals(owner.getRole())) {
                throw new RuntimeException("Owner email không hợp lệ hoặc không có quyền tạo nhà hàng");
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể xác thực owner email: " + e.getMessage());
        }

        return restaurantRepository.save(restaurant);
    }

    public Restaurant getRestaurantByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found for ownerId: " + ownerId));
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantByOwnerEmail(String email) {
        return restaurantRepository.findByOwnerEmail(email)
                .orElseThrow(() -> new RuntimeException("Restaurant not found for owner email: " + email));
    }
}
