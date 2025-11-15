package com.foodfast.order_service.service;

import com.foodfast.order_service.model.Order;
import com.foodfast.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));
        List<Order> orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrderByIdFound() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Optional<Order> result = orderService.getOrderById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Order> result = orderService.getOrderById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);
        Order created = orderService.createOrder(order);
        assertNotNull(created);
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setStatus(0);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updated = orderService.updateOrderStatus(1L, 1);
        assertEquals(1, updated.getStatus());
    }

    @Test
    void testUpdateOrderStatusNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Order updated = orderService.updateOrderStatus(1L, 1);
        assertNull(updated);
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(1L);
        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    // ✅ Fail test example
    @Test
    void testFailExample() {
        fail("This test is intentionally failed to trigger CI failure!");
    }
}
