package com.foodfast.restaurant_service.service;

import com.foodfast.restaurant_service.client.UserClient;
import com.foodfast.restaurant_service.dto.UserDto;
import com.foodfast.restaurant_service.model.Restaurant;
import com.foodfast.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RestaurantServiceUnitTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant testRestaurant;
    private UserDto testOwner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare test data
        testOwner = new UserDto();
        testOwner.setId(1L);
        testOwner.setEmail("owner@example.com");
        testOwner.setFullname("John Doe");
        testOwner.setRole("RESTAURANT_OWNER");

        testRestaurant = new Restaurant();
        testRestaurant.setId(1L);
        testRestaurant.setName("Test Restaurant");
        testRestaurant.setAddress("123 Main St");
        testRestaurant.setOwnerId(1L);
        testRestaurant.setOwnerEmail("owner@example.com");
    }

    @Test
    void testCreateRestaurantSuccess() {
        // Arrange
        when(userClient.getUserByEmail(testRestaurant.getOwnerEmail()))
                .thenReturn(testOwner);
        when(restaurantRepository.save(any(Restaurant.class)))
                .thenReturn(testRestaurant);

        // Act
        Restaurant result = restaurantService.createRestaurant(testRestaurant);

        // Assert
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("owner@example.com", result.getOwnerEmail());

        // Verify interactions
        verify(userClient, times(1)).getUserByEmail(testRestaurant.getOwnerEmail());
        verify(restaurantRepository, times(1)).save(testRestaurant);
    }

    @Test
    void testCreateRestaurantWithInvalidRole() {
        // Arrange
        UserDto invalidOwner = new UserDto();
        invalidOwner.setId(1L);
        invalidOwner.setEmail("owner@example.com");
        invalidOwner.setRole("CUSTOMER"); // Invalid role

        when(userClient.getUserByEmail(testRestaurant.getOwnerEmail()))
                .thenReturn(invalidOwner);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.createRestaurant(testRestaurant);
        });

        assertTrue(exception.getMessage().contains("không có quyền tạo nhà hàng"));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testCreateRestaurantWithNullOwner() {
        // Arrange
        when(userClient.getUserByEmail(testRestaurant.getOwnerEmail()))
                .thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.createRestaurant(testRestaurant);
        });

        assertTrue(exception.getMessage().contains("không hợp lệ"));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testCreateRestaurantUserClientException() {
        // Arrange
        when(userClient.getUserByEmail(anyString()))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.createRestaurant(testRestaurant);
        });

        assertTrue(exception.getMessage().contains("Không thể xác thực"));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void testGetRestaurantByOwnerSuccess() {
        // Arrange
        when(restaurantRepository.findByOwnerId(1L))
                .thenReturn(Optional.of(testRestaurant));

        // Act
        Restaurant result = restaurantService.getRestaurantByOwner(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        assertEquals(1L, result.getOwnerId());
        verify(restaurantRepository, times(1)).findByOwnerId(1L);
    }

    @Test
    void testGetRestaurantByOwnerNotFound() {
        // Arrange
        when(restaurantRepository.findByOwnerId(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.getRestaurantByOwner(999L);
        });

        assertTrue(exception.getMessage().contains("not found"));
        verify(restaurantRepository, times(1)).findByOwnerId(999L);
    }

    @Test
    void testGetRestaurantByOwnerEmailSuccess() {
        // Arrange
        when(restaurantRepository.findByOwnerEmail("owner@example.com"))
                .thenReturn(Optional.of(testRestaurant));

        // Act
        Restaurant result = restaurantService.getRestaurantByOwnerEmail("owner@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
        assertEquals("owner@example.com", result.getOwnerEmail());
        verify(restaurantRepository, times(1)).findByOwnerEmail("owner@example.com");
    }

    @Test
    void testGetRestaurantByOwnerEmailNotFound() {
        // Arrange
        when(restaurantRepository.findByOwnerEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restaurantService.getRestaurantByOwnerEmail("nonexistent@example.com");
        });

        assertTrue(exception.getMessage().contains("not found"));
        verify(restaurantRepository, times(1)).findByOwnerEmail("nonexistent@example.com");
    }

    @Test
    void testGetAllRestaurants() {
        // Arrange
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(testRestaurant);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Another Restaurant");
        restaurant2.setAddress("456 Oak St");
        restaurant2.setOwnerId(2L);
        restaurants.add(restaurant2);

        when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Act
        List<Restaurant> result = restaurantService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Restaurant", result.get(0).getName());
        assertEquals("Another Restaurant", result.get(1).getName());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRestaurantsEmpty() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Restaurant> result = restaurantService.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restaurantRepository, times(1)).findAll();
    }
}
