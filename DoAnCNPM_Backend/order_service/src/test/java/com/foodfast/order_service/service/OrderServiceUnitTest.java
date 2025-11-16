package com.foodfast.order_service.service;

import com.foodfast.order_service.model.Order;
import com.foodfast.order_service.model.OrderItem;
import com.foodfast.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceUnitTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void testCreateOrder() {
        Order order = Order.builder()
                .fullname("Nguyen Van A")
                .phone("0901234567")
                .address("123 Street")
                .total(new BigDecimal("200000"))
                .items(Arrays.asList(new OrderItem(null, "P001", 2, new BigDecimal("100000"))))
                .status(0)
                .paymethod(1)
                .build();

        when(orderRepository.save(order)).thenReturn(order);

        Order created = orderService.createOrder(order);
        assertNotNull(created);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = Order.builder().id(1L).status(0).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updated = orderService.updateOrderStatus(1L, 1);
        assertEquals(1, updated.getStatus());
    }
}
