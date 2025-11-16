package com.foodfast.order_service.service;

import com.foodfast.order_service.model.Order;
import com.foodfast.order_service.model.OrderItem;
import com.foodfast.order_service.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        // Khởi tạo order với items mutable (ArrayList)
        testOrder = Order.builder()
                .userId("U1")
                .fullname("Nguyen Van A")
                .phone("0123456789")
                .address("123 Street")
                .total(BigDecimal.valueOf(100000))
                .paymethod(1)
                .status(0)
                .items(new ArrayList<>(List.of(
                        new OrderItem(null, "P1", 2, BigDecimal.valueOf(50000))
                )))
                .build();
        orderRepository.save(testOrder);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void testCreateOrder() {
        Order order = Order.builder()
                .userId("U2")
                .fullname("Nguyen Van B")
                .phone("0987654321")
                .address("456 Street")
                .total(BigDecimal.valueOf(200000))
                .paymethod(0)
                .status(0)
                .items(new ArrayList<>(List.of(
                        new OrderItem(null, "P2", 4, BigDecimal.valueOf(50000))
                )))
                .build();
        Order saved = orderRepository.save(order);
        assertNotNull(saved.getId());
        assertEquals(1, saved.getItems().size());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testUpdateOrderStatus() {
        testOrder.setStatus(1);
        Order updated = orderRepository.save(testOrder); // Hibernate sẽ merge mutable list
        assertEquals(1, updated.getStatus());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void testGetOrderById() {
        Optional<Order> found = orderRepository.findById(testOrder.getId());
        assertTrue(found.isPresent());
        assertEquals(testOrder.getId(), found.get().getId());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void testDeleteOrder() {
        orderRepository.deleteById(testOrder.getId());
        Optional<Order> found = orderRepository.findById(testOrder.getId());
        assertFalse(found.isPresent());
    }
}
